package pages.web.dashboard;

import io.appium.java_client.AppiumDriver;
import models.Course;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.interfaces.dashboard.IUserDashboardPage;
import pages.web.BasePageWeb;
import utils.ScreenshotUtils;

import java.util.logging.Logger;

public class UserDashboardPageWeb extends BasePageWeb implements IUserDashboardPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(UserDashboardPageWeb.class.getName());

    private final By viewAllCoursesBtn = By.xpath("//button[contains(., 'View All')]");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public UserDashboardPageWeb(AppiumDriver driver) {
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
            WebElement courseCardElement = findCourse(course.getTitle());
            scrollIntoView(courseCardElement);
            ScreenshotUtils.captureAndAttach(driver, "Enrolled Course");
        } catch (Exception ex) {
            logger.info("Course did not exist!");
            ScreenshotUtils.captureAndAttach(driver, "Enrolled Course does not exist!");
        }
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private WebElement findCourse(String courseTitle) {
        return getElement(
                By.xpath("//div[contains(@class,'dashboard-grid')]" +
                        "//span[text()='" + courseTitle + "']" +
                        "/ancestor::div[2]")
        );
    }
    // </editor-fold>

}
