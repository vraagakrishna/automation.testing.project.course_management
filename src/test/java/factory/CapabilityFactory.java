package factory;

import common.Constants;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.MutableCapabilities;
import utils.ApkDownloader;
import utils.ConfigManager;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    .setPlatformName(platform)
                    .setAdbExecTimeout(Duration.ofSeconds(60))        // Give ADB 60s instead of 30s
                    .setAppWaitDuration(Duration.ofSeconds(60));      // Wait longer for Appium to hook into the app

            if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB)) {
                options.withBrowserName(browserName);

                // ONLY apply these if the browser is Chrome
                if (browserName.equalsIgnoreCase(Constants.BROWSER_NAME_CHROME)) {
                    options.setChromedriverArgs(List.of(
                            "--disable-blink-features=AutomationControlled",

                            // Add a real-looking User-Agent to hide the "Headless/Automation" signature
                            "--user-agent=Mozilla/5.0 (Linux; Android 13; SM-A505F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.6045.163 Mobile Safari/537.36",

                            "--disable-infobars",
                            "--no-sandbox",
                            "--disable-dev-shm-usage"
                    ));

                    Map<String, Object> chromeOptions = new HashMap<>();

                    // This prevents the "Chrome is being controlled by automated software" info bar
                    chromeOptions.put("excludeSwitches", List.of("enable-automation"));

                    // Incognito can help clear bad state from previous runs
                    chromeOptions.put("args", List.of(
                            "--disable-notifications",
                            "--disable-popup-blocking",

                            // Helps prevent Vercel from tracking session history
                            "--incognito"
                    ));

                    options.setCapability("appium:chromeOptions", chromeOptions);
                }
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
                    .setPlatformName(platform)
                    .setWdaLaunchTimeout(Duration.ofMinutes(10))    // Wait up to 10 mins for the build
                    .setWdaConnectionTimeout(Duration.ofMinutes(5)) // Wait for the server to respond
                    .setUseNewWDA(false);                           // Don't rebuild if already built

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
