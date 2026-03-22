package pages.web.admin;

import io.appium.java_client.AppiumDriver;
import models.Course;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.BasePage;
import pages.interfaces.admin.CourseManagementPage;
import utils.ReportManager;
import utils.ScreenshotUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class CourseManagementPageWeb extends BasePage implements CourseManagementPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(CourseManagementPageWeb.class.getName());

    private final By courseManagementHeading = By.xpath("//h1[contains(., 'Course Management')]");

    private final By coursesAvailableText = By.xpath("//p[contains(., 'courses available')]");

    private final By addCourseBtn = By.xpath(
            "//div[./button[contains(., 'Add Course')]]/button[contains(., 'Add Course')]");

    // <editor-fold desc="Course Form">
    private final By addCourseForm = By.xpath("//form");

    private final By courseTitleField = By.xpath("//label[normalize-space()='Course Title *']/following::input[1]");

    private final By courseDescriptionField = By.xpath(
            "//label[normalize-space()='Description *']/following::textarea[1]");

    private final By courseDurationField = By.xpath("//label[normalize-space()='Duration']/following::input[1]");

    private final By courseLevelDropdown = By.xpath("//label[normalize-space()='Level']/following::select[1]");

    private final By coursePriceField = By.xpath("//input[@type='number']");

    private final By courseThumbnailUrlField = By.xpath(
            "//label[normalize-space()='Thumbnail URL']/following::input[1]");

    private final By courseMeetingUrlField = By.xpath("//label[contains(text(),'Meeting URL')]/following::input[1]");

    private final By coursePublishedCheckboxField = By.xpath("//input[@type='checkbox']");

    private final By createCourseBtn = By.xpath("//button[normalize-space()='Create Course']");

    private final By clickCancelCourseBtn = By.xpath("//button[normalize-space()='Cancel']");
    // </editor-fold>

    private int numberOfCoursesAvailable;
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

        WebElement element = getElement(coursesAvailableText);
        numberOfCoursesAvailable = Integer.parseInt(element.getText()
                                                           .replace(" courses available", ""));
    }

    @Override
    public void clickAddCourseBtn() {
        clickButton(addCourseBtn);
    }

    @Override
    public void verifyBlankCourseFormIsDisplayed() {
        validateBlankCourseForm();
    }

    @Override
    public void addCourse(Course course) {
        logger.info("Adding course " + course.toString());
        ReportManager.getTest()
                     .info("Add course: " + course);

        this.clearAddCourseForm();

        populateCourseData(course);

        ScreenshotUtils.captureAndAttach(driver, "Creating Course");

        this.clickCreateCourseBtn();
    }

    @Override
    public void cancelCourse(Course course) {
        logger.info("Adding course " + course.toString());
        ReportManager.getTest()
                     .info("Add course: " + course);

        this.clearAddCourseForm();

        populateCourseData(course);

        this.clickCancelCourseBtn();

        ScreenshotUtils.captureAndAttach(driver, "After clicking Cancel button");
    }

    @Override
    public void validateCourseTitleErrorMessage(String expectedMessage) {
        String actualMessage = getValidationMessage(courseTitleField);
        Assert.assertTrue(
                actualMessage.contains(expectedMessage),
                "Expected message: " + expectedMessage + ", but actual message: " + actualMessage
        );
    }

    @Override
    public void validateCourseDescriptionErrorMessage(String expectedMessage) {
        String actualMessage = getValidationMessage(courseDescriptionField);
        Assert.assertTrue(
                actualMessage.contains(expectedMessage),
                "Expected message: " + expectedMessage + ", but actual message: " + actualMessage
        );
    }

    @Override
    public void verifyAlertMessage(String expectedMessage) {
        ReportManager.getTest()
                     .info("Verifying alert message is: " + expectedMessage);
        this.alertUtils.verifyIfAlertMessageIsCorrect(expectedMessage);
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private void clearAddCourseForm() {
        this.enterCourseTitle("");
        this.enterCourseDescription("");
        this.enterCourseDuration("");
        this.enterCourseLevel("Beginner");
        this.enterCoursePrice("");
        this.enterCourseThumbnailUrl("");
        this.enterCourseMeetingUrl("");
        this.publishCourse(false);
    }

    private void validateBlankCourseForm() {
        logger.info("Verify the fields on the course form");

        WebElement addCourseFormElement;
        try {
            addCourseFormElement = getElement(addCourseForm);
        } catch (TimeoutException ex) {
            throw new TimeoutException("Add Course Form not displayed");
        }

        // checking fields
        List<String> expectedFields = new ArrayList<>(
                Arrays.asList(
                        "course title", "description", "duration", "level", "price", "thumbnail url",
                        "meeting url", "published"
                ));

        for (WebElement fieldRow : addCourseFormElement.findElements(By.tagName("div"))) {
            if (expectedFields.isEmpty())
                break;

            String labelText = fieldRow.findElement(By.tagName("label"))
                                       .getText()
                                       .toLowerCase();

            Optional<String> matchedField = expectedFields.stream()
                                                          .filter(labelText::contains)
                                                          .findFirst();

            if (matchedField.isEmpty())
                continue;

            String fieldName = matchedField.get();
            expectedFields.remove(fieldName);

            String errorMessage = fieldName + " is not blank";

            switch (fieldName) {
                case "course title" -> Assert.assertEquals(
                        getElement(courseTitleField).getAttribute("value"),
                        "",
                        errorMessage
                );
                case "description" -> Assert.assertNull(
                        getElement(courseDescriptionField).getAttribute("value"),
                        errorMessage
                );
                case "duration" -> Assert.assertEquals(
                        getElement(courseDurationField).getAttribute("value"),
                        "",
                        errorMessage
                );
                case "level" -> Assert.assertEquals(
                        getSelectedOptionInDropdown(courseLevelDropdown),
                        "Beginner",
                        errorMessage
                );
                case "price" -> Assert.assertEquals(
                        getElement(coursePriceField).getAttribute("value"),
                        "0",
                        errorMessage
                );
                case "thumbnail url" -> Assert.assertEquals(
                        getElement(courseThumbnailUrlField).getAttribute("value"),
                        "",
                        errorMessage
                );
                case "meeting url" -> Assert.assertEquals(
                        getElement(courseMeetingUrlField).getAttribute("value"),
                        "",
                        errorMessage
                );
                case "published" -> Assert.assertTrue(
                        getElement(coursePublishedCheckboxField).isSelected(),
                        errorMessage
                );
            }

            logger.info(fieldName + " is found");
        }

        Assert.assertEquals(expectedFields.size(), 0, "All the expected fields are not present");
    }

    private void populateCourseData(Course course) {
        if (course.getTitle() != null && !course.getTitle()
                                                .isEmpty())
            this.enterCourseTitle(course.getTitle());

        if (course.getDescription() != null && !course.getDescription()
                                                      .isEmpty())
            this.enterCourseDescription(course.getDescription());

        if (course.getDuration() != null && !course.getDuration()
                                                   .isEmpty())
            this.enterCourseDuration(course.getDuration());

        if (course.getLevel() != null && !course.getLevel()
                                                .isEmpty())
            this.enterCourseLevel(course.getLevel());

        this.enterCoursePrice(String.format("%.0f", course.getPrice()));

        if (course.getThumbnailUrl() != null && !course.getThumbnailUrl()
                                                       .isEmpty())
            this.enterCourseThumbnailUrl(course.getThumbnailUrl());

        if (course.getMeetingUrl() != null && !course.getMeetingUrl()
                                                     .isEmpty())
            this.enterCourseMeetingUrl(course.getMeetingUrl());

        this.publishCourse(course.isPublished());
    }

    private void enterCourseTitle(String courseTitle) {
        this.enterKeys(courseTitleField, courseTitle);
    }

    private void enterCourseDescription(String courseDescription) {
        this.enterKeys(courseDescriptionField, courseDescription);
    }

    private void enterCourseDuration(String courseDuration) {
        this.enterKeys(courseDurationField, courseDuration);
    }

    private void enterCourseLevel(String courseLevel) {
        this.selectByVisibleText(courseLevelDropdown, courseLevel);
    }

    private void enterCoursePrice(String coursePrice) {
        this.enterKeys(coursePriceField, coursePrice);
    }

    private void enterCourseThumbnailUrl(String courseThumbnailUrl) {
        this.enterKeys(courseThumbnailUrlField, courseThumbnailUrl);
    }

    private void enterCourseMeetingUrl(String courseMeetingUrl) {
        this.enterKeys(courseMeetingUrlField, courseMeetingUrl);
    }

    private void publishCourse(boolean publish) {
        WebElement element = getElement(coursePublishedCheckboxField);

        if (publish && !element.isSelected())
            element.click();

        if (!publish && element.isSelected())
            element.click();
    }

    private void clickCreateCourseBtn() {
        clickButton(createCourseBtn);
    }

    private void clickCancelCourseBtn() {
        clickButton(clickCancelCourseBtn);
    }
    // </editor-fold>

}
