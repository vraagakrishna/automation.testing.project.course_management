package pages.android.admin;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.android.BasePageAndroid;
import pages.interfaces.admin.IEnrollmentsManagementPage;
import utils.ReportManager;
import utils.SoftAssertManager;

import java.util.List;
import java.util.logging.Logger;

public class EnrollmentsManagementPageAndroid extends BasePageAndroid implements IEnrollmentsManagementPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(EnrollmentsManagementPageAndroid.class.getName());

    private final By emailField = AppiumBy.xpath("//android.widget.EditText[@hint='Search by email...']");

    private final By completeBtn = AppiumBy.xpath(".//android.view.View[@content-desc='Complete']");

    private final By completeEnrollmentCompleteBtn = AppiumBy.xpath(
            "//android.widget.Button[@content-desc='Complete']");

    private final By enrollUserBtn = By.xpath("//android.widget.Button[@content-desc='Enroll User']");

    private final By enrollSearchField = By.xpath(
            "//android.widget.EditText[contains(@hint, 'Search by name or email')]");

    private final By enrollSubmitBtn = By.xpath("//android.widget.Button[@content-desc=\"Enroll User\"]");

    private final By enrollCancelBtn = By.xpath("//android.widget.Button[@content-desc='Cancel']");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public EnrollmentsManagementPageAndroid(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void completeAllCourses(String userEmail) {
        searchEmail(userEmail);

        By rows = By.xpath(
                "//android.view.View[contains(@content-desc,'" + userEmail + "')]" +
                        "[.//android.view.View[@content-desc='Complete']]"
        );

        List<WebElement> results = driver.findElements(rows);
        logger.info("Results found: " + (long) results.size());

        for (WebElement row : results) {
            String rowData = row.getAttribute("content-desc");

            String[] lines = rowData.split("\\n");
            String courseName = lines[3];   // course name line

            logger.info("Completing course: " + courseName);

            WebElement rowCompleteBtn = row.findElement(completeBtn);
            rowCompleteBtn.click();

            clickButton(completeEnrollmentCompleteBtn);
        }
    }

    @Override
    public void clickEnroll(String courseName, String userEmail, String enrollmentNotes, boolean shouldWork) {
        logger.info("Enrolling user '" + userEmail + "' to course '" + courseName + "'");
        ReportManager.getTest()
                     .info("Enrolling user '" + userEmail + "' to course '" + courseName + "'");
        clickButton(enrollUserBtn);

        boolean courseExists = this.setDropdownValue("-- Select Course --", courseName);

        String errorMsg = shouldWork ?
                "Course should exist but it does not exist!" :
                "Course should not exist but it does exist!";
        SoftAssertManager.getSoftAssert()
                         .assertEquals(
                                 courseExists,
                                 shouldWork,
                                 errorMsg
                         );

        if (!courseExists) {
            logger.info("Course does not exist; Nothing to do!");
            ReportManager.getTest()
                         .info("Course does not exist; Nothing to do!");
            clickButton(enrollCancelBtn);
            return;
        }

        enterKeys(enrollSearchField, userEmail);

        By userResult = By.xpath("//android.widget.Button[contains(@content-desc, \"" + userEmail + "\")]");
        clickButton(userResult);

        closeKeyboardIfOpen();
        clickButton(enrollSubmitBtn);

        logger.info("Enrolled user '" + userEmail + "' to course '" + courseName + "'");
        ReportManager.getTest()
                     .info("Enrolled user '" + userEmail + "' to course '" + courseName + "'");
    }

    @Override
    public void searchForEnrollment(String courseName, String userEmail, boolean shouldExist) {
        logger.info("Searching for enrollment: user '" + userEmail + "' to course '" + courseName + "'");
        ReportManager.getTest()
                     .info("Searching for enrollment: user '" + userEmail + "' to course '" + courseName + "'");
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
                "//android.view.View[contains(@content-desc, '" + userEmail + "')]" +
                        "[contains(@content-desc, '" + courseName + "')]" +
                        "[contains(@content-desc, 'ENROLLED')]"
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
        return this.setDropdownValue("All Courses", courseName);
    }
    // </editor-fold>

}
