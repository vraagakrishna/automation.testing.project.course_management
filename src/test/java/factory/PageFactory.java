package factory;

import common.Constants;
import io.appium.java_client.AppiumDriver;
import pages.android.HomePageAndroid;
import pages.android.NavigationBarAndroid;
import pages.android.admin.CourseManagementPageAndroid;
import pages.android.admin.EnrollmentsManagementPageAndroid;
import pages.android.auth.LoginPageAndroid;
import pages.android.dashboard.AdminDashboardPageAndroid;
import pages.android.dashboard.DashboardPageAndroid;
import pages.interfaces.IHomePage;
import pages.interfaces.INavigationBar;
import pages.interfaces.admin.ICourseManagementPage;
import pages.interfaces.admin.IEnrollmentsManagementPage;
import pages.interfaces.auth.ILoginPage;
import pages.interfaces.dashboard.IAdminDashboardPage;
import pages.interfaces.dashboard.IDashboardPage;
import pages.interfaces.dashboard.IUserDashboardPage;
import pages.interfaces.user.ICoursePage;
import pages.web.HomePageWeb;
import pages.web.NavigationBarWeb;
import pages.web.admin.CourseManagementPageWeb;
import pages.web.admin.EnrollmentsManagementPageWeb;
import pages.web.auth.LoginPageWeb;
import pages.web.dashboard.AdminDashboardPageWeb;
import pages.web.dashboard.DashboardPageWeb;
import pages.web.dashboard.UserDashboardPageWeb;
import pages.web.user.CoursePageWeb;
import utils.ConfigManager;

public class PageFactory {

    public static IHomePage getHomePage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new HomePageWeb(driver);

        if (platformName.equalsIgnoreCase(Constants.PLATFORM_ANDROID))
            return new HomePageAndroid(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

    public static INavigationBar getNavigationBar(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new NavigationBarWeb(driver);

        if (platformName.equalsIgnoreCase(Constants.PLATFORM_ANDROID))
            return new NavigationBarAndroid(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

    public static ILoginPage getLoginPage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new LoginPageWeb(driver);

        if (platformName.equalsIgnoreCase(Constants.PLATFORM_ANDROID))
            return new LoginPageAndroid(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

    public static IDashboardPage getDashboardPage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new DashboardPageWeb(driver);

        if (platformName.equalsIgnoreCase(Constants.PLATFORM_ANDROID))
            return new DashboardPageAndroid(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

    public static IAdminDashboardPage getAdminDashboardPage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new AdminDashboardPageWeb(driver);

        if (platformName.equalsIgnoreCase(Constants.PLATFORM_ANDROID))
            return new AdminDashboardPageAndroid(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

    public static ICourseManagementPage getCourseManagementPage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new CourseManagementPageWeb(driver);

        if (platformName.equalsIgnoreCase(Constants.PLATFORM_ANDROID))
            return new CourseManagementPageAndroid(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

    public static IEnrollmentsManagementPage getEnrollmentsManagementPage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new EnrollmentsManagementPageWeb(driver);

        if (platformName.equalsIgnoreCase(Constants.PLATFORM_ANDROID))
            return new EnrollmentsManagementPageAndroid(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

    public static IUserDashboardPage getUserDashboardPage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new UserDashboardPageWeb(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

    public static ICoursePage getCoursePage(AppiumDriver driver) {
        String platformName = ConfigManager.getPlatformName();
        String executionType = ConfigManager.getExecutionType();

        if (executionType.equalsIgnoreCase(Constants.EXECUTION_TYPE_MOBILE_WEB))
            return new CoursePageWeb(driver);

        throw new RuntimeException("Unsupported platform: " + platformName);
    }

}
