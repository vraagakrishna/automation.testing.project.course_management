package pages.web;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.testng.Assert;
import pages.interfaces.IHomePage;

import java.util.logging.Logger;

public class HomePageWeb extends BasePageWeb implements IHomePage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(HomePageWeb.class.getName());

    private final By homePageTitle = By.xpath("//div[@id='overview-hero']//h1");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public HomePageWeb(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    public void verifyHomePageIsDisplayed() {
        logger.info("Waiting for Home Page to be visible...");
        String expectedHeading = "Master Test Automation";

        // wait until the element is visible
        String heading = this.getElement(homePageTitle, 50)
                             .getText();
        logger.info(String.format("Heading found: %s", heading));

        Assert.assertEquals(heading, expectedHeading, "Heading does not match");
    }
    // </editor-fold>

}
