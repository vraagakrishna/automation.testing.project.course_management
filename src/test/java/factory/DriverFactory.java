package factory;

import common.Constants;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.JavascriptExecutor;
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

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            driverInstance.get(Constants.DEV_URL);

        return driverInstance;
    }

    public static void reInitDriver(AppiumDriver driver) {
        resetBrowserState(driver);

        if (ConfigManager.getExecutionType()
                         .equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB)) {
            driver.get(Constants.DEV_URL);
        }
    }

    public static AppiumDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(AppiumDriver driverInstance) {
        driver.set(driverInstance);
    }

    public static void cleanDriver() {
        AppiumDriver driverInstance = driver.get();
        if (driverInstance != null)
            resetBrowserState(driverInstance);
    }

    public static void quitDriver() {
        AppiumDriver driverInstance = driver.get();
        if (driverInstance != null) {
            driverInstance.quit();
            driver.remove();
        }
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    private static void resetBrowserState(AppiumDriver driver) {
        if (ConfigManager.getExecutionType()
                         .equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB)) {
            driver.manage()
                  .deleteAllCookies();

            // Clear local & session storage
            ((JavascriptExecutor) driver).executeScript(
                    "window.localStorage.clear(); window.sessionStorage.clear();"
            );
        }
    }
    // </editor-fold>

}
