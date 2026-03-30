package tests.auth;

import factory.PageFactory;
import org.testng.annotations.Test;
import pages.interfaces.IHomePage;
import pages.interfaces.INavigationBar;
import pages.interfaces.auth.ILoginPage;
import pages.interfaces.dashboard.IDashboardPage;
import tests.TestsBase;
import utils.ConfigManager;
import utils.UserTestData;

public class LoginTests extends TestsBase {

    // <editor-fold desc="Class Fields / Constants">
    protected IHomePage homePage;

    protected INavigationBar navigationBar;

    protected ILoginPage loginPage;

    protected IDashboardPage dashboardPage;
    // </editor-fold>

    // <editor-fold desc="Overrides">
    @Override
    protected void setUpPage() {
        this.homePage = PageFactory.getHomePage(driver);
        this.navigationBar = PageFactory.getNavigationBar(driver);
        this.loginPage = PageFactory.getLoginPage(driver);
        this.dashboardPage = PageFactory.getDashboardPage(driver);

        homePage.verifyHomePageIsDisplayed();

        navigationBar.goToLoginPage();

        loginPage.verifyLoginPageIsDisplayed();
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Test(description = "Submission of a blank login form", groups = "2. Login Tests")
    public void blankLoginFormSubmission() {
        loginPage.loginUser("", "");

        loginPage.verifyErrorMessage("email and password");
    }

    @Test(description = "Login using invalid credentials", groups = "2. Login Tests", priority = 1)
    public void invalidCredentialsLogin() {
        loginPage.loginUser(UserTestData.getEmail(), UserTestData.getWeakPassword());

        loginPage.verifyErrorMessage("Invalid");
    }

    @Test(description = "Login with valid credentials", groups = "2. Login Tests", priority = 2)
    public void validCredentialsLogin() {
        loginPage.loginUser(ConfigManager.getAdminEmail(), ConfigManager.getAdminPassword());

        dashboardPage.verifyDashboardPageIsDisplayed();

        navigationBar.logout();

        homePage.verifyHomePageIsDisplayed();
    }
    // </editor-fold>

}
