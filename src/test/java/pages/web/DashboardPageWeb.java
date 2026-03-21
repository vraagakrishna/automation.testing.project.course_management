package pages.web;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import pages.BasePage;
import pages.interfaces.DashboardPage;

import java.util.logging.Logger;

public class DashboardPageWeb extends BasePage implements DashboardPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(DashboardPageWeb.class.getName());

    private final By welcomeHeading = By.xpath("//h2[contains(., 'Welcome back')]");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public DashboardPageWeb(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void verifyDashboardPageIsDisplayed() {
        try {
            getElement(welcomeHeading);
        } catch (TimeoutException ex) {
            throw new TimeoutException("Welcome Heading not displayed");
        }
    }
    // </editor-fold>

}
