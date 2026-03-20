package services;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.util.logging.Logger;

public class AppiumServiceManager {

    private static final Logger logger = Logger.getLogger(AppiumServiceManager.class.getName());

    private static AppiumDriverLocalService service;

    public static void startService() {
        logger.info("Starting service...");
        if (service == null || !service.isRunning()) {
            service = new AppiumServiceBuilder()
                    .usingAnyFreePort()
                    .build();
            service.start();
        }
        logger.info("Started service!!");
    }

    public static void stopService() {
        logger.info("Stopping service...");
        if (service != null && service.isRunning()) {
            service.stop();
            service = null;
        }
        logger.info("Stopped service!!");
    }

    public static String getServiceUrl() {
        return service.getUrl()
                      .toString();
    }

}
