package pages.android.auth;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.android.BasePageAndroid;
import pages.interfaces.auth.ILoginPage;
import pages.web.auth.LoginPageWeb;
import utils.ReportManager;

import java.util.logging.Logger;

public class LoginPageAndroid extends BasePageAndroid implements ILoginPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(LoginPageWeb.class.getName());

    private final By loginHeading = By.xpath(
            "//android.view.View[@content-desc=\"Login to Access Learning Materials\"]");

    private final By emailField = By.xpath("(//android.widget.EditText)[1]");

    private final By passwordField = By.xpath("(//android.widget.EditText)[2]");

    private final By loginButton = By.xpath("//android.widget.Button[@content-desc=\"Login\"]");

    private final By registerButton = By.xpath("//android.view.View[@content-desc=\"Sign Up Here\"]");

    private final By errorPanel = By.xpath(
            "//android.view.View[@focusable='true' and @clickable='false' and @content-desc][2]");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public LoginPageAndroid(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void verifyLoginPageIsDisplayed() {
        getElementOrThrow(loginHeading, "Login Heading");
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

        this.closeKeyboardIfOpen();
        this.clickLoginButton();
    }

    @Override
    public void validateEmailAddress(String expectedEmailAddress) {
        String heading = this.getEmailAddress();

        Assert.assertEquals(heading, expectedEmailAddress, "Email address does not match");
    }

    @Override
    public void verifyErrorMessage(String expectedMessage) {
        logger.info("Verifying error message");
        WebElement element = this.getElement(errorPanel);

        String actualMessage = element.getAttribute("content-desc");
        logger.info("Actual Message: " + actualMessage);

        Assert.assertTrue(
                actualMessage.contains(expectedMessage),
                "Expected message: " + expectedMessage + ", but actual message: " + actualMessage
        );
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
