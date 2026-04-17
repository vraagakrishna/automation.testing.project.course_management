package pages.web.admin;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.interfaces.admin.IEnrollmentsManagementPage;
import pages.web.BasePageWeb;
import utils.ReportManager;

import java.util.List;
import java.util.logging.Logger;

public class EnrollmentsManagementPageWeb extends BasePageWeb implements IEnrollmentsManagementPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(EnrollmentsManagementPageWeb.class.getName());

    private final By emailField = By.xpath("//input[@placeholder=\"Search by email...\"]");

    private final By coursesDropdown = By.xpath("//select[option[contains(text(),'All Courses')]]");

    private final By completeBtn = By.xpath("//button[normalize-space()='Complete']");

    private final By enrollUserBtn = By.xpath("//button[contains(., \"Enroll User\")]");

    private final By enrollCourseDropdown = By.xpath("//select[option[contains(text(),'Select Course')]]");

    private final By enrollSearchField = By.xpath("//input[contains(@placeholder, 'Search by name or email...')]");

    private final By enrollSubmitBtn = By.xpath("//button[contains(., \"Enroll User\") and @type=\"submit\"]");

    private final By enrollCancelBtn = By.xpath("//button[text()='Cancel']");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public EnrollmentsManagementPageWeb(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void completeAllCourses(String userEmail) {
        searchEmail(userEmail);

        By rows = By.xpath(
                "//div[@class='admin-enrollments']//tr[" +
                        ".//td[normalize-space()='" + userEmail + "'] and " +
                        ".//button[normalize-space()='Complete']]"
        );

        List<WebElement> results = driver.findElements(rows);
        logger.info("Results found: " + (long) results.size());

        for (WebElement row : results) {
            String courseName = row.findElement(By.xpath("./td[3]"))
                                   .getText();
            logger.info("Completing course: " + courseName);

            WebElement rowCompleteBtn = row.findElement(completeBtn);
            scrollIntoView(rowCompleteBtn);
            clickButton(rowCompleteBtn);

            alertUtils.verifyIfConfirmationAlertMessageIsCorrect(
                    "Mark this course as completed?",
                    true
            );
        }
    }

    @Override
    public void clickEnroll(String courseName, String userEmail, String enrollmentNotes, boolean shouldWork) {
        logger.info("Enrolling user '" + userEmail + "' to course '" + courseName + "'");
        ReportManager.getTest()
                     .info("Enrolling user '" + userEmail + "' to course '" + courseName + "'");
        clickButton(enrollUserBtn);

        boolean courseExists = this.selectByVisibleText(enrollCourseDropdown, courseName);

        Assert.assertEquals(courseExists, shouldWork, "Course should exists but it does not exist!");

        if (!courseExists) {
            logger.info("Course does not exist; Nothing to do!");
            ReportManager.getTest()
                         .info("Course does not exist; Nothing to do!");
            scrollToViewThenClickButton(enrollCancelBtn);
            return;
        }

        enterKeys(enrollSearchField, userEmail);
        closeKeyboardIfOpen();

        By userResult = By.xpath("//div[starts-with(text(),'" + userEmail + "')]/parent::div");
        scrollToViewThenClickButton(userResult);

        scrollToViewThenClickButton(enrollSubmitBtn);

        logger.info("Enrolled user '" + userEmail + "' to course '" + courseName + "'");
        ReportManager.getTest()
                     .info("Enrolled user '" + userEmail + "' to course '" + courseName + "'");
    }

    @Override
    public void searchForEnrollment(String courseName, String userEmail, boolean shouldExist) {
        logger.info("Searching for enrollment: user '" + userEmail + "' to course '" + courseName + "'");
        ReportManager.getTest()
                     .info("Searching for enrollment: user " + userEmail + " to course " + courseName);
        searchEmail(userEmail);
        boolean courseFound = searchCourse(courseName);

        if (!courseFound) {
            logger.info("Course does not exist");
            ReportManager.getTest()
                         .info("Course does not exist");
            Assert.assertEquals(courseFound, shouldExist, "Enrollment should exists but it does not exist!");
            return;
        }

        if (verifyIfTextDisplayedAnywhere("No Enrollments Found")) {
            logger.info("Enrollment does not exist");
            ReportManager.getTest()
                         .info("Enrollment does not exist");
            Assert.assertTrue(shouldExist, "Enrollment should exist, but does not exist");
            return;
        }

        By rows = By.xpath(
                "//div[@class='admin-enrollments']//table//tr[" +
                        ".//td[contains(text(), '" + userEmail + "')] and " +
                        ".//td[contains(text(), '" + courseName + "')] and " +
                        ".//span[contains(text(), 'Enrolled')]]"
        );
        List<WebElement> results = driver.findElements(rows);
        long numberOfResults = results.size();
        logger.info("Results found: " + numberOfResults);
        ReportManager.getTest()
                     .info("Results found: " + numberOfResults);

        if (shouldExist)
            Assert.assertTrue(
                    numberOfResults > 0,
                    "Expected to find enrollment, but found none"
            );
        else
            Assert.assertEquals(
                    numberOfResults,
                    0,
                    "Expected no enrollment, but found one"
            );
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private void searchEmail(String email) {
        logger.info("Search for email: " + email);
        enterKeys(emailField, email);
        closeKeyboardIfOpen();
    }

    private boolean searchCourse(String courseName) {
        logger.info("Search for course: " + courseName);
        return this.selectByVisibleText(coursesDropdown, courseName);
    }
    // </editor-fold>

}
