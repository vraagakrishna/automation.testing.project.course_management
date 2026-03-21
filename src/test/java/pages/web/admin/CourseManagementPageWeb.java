package pages.web.admin;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import pages.BasePage;
import pages.interfaces.admin.CourseManagementPage;

import java.util.logging.Logger;

public class CourseManagementPageWeb extends BasePage implements CourseManagementPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(CourseManagementPageWeb.class.getName());

    private final By courseManagementHeading = By.xpath("//h1[contains(., 'Course Management')]");

    private final By addCourseBtn = By.xpath(
            "//div[./button[contains(., 'Add Course')]]/button[contains(., 'Add Course')]");

    private final By clickCancelCourseBtn = By.xpath(
            "//div[./button[contains(., 'Cancel')]]/button[contains(., 'Cancel')]");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public CourseManagementPageWeb(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void verifyCourseManagementPageIsDisplayed() {
        logger.info("Verifying Course Management is displayed...");
        try {
            getElement(courseManagementHeading);
        } catch (TimeoutException ex) {
            throw new TimeoutException("Course Management not displayed");
        }
    }

    @Override
    public void clickAddCourseBtn() {
        clickButton(addCourseBtn);
    }

    @Override
    public void clickCancelCourseBtn() {
        clickButton(clickCancelCourseBtn);
    }
    // </editor-fold>

}
