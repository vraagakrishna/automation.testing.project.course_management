package pages.web.auth;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.interfaces.auth.ILoginPage;
import pages.web.BasePageWeb;
import utils.ReportManager;
import utils.ScreenshotUtils;

import java.util.logging.Logger;

public class LoginPageWeb extends BasePageWeb implements ILoginPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(LoginPageWeb.class.getName());

    private final By loginHeading = By.id("login-heading");

    private final By emailField = By.id("login-email");

    private final By passwordField = By.id("login-password");

    private final By loginButton = By.id("login-submit");

    private final By registerButton = By.id("signup-toggle");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public LoginPageWeb(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void verifyLoginPageIsDisplayed() {
        if (!isElementVisible(loginHeading))
            throw new TimeoutException("Login Heading not displayed");

        logger.info("Waiting for Login Page to be visible");
        String expectedHeading = "Login to Access Learning Materials";

        this.verifyIfTextDisplayed(loginHeading, expectedHeading);
    }

    @Override
    public void clickRegisterButton() {
        WebElement element = this.getElement(registerButton);
        element.click();
    }

    @Override
    public void loginUser(String email, String password) {
        logger.info("Login user User{email=" + email + ",password=" + password + "}");
        ReportManager.getTest()
                     .info("Login user User{email=" + email + ",password=" + password + "}");
        this.clearLoginForm();

        this.enterEmailAddress(email);
        this.enterPassword(password);

        this.clickLoginButton();
    }

    @Override
    public void validateEmailAddress(String expectedEmailAddress) {
        String heading = this.getEmailAddress();

        Assert.assertEquals(heading, expectedEmailAddress, "Email address does not match");
    }

    @Override
    public void verifyErrorMessage(String expectedMessage) {
        logger.info("Verifying Error Message is: " + expectedMessage);
        this.alertUtils.verifyIfAlertMessageIsCorrect(expectedMessage);
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private void clearLoginForm() {
        this.enterEmailAddress("");
        this.enterPassword("");
    }

    private void enterEmailAddress(Object emailAddress) {
        this.enterKeys(emailField, emailAddress);
    }

    private void enterPassword(Object password) {
        this.enterKeys(passwordField, password);
    }

    private void clickLoginButton() {
        this.clickButton(loginButton);
    }

    private String getEmailAddress() {
        WebElement element = this.getElement(emailField);
        return element.getDomProperty("value");
    }
    // </editor-fold>

}
