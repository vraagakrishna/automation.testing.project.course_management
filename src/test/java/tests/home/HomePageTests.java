package tests.home;

import factory.PageFactory;
import org.testng.annotations.Test;
import pages.interfaces.IHomePage;
import tests.TestsBase;

public class HomePageTests extends TestsBase {

    // <editor-fold desc="Class Fields / Constants">
    protected IHomePage homePage;
    // </editor-fold>

    // <editor-fold desc="Overrides">
    @Override
    protected void setUpPage() {
        this.homePage = PageFactory.getHomePage(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Test(description = "Verify that Home Page is displayed", groups = "1. Home Page")
    public void verifyHomePageIsDisplayed() {
        this.homePage.verifyHomePageIsDisplayed();
    }
    // </editor-fold>

}
