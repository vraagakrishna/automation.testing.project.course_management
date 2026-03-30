package pages.android;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.interfaces.IHomePage;

import java.util.logging.Logger;

public class HomePageAndroid extends BasePageAndroid implements IHomePage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(HomePageAndroid.class.getName());

    private final By homePageHeading = By.xpath("//android.view.View[@content-desc=\"Master Test Automation\"]");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public HomePageAndroid(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    public void verifyHomePageIsDisplayed() {
        logger.info("Waiting for Home Page to be visible...");

        // wait until the element is visible
        WebElement element = this.getElement(homePageHeading);

        Assert.assertNotNull(element, "Heading does not match");
    }
    // </editor-fold>

}
