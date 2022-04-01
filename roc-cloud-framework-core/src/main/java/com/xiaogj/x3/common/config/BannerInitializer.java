package com.roc.cloud.common.config;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.taobao.text.Color;
import com.roc.cloud.common.constant.CommonCoreConstant;
import com.roc.cloud.common.utils.CustomBanner;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Banner初始化
 *
 * @author: Roc
 * @date 2019/8/28
 */
public class BannerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    /**
     * 设置统一项目banner
     *
     * @param applicationContext :
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (!(applicationContext instanceof AnnotationConfigApplicationContext)) {
            LogoBanner logoBanner = new LogoBanner(BannerInitializer.class, "/x3map/logo.txt", "Welcome to X3 ...", 5, 6, new Color[5], true);
            CustomBanner.show(logoBanner, new Description(BannerConstant.VERSION + ":", CommonCoreConstant.PROJECT_VERSION, 0, 1)
                    , new Description("html:", "https://www.xiaogj.com/index.html", 0, 1)
            );
        }
    }
}
