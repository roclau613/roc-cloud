package com.roc.cloud.canary.gateway.starter.filter;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

import com.roc.cloud.canary.gateway.starter.balancer.CanaryLoadBalancer;
import com.roc.cloud.canary.gateway.starter.tenant.TenantService;
import java.net.URI;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.client.loadbalancer.reactive.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.reactive.Request;
import org.springframework.cloud.client.loadbalancer.reactive.Response;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway全局fitler <br>
 * <p>
 * 1. 参考：{@code org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter}
 * <p>
 * 2.注意ordered的实现，返回的值应该需要跟ReactiveLoadBalancerClientFilter.getOrder一样或者在前面
 *
 * @author Roc
 * @version 1.0, 2020/5/5 15:16
 */
public class CanaryLoadBalancerFilter implements GlobalFilter, Ordered {

    private static final Log log = LogFactory.getLog(CanaryLoadBalancerFilter.class);

    private static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10149;

    private final LoadBalancerClientFactory clientFactory;
    private final LoadBalancerProperties balancerProperties;
    private final TenantService tenantService;


    /**
     * 构造器
     *
     * @param clientFactory :
     * @param properties    : 负载均衡配置
     * @param tenantService : 租户配置
     * @return
     * @author Roc
     * @date 14:09 2020/9/22
     **/
    public CanaryLoadBalancerFilter(LoadBalancerClientFactory clientFactory, LoadBalancerProperties properties,
                                    TenantService tenantService) {
        this.clientFactory = clientFactory;
        this.balancerProperties = properties;
        this.tenantService = tenantService;
    }

    /**
     * Process the Web request and (optionally) delegate to the next {@code WebFilter} through the given {@link
     * GatewayFilterChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 处理消息头信息
        setHeaderInfo(exchange);

        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
        ///boolean isLbModel = !"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix);

        // lb 模式
        /// if (url == null || isLbModel) {
        ///     return chain.filter(exchange);
        /// }

        addOriginalRequestUrl(exchange, url);

        if (log.isTraceEnabled()) {
            log.trace(CanaryLoadBalancerFilter.class.getSimpleName()
                    + " url before: " + url);
        }

        return choose(exchange).doOnNext(response -> {

            if (!response.hasServer()) {
                throw NotFoundException.create(balancerProperties.isUse404(),
                        "Unable to find instance for " + url.getHost());
            }

            // if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
            // if the loadbalancer doesn't provide one.
            String overrideScheme = null;
            if (schemePrefix != null) {
                overrideScheme = url.getScheme();
            }

            DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(
                    response.getServer(), overrideScheme);

            URI requestUrl = reconstructUri(serviceInstance, url);

            if (log.isTraceEnabled()) {
                log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
            }
            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
        }).then(chain.filter(exchange));
    }


    /**
     * 设置消息头信息
     *
     * @param exchange :
     * @author Roc
     * @date 15:34 2020/9/21
     **/
    private void setHeaderInfo(ServerWebExchange exchange) {
        if (null != tenantService) {
            String version = tenantService.getVersion(exchange);
            if (StringUtils.isNotBlank(version)) {
                exchange.getRequest().mutate().header(HeaderConstant.CANARY_VERSION_HEADER, version);
            }
        }
    }

    protected URI reconstructUri(ServiceInstance serviceInstance, URI original) {
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }

    private Mono<Response<ServiceInstance>> choose(ServerWebExchange exchange) {

        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
        boolean isLbModel = !"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix);
        String serverId = "";
        // lb 模式
        if (url == null || isLbModel) {
            Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
            serverId = route.getUri().getHost();
        } else {
            URI uri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
            serverId = uri.getHost();
        }
        CanaryLoadBalancer loadBalancer = new CanaryLoadBalancer(
                clientFactory.getLazyProvider(serverId, ServiceInstanceListSupplier.class), serverId);
        if (loadBalancer == null) {
            throw new NotFoundException("No loadbalancer available for " + serverId);
        } else {
            return loadBalancer.choose(this.createRequest(exchange));
        }
    }

    private Request createRequest(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        Request<HttpHeaders> request = new DefaultRequest<>(headers);
        return request;
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat analogous to Servlet {@code load-on-startup}
     * values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        // 请注意这个ORDER 一定和内置的LoadBalancerFilter顺序保持一直
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }
}
