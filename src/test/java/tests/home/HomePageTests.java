package tests.home;

import org.testng.annotations.Test;
import tests.TestsBase;

public class HomePageTests extends TestsBase {

    @Test(description = "Verify that Home Page is displayed", groups = "1. Home Page")
    public void verifyHomePageIsDisplayed() {
        this.homePage.verifyHomePageIsDisplayed();
    }

}
