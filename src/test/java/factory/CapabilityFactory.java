package factory;

import common.Constants;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.MutableCapabilities;
import utils.ApkDownloader;
import utils.ConfigManager;

import java.io.File;

public class CapabilityFactory {

    public static MutableCapabilities getCapabilities() {
        String platform = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();
        String browserName = ConfigManager.getBrowserName();
        String appPath = ConfigManager.getAppPath();
        String appUrl = ConfigManager.getAppUrl();
        String automatorName = ConfigManager.getAutomatorName();

        if (platform.equalsIgnoreCase(Constants.PLATFORM_ANDROID)) {
            UiAutomator2Options options = new UiAutomator2Options()
                    .setAutomationName(automatorName)
                    .setPlatformName(platform);

            if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB)) {
                options.withBrowserName(browserName);
            } else if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_NATIVE_APP)) {
                ApkDownloader.downloadApk(appUrl, appPath);

                options.setApp(System.getProperty("user.dir") + File.separator + appPath);
            } else {
                throw new RuntimeException("Unsupported execution type for " + platform + ": " + executionType);
            }

            return options;
        } else if (platform.equalsIgnoreCase(Constants.PLATFORM_IOS)) {
            XCUITestOptions options = new XCUITestOptions()
                    .setAutomationName(automatorName)
                    .setPlatformName(platform);

            if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB)) {
                options.withBrowserName(browserName);
            } else if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_NATIVE_APP)) {
                throw new UnsupportedOperationException(platform + " is not supported yet for " + executionType);
            } else {
                throw new RuntimeException("Unsupported execution type for " + platform + ": " + executionType);
            }

            return options;
        }

        throw new RuntimeException("Unsupported platform: " + platform);
    }

}
