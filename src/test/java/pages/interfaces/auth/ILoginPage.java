package pages.interfaces.auth;

public interface ILoginPage {

    void verifyLoginPageIsDisplayed();

    void clickRegisterButton();

    void validateEmailAddress(String expectedEmailAddress);

    void loginUser(String email, String password);

    void verifyErrorMessage(String expectedMessage);

}
