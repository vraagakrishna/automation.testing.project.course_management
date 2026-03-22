package pages.web;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.BasePage;
import pages.interfaces.IHomePage;

import java.util.logging.Logger;

public class HomePageWeb extends BasePage implements IHomePage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(HomePageWeb.class.getName());

    private final By homePageTitle = By.id("overview-hero");
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
        WebElement element = this.getElement(homePageTitle);

        String heading = element.findElement(By.tagName("h1"))
                                .getText();
        logger.info(String.format("Heading found: %s", heading));

        Assert.assertEquals(heading, expectedHeading, "Heading does not match");
    }
    // </editor-fold>

}
