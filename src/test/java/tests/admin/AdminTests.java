package tests.admin;

import factory.PageFactory;
import org.testng.annotations.Test;
import pages.interfaces.IHomePage;
import pages.interfaces.INavigationBar;
import pages.interfaces.admin.ICourseManagementPage;
import pages.interfaces.auth.ILoginPage;
import pages.interfaces.dashboard.IAdminDashboardPage;
import pages.interfaces.dashboard.IDashboardPage;
import tests.TestsBase;
import utils.ConfigManager;

public class AdminTests extends TestsBase {

    // <editor-fold desc="Class Fields / Constants">
    protected IHomePage homePage;

    protected INavigationBar navigationBar;

    protected ILoginPage loginPage;

    protected IDashboardPage dashboardPage;

    protected IAdminDashboardPage adminDashboardPage;

    protected ICourseManagementPage courseManagementPage;
    // </editor-fold>

    // <editor-fold desc="Overrides">
    @Override
    protected void setUpPage() {
        this.homePage = PageFactory.getHomePage(driver);
        this.navigationBar = PageFactory.getNavigationBar(driver);
        this.loginPage = PageFactory.getLoginPage(driver);
        this.dashboardPage = PageFactory.getDashboardPage(driver);
        this.adminDashboardPage = PageFactory.getAdminDashboardPage(driver);
        this.courseManagementPage = PageFactory.getCourseManagementPage(driver);

        homePage.verifyHomePageIsDisplayed();

        navigationBar.goToLoginPage();

        loginPage.verifyLoginPageIsDisplayed();

        loginAsAdminAndVerify(loginPage, dashboardPage);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Test(description = "Navigate to Admin Dashboard", groups = "3. Admin Tests")
    public void navigateToAdminDashboard() {
        navigationBar.goToAdminPanel();

        adminDashboardPage.verifyAdminDashboardIsDisplayed();

        navigationBar.clickBackToWebsiteBtn();

        dashboardPage.verifyDashboardPageIsDisplayed();
    }

    @Test(description = "Navigate to Manage Courses page from Quick Actions", groups = "3. Admin Tests", dependsOnMethods = "navigateToAdminDashboard", priority = 1)
    public void navigateToManageCoursePageFromQuickActions() {
        navigationBar.goToAdminPanel();

        adminDashboardPage.verifyAdminDashboardIsDisplayed();

        adminDashboardPage.navigateToManageCourses();

        courseManagementPage.verifyCourseManagementPageIsDisplayed();

        navigationBar.clickBackToWebsiteBtn();

        dashboardPage.verifyDashboardPageIsDisplayed();
    }

    @Test(description = "Navigate to Manage Courses page", groups = "3. Admin Tests", dependsOnMethods = "navigateToAdminDashboard", priority = 2)
    public void navigateToManageCoursePage() {
        navigationBar.goToAdminPanel();

        adminDashboardPage.verifyAdminDashboardIsDisplayed();

        navigationBar.clickCoursesBtn();

        courseManagementPage.verifyCourseManagementPageIsDisplayed();

        navigationBar.clickBackToWebsiteBtn();

        dashboardPage.verifyDashboardPageIsDisplayed();
    }
    // </editor-fold>

}
