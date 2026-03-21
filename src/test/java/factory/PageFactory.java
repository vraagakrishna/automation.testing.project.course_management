package factory;

import common.Constants;
import io.appium.java_client.AppiumDriver;
import pages.android.DashboardPageAndroid;
import pages.android.HomePageAndroid;
import pages.android.LoginPageAndroid;
import pages.android.NavigationBarAndroid;
import pages.interfaces.DashboardPage;
import pages.interfaces.HomePage;
import pages.interfaces.LoginPage;
import pages.interfaces.NavigationBar;
import pages.web.DashboardPageWeb;
import pages.web.HomePageWeb;
import pages.web.LoginPageWeb;
import pages.web.NavigationBarWeb;
import utils.ConfigManager;

public class PageFactory {

    public static HomePage getHomePage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new HomePageWeb(driver);

        if (platformName.equalsIgnoreCase(Constants.PLATFORM_ANDROID))
            return new HomePageAndroid(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

    public static NavigationBar getNavigationBar(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new NavigationBarWeb(driver);

        if (platformName.equalsIgnoreCase(Constants.PLATFORM_ANDROID))
            return new NavigationBarAndroid(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

    public static LoginPage getLoginPage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new LoginPageWeb(driver);

        if (platformName.equalsIgnoreCase(Constants.PLATFORM_ANDROID))
            return new LoginPageAndroid(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

    public static DashboardPage getDashboardPage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new DashboardPageWeb(driver);

        if (platformName.equalsIgnoreCase(Constants.PLATFORM_ANDROID))
            return new DashboardPageAndroid(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

}
