package com.roc.cloud.common.constant;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局公共常量
 *
 * @author: Roc
 * @date 2018/10/29
 */
public class CommonConstant {
    /**
     * 项目版本号(banner使用)
     */
    public final static String PROJECT_VERSION = "1.0.0";

    /**
     * token请求头名称
     */
    public final static String TOKEN_HEADER = "Authorization";

    /**
     * The access token issued by the authorization server. This value is REQUIRED.
     */
    public final static String ACCESS_TOKEN = "access_token";

    public final static String BEARER_TYPE = "Bearer";

    /**
     * 标签 header key
     */
    public final static String HEADER_LABEL = "x-label";

    /**
     * 标签 header 分隔符
     */
    public final static String HEADER_LABEL_SPLIT = ",";

    /**
     * 标签或 名称
     */
    public final static String LABEL_OR = "labelOr";

    /**
     * 标签且 名称
     */
    public final static String LABEL_AND = "labelAnd";

    /**
     * 权重key
     */
    public final static String WEIGHT_KEY = "weight";

    /**
     * 删除
     */
    public final static String STATUS_DEL = "1";

    /**
     * 正常
     */
    public final static String STATUS_NORMAL = "0";

    /**
     * 锁定
     */
    public final static String STATUS_LOCK = "9";

    /**
     * 目录
     */
    public final static Integer CATALOG = -1;

    /**
     * 菜单
     */
    public final static Integer MENU = 1;

    /**
     * 权限
     */
    public final static Integer PERMISSION = 2;

    /**
     * 删除标记
     */
    public final static String DEL_FLAG = "is_del";

    /**
     * 超级管理员用户名
     */
    public final static String ADMIN_USER_NAME = "admin";

    /**
     * 公共日期格式
     */
    public final static String MONTH_FORMAT = "yyyy-MM";
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String SIMPLE_MONTH_FORMAT = "yyyyMM";
    public final static String SIMPLE_DATE_FORMAT = "yyyyMMdd";
    public final static String SIMPLE_DATETIME_FORMAT = "yyyyMMddHHmmss";

    public final static String DEF_USER_PASSWORD = "123456";

    public final static String LOCK_KEY_PREFIX = "LOCK_KEY:";

    /**
     * 租户id参数
     */
    public final static String TENANT_ID_PARAM = "tenantId";


    /**
     * 日志链路追踪id信息头
     */
    public final static String TRACE_ID_HEADER = "x-traceId-header";

    /**
     * 日志链路追踪id日志标志
     */
    public final static String LOG_TRACE_ID = "traceId";

    /**
     * 日志链路追踪id日志标志-机构
     */
    public final static String LOG_COMPANY_ID = "companyId";

    /**
     * 日志链路追踪id日志标志-请求地址
     */
    public final static String LOG_REQUEST_URL = "requestUrl";
    /**
     * 负载均衡策略-版本号 信息头
     */
//    String Z_L_T_VERSION = "z-l-t-version";
    /**
     * 注册中心元数据 版本号
     */
    public final static String METADATA_VERSION = "version";


    /**
     * 请求IP
     */
//    public static final String ORIGINAL_FORWARDED_FOR = "X-original-Forwarded-For";

    /**
     * 入口IP
     */
    public static final String ORIGINAL_FORWARDED_HOST_FOR = "X-original-Forwarded-Host-For";

    /**
     * 灰度环境
     */
    public static final List<String> PROFILES = Arrays.asList("dev");


    public static void main(String[] args) throws IOException {
        String basePath = "D:\\code\\zt";

        File dir = new File(basePath);
        for (File file : dir.listFiles()) {
//            make(file);

			un(file);
        }
    }

    public static void un(File sourceFile) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader read = new InputStreamReader(Files.newInputStream(sourceFile.toPath()), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(read);
        String line;
        File saveFile = null;
        while ((line = reader.readLine()) != null) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
//            int indexOf = line.indexOf("-----------------------------------我是文件名");
            int indexOf = line.indexOf("EFC5EC9F3E833A32AEE9FAEF4A50DDD3201096B55CF2F36E01E25EE1B25E7E0F612FFA666C212D0D2EF60F95479A9316A938D77BF9597755D59DC4CF95B62269");
//            int indexOf2 = line.indexOf("D:\\code\\");
            if (indexOf > 0) {
                if (StringUtils.isNotBlank(stringBuilder.toString())) {
                    BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile, true), "UTF-8"));
                    fw.append(stringBuilder.toString());
                    fw.newLine();
                    fw.flush(); // 全部写入缓存中的内容
                }

                String newFilePath = AesUtils.decryptAES("552A9A6820A352B739ED1D670BF0E432",line.substring(0, indexOf));
                saveFile = new File(newFilePath);
                String dir = newFilePath.substring(0, newFilePath.lastIndexOf("\\"));
                File folder = new File(dir);
                if (!folder.exists() && !folder.isDirectory()) {
                    folder.mkdirs();
                    System.out.println("创建件夹：" + dir);
                }
                if (!saveFile.exists()) {
                    saveFile.createNewFile();
                    System.out.println("创建文件：" + newFilePath);
                }
                stringBuilder = new StringBuilder();
            } else {
                stringBuilder.append(AesUtils.decryptAES("552A9A6820A352B739ED1D670BF0E432", line)).append("\n");
            }
        }
        read.close();
    }


    //1
    //C.Users.zhangling.IdeaProjects.file
    //2
    //C.Users.zhangling.IdeaProjects.live
    //3
    //C.Users.zhangling.IdeaProjects.message
    //4
    //C.Users.zhangling.IdeaProjects.OpenWeixinServer
    //5
    //C.Users.zhangling.IdeaProjects.OpenWeixinServer1
    //6
    //C.Users.zhangling.IdeaProjects.pay
    //7
    //C.Users.zhangling.IdeaProjects.testzl
    //8
    //C.Users.zhangling.IdeaProjects.untitled
    //9
    //C.Users.zhangling.IdeaProjects.workwx
    //10
    //C.Users.zhangling.IdeaProjects.XGJ.Pay
    //11
    //C.Users.zhangling.IdeaProjects.xiaogj-authorization
    //12
    //C.Users.zhangling.IdeaProjects.xiaogj-live-server1
    //13
    //C.Users.zhangling.IdeaProjects.yunke
    //14
    //C.Users.zhangling.IdeaProjects.yunpan-server
    //15
    //C.Users.zhangling.IdeaProjects.zt
    public static void make(File file) throws IOException {
        if (file.isDirectory()) {
            String lastFile = file.getPath().substring(file.getPath().lastIndexOf("\\") + 1);
            if (lastFile.substring(0, 1).equals(".") || lastFile.equals("target")) {
                return;
            }

            StringBuilder stringBuilder = new StringBuilder();
            getAllFile(file, stringBuilder);
            if (StringUtils.isBlank(stringBuilder.toString())) {
                return;
            }
            String path = file.getPath().replace(":", "").replace("\\", ".");
            File saveFile = new File("C:\\Users\\zhangling\\test\\" + path + ".jpg");
            BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile, true), "UTF-8"));
            fw.append(stringBuilder.toString());
            fw.newLine();
            fw.flush(); // 全部写入缓存中的内容

            System.out.println("完成文件：" + file.getPath());
        }
    }

    public static void getAllFile(File fileInput, StringBuilder stringBuilder) throws IOException {
        String lastFile = fileInput.getPath().substring(fileInput.getPath().lastIndexOf("\\") + 1);
        if (lastFile.substring(0, 1).equals(".") || lastFile.equals("target") || lastFile.equals(".git")) {
            return;
        }
        // 获取文件列表
        File[] fileList = fileInput.listFiles();
        assert fileList != null;
        String key = "552A9A6820A352B739ED1D670BF0E432";
        String biaoshi = AesUtils.encryptAES(key, "-----------------------------------我是文件名");
        for (File file : fileList) {
            if (file.isDirectory()) {
                // 递归处理文件夹
                // 如果不想统计子文件夹则可以将下一行注释掉
                getAllFile(file, stringBuilder);
            } else {
                List<String> jepEx = Arrays.asList("png", "gif", "tif", "jpg", "woff", "woff2", "otf", "eot", "ttf", "ico", "eot", "dll", "jar");
                String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
                if (jepEx.contains(extension)) {
                    stringBuilder.append("\n\n\n\n");
                    stringBuilder.append(AesUtils.encryptAES(key, file.getPath())).append(biaoshi + "\n");
                    continue;
                }

                stringBuilder.append("\n\n\n\n");
                stringBuilder.append(AesUtils.encryptAES(key, file.getPath())).append(biaoshi + "\n");
                InputStreamReader read = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    if (StringUtils.isEmpty(line)) {
                        stringBuilder.append(line).append("\n");
                    } else {
                        stringBuilder.append(AesUtils.encryptAES(key, line)).append("\n");
                    }
                }
                read.close();
            }
        }
    }
}
