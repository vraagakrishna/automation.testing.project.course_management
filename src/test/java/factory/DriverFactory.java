package factory;

import common.Constants;
import io.appium.java_client.AppiumDriver;
import services.AppiumServiceManager;
import utils.ConfigManager;

import java.net.MalformedURLException;
import java.net.URI;

public class DriverFactory {

    // <editor-fold desc="Class Fields / Constants">
    private static AppiumDriver driver;
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    public static void initDriver() throws MalformedURLException {
        if (driver != null) return;

        AppiumServiceManager.startService();
        String appiumUrl = AppiumServiceManager.getServiceUrl();

        driver = new AppiumDriver(URI.create(appiumUrl)
                                     .toURL(), CapabilityFactory.getCapabilities());

        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB)) {
            driver.get(Constants.DEV_URL);
        }
    }

    public static AppiumDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            AppiumServiceManager.stopService();
            driver = null;
        }
    }
    // </editor-fold>

}
