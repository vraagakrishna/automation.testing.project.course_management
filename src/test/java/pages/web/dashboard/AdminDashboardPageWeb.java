package pages.web.dashboard;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import pages.BasePage;
import pages.interfaces.dashboard.IAdminDashboardPage;

import java.util.logging.Logger;

public class AdminDashboardPageWeb extends BasePage implements IAdminDashboardPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(AdminDashboardPageWeb.class.getName());

    private final By adminDashboardHeading = By.xpath("//h1[contains(text(), 'Admin Dashboard')]");

    private final By manageCoursesButton = By.xpath(
            "//div[@class='quick-actions']/button[contains(., 'Manage Courses')]");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public AdminDashboardPageWeb(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void verifyAdminDashboardIsDisplayed() {
        logger.info("Verifying Admin Dashboard is displayed...");
        try {
            getElement(adminDashboardHeading);
        } catch (TimeoutException ex) {
            throw new TimeoutException("Admin Dashboard not displayed");
        }
    }

    @Override
    public void navigateToManageCourses() {
        clickButton(manageCoursesButton);
    }
    // </editor-fold>

}
