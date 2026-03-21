package factory;

import common.Constants;
import io.appium.java_client.AppiumDriver;
import pages.android.dashboard.DashboardPageAndroid;
import pages.android.HomePageAndroid;
import pages.android.auth.LoginPageAndroid;
import pages.android.NavigationBarAndroid;
import pages.interfaces.*;
import pages.interfaces.admin.CourseManagementPage;
import pages.interfaces.auth.LoginPage;
import pages.interfaces.dashboard.AdminDashboardPage;
import pages.interfaces.dashboard.DashboardPage;
import pages.web.*;
import pages.web.admin.CourseManagementPageWeb;
import pages.web.auth.LoginPageWeb;
import pages.web.dashboard.AdminDashboardPageWeb;
import pages.web.dashboard.DashboardPageWeb;
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

    public static AdminDashboardPage getAdminDashboardPage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new AdminDashboardPageWeb(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

    public static CourseManagementPage getCourseManagementPage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new CourseManagementPageWeb(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

}
