package pages.android.dashboard;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pages.android.BasePageAndroid;
import pages.interfaces.dashboard.IDashboardPage;
import utils.SoftAssertManager;

import java.util.logging.Logger;

public class DashboardPageAndroid extends BasePageAndroid implements IDashboardPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(DashboardPageAndroid.class.getName());

    private final By welcomeHeading = By.xpath(
            "//android.view.View[contains(@content-desc,'overview') or contains(@content-desc,'Good evening')]");

    private boolean firstLogin = true;
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public DashboardPageAndroid(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void verifyDashboardPageIsDisplayed() {
        logger.info("firstLogin = " + firstLogin);
        if (firstLogin) {
            String actualSnackBarText = getSnackBarText();
            String expectedSnackBarText = "Welcome";
            logger.info("Snack bar text: " + actualSnackBarText);
            SoftAssertManager.getSoftAssert()
                             .assertTrue(
                                     actualSnackBarText.contains(expectedSnackBarText),
                                     "Expected message: " + expectedSnackBarText + ", but actual message: " + actualSnackBarText
                             );
        }

        logger.info("Verifying Dashboard page is displayed");
        getElementOrThrow(welcomeHeading, "Welcome Heading");

        firstLogin = false;
    }
    // </editor-fold>

}
