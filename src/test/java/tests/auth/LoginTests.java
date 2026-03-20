package tests.auth;

import org.testng.annotations.Test;
import tests.TestsBase;
import utils.ConfigManager;
import utils.UserTestData;

public class LoginTests extends TestsBase {

    // <editor-fold desc="Overrides">
    @Override
    protected void setUpPage() {
        homePage.verifyHomePageIsDisplayed();

        navigationBar.goToLoginPage();

        loginPage.verifyLoginPageIsDisplayed();
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Test(description = "Submission of a blank login form", groups = "2. Login Tests")
    public void blankLoginFormSubmission() {
        loginPage.loginUser("", "");

        loginPage.verifyErrorMessage("Please enter both email and password");
    }

    @Test(description = "Login using invalid credentials", groups = "2. Login Tests", priority = 1)
    public void invalidCredentialsLogin() {
        loginPage.loginUser(UserTestData.getEmail(), UserTestData.getWeakPassword());

        loginPage.verifyErrorMessage("Invalid credentials. Please try again.");
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
