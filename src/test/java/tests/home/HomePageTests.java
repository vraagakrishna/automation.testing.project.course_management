package tests.home;

import org.testng.annotations.Test;
import tests.TestsBase;

public class HomePageTests extends TestsBase {

    @Test()
    public void verifyHomePageIsDisplayed() {
        this.homePage.verifyHomePageIsDisplayed();
    }

}
