package com.roc.cloud.aliyunlog;

/**
 * @description:
 * @date: 2020/9/08 9:26
 * @author: Roc
 * @version: 1.0
 */
public class LogEnhancerBinder {

    private volatile static LogEnhancer INSTANCE;

    private static ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    /**
     * bindClassLoader
     *
     * @param classLoader :
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    public static void bindClassLoader(ClassLoader classLoader) {

        if (INSTANCE == null) {

            synchronized (LogEnhancerBinder.class) {

                if (INSTANCE == null) {
                    LogEnhancerBinder.classLoader = classLoader;
                }
            }
        }
    }

    /**
     * getInstance
     *
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public static LogEnhancer getInstance() {

        if (INSTANCE == null) {

            synchronized (LogEnhancerBinder.class) {

                if (INSTANCE == null) {

                    INSTANCE = doInit(classLoader);
                }

            }
        }

        return INSTANCE;
    }

    /**
     * doInit
     *
     * @param classLoader :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    private static LogEnhancer doInit(ClassLoader classLoader) {

        LogEnhancer enhancer = null;
        if (LogEnvUtils.isLog4j2Usable(classLoader)) {
            enhancer = new DefaultLog4j2Enhancer(classLoader);
        } else if (LogEnvUtils.isLogbackUsable(classLoader)) {
            enhancer = new DefaultLogbackEnhancer(classLoader);
        } else {
            throw new IllegalStateException("No applicable logging system found");
        }

        return enhancer;
    }

}
