package services;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class AppiumServiceManager {

    private static AppiumDriverLocalService service;

    public static void startService() {
        if (service == null || !service.isRunning()) {
            service = new AppiumServiceBuilder()
                    .usingAnyFreePort()
                    .build();
            service.start();
        }
    }

    public static void stopService() {
        if (service != null && service.isRunning()) {
            service.stop();
            service = null;
        }
    }

    public static String getServiceUrl() {
        return service.getUrl()
                      .toString();
    }

}
