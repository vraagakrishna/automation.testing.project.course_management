package utils;

import common.Constants;

import java.io.File;

public class ConfigManager {

    public static String getAppiumServerUrl() {
        return System.getProperty("APPIUM_SERVER_URL", System.getenv("APPIUM_SERVER_URL"));
    }

    public static String getPlatformName() {
        return System.getProperty("PLATFORM_NAME", Constants.PLATFORM_ANDROID);
    }

    public static String getExecutionType() {
        return System.getProperty("EXECUTION_TYPE", Constants.EXECUTION_TYPE_MOBILE_WEB);
    }

    public static String getBrowserName() {
        return System.getProperty("BROWSER_NAME", "chrome");
    }

    public static String getAppPath() {
        return System.getProperty(
                "APP_PATH", "target" + File.separator + "app" + File.separator + "app-qa-release.apk");
    }

    public static String getAppUrl() {
        return System.getProperty("APP_URL", Constants.DEV_APK_URL);
    }

    public static String getAutomatorName() {
        return System.getProperty("AUTOMATOR_NAME", "UiAutomator2");
    }

    public static String getAdminEmail() {
        return System.getProperty("ADMIN_EMAIL", System.getenv("ADMIN_EMAIL"));
    }

    public static String getAdminPassword() {
        return System.getProperty("ADMIN_PASSWORD", System.getenv("ADMIN_PASSWORD"));
    }

    public static String getUserEmail() {
        return System.getProperty("USER_EMAIL", System.getenv("USER_EMAIL"));
    }

}
