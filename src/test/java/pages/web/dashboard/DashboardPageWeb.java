package pages.web.dashboard;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import pages.BasePage;
import pages.interfaces.dashboard.IDashboardPage;

import java.util.logging.Logger;

public class DashboardPageWeb extends BasePage implements IDashboardPage {

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
        logger.info("Verify Dashboard Page is displayed");
        try {
            getElement(welcomeHeading);
        } catch (TimeoutException ex) {
            throw new TimeoutException("Welcome Heading not displayed");
        }
    }
    // </editor-fold>

}
