package pages.interfaces;

public interface LoginPage {

    void verifyLoginPageIsDisplayed();

    void clickRegisterButton();

    void validateEmailAddress(String expectedEmailAddress);

    void loginUser(String email, String password);

    void verifyErrorMessage(String expectedMessage);

}
