package pages.android.dashboard;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import models.Course;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.android.BasePageAndroid;
import pages.interfaces.dashboard.IUserDashboardPage;
import pages.web.dashboard.UserDashboardPageWeb;
import utils.ScreenshotUtils;

import java.util.logging.Logger;

public class UserDashboardPageAndroid extends BasePageAndroid implements IUserDashboardPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(UserDashboardPageWeb.class.getName());

    private final By viewAllCoursesBtn = By.xpath("//android.widget.Button[contains(@content-desc,'View All')]");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public UserDashboardPageAndroid(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void clickViewAllCourses() {
        logger.info("Clicking View All courses button");
        clickButton(viewAllCoursesBtn);
    }

    @Override
    public void validateEnrolledCourse(Course course) {
        logger.info("Finding enrolled course: " + course.getTitle());
        try {
            findCourse(course.getTitle());
            ScreenshotUtils.captureAndAttach(driver, "Enrolled Course");
        } catch (Exception ex) {
            logger.info("Course did not exist!");
            ScreenshotUtils.captureAndAttach(driver, "Enrolled Course does not exist!");
        }
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private WebElement findCourse(String courseTitle) {
        return driver.findElement(
                AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true))" +
                                ".scrollIntoView(new UiSelector().description(\"" + courseTitle + "\"))"
                )
        );
    }
    // </editor-fold>

}
