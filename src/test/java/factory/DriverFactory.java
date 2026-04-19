package factory;

import common.Constants;
import io.appium.java_client.AppiumDriver;
import services.AppiumServiceManager;
import utils.ConfigManager;

import java.net.MalformedURLException;
import java.net.URI;

public class DriverFactory {

    // <editor-fold desc="Class Fields / Constants">
    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    public static AppiumDriver initDriver() throws MalformedURLException {
        String appiumUrl = AppiumServiceManager.startServiceIfRequired();

        AppiumDriver driverInstance = new AppiumDriver(
                URI.create(appiumUrl)
                   .toURL(),
                CapabilityFactory.getCapabilities()
        );

        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB)) {
            // Wait a random amount of time (5-10s) before navigating.
            // This prevents the "rhythmic" bot detection signature on the 10th run.
            long jitter = 5000 + (long) (Math.random() * 5000);
            try {
                Thread.sleep(jitter);
            } catch (InterruptedException ignored) {
            }

            // Navigate to Google first to establish a "clean" session history
            driverInstance.get("https://www.google.com");

            driverInstance.get(Constants.DEV_URL);
        }

        return driverInstance;
    }

    public static AppiumDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(AppiumDriver driverInstance) {
        driver.set(driverInstance);
    }

    public static void quitDriver() {
        AppiumDriver driverInstance = driver.get();
        if (driverInstance != null) {

            if (ConfigManager.getExecutionType()
                             .equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB)) {
                driverInstance.manage()
                              .deleteAllCookies();
            }

            driverInstance.quit();
            driver.remove();
        }
    }
    // </editor-fold>

}
