package pages.android.admin;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import models.Course;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pages.BasePage;
import pages.interfaces.admin.ICourseManagementPage;
import utils.ReportManager;
import utils.ScreenshotUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class CourseManagementPageAndroid extends BasePage implements ICourseManagementPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(CourseManagementPageAndroid.class.getName());

    private final By courseManagementHeading = By.xpath("//android.view.View[@content-desc=\"Manage Courses\"]");

    private final By addCourseBtn = By.xpath("//android.widget.Button[@content-desc=\"+ Create New Course\"]");

    // <editor-fold desc="Course Form">
    private final By courseTitleField = AppiumBy.xpath("//android.widget.EditText[@hint='Course Title *']");

    private final By courseDescriptionField = AppiumBy.xpath("//android.widget.EditText[@hint='Description *']");

    private final By courseDurationField = AppiumBy.xpath("//android.widget.EditText[@hint='Duration']");

    private final By coursePriceField = AppiumBy.xpath("//android.widget.EditText[@hint='Price (R)']");

    private final By courseThumbnailUrlField = AppiumBy.xpath(
            "//android.widget.EditText[contains(@hint,'Thumbnail URL')]");

    private final By courseMeetingUrlField = AppiumBy.xpath("//android.widget.EditText[contains(@hint,'Meeting URL')]");

    private final By coursePublishedCheckboxField = AppiumBy.className("android.widget.CheckBox");

    private final By createCourseBtn = By.xpath("//android.widget.Button[@content-desc=\"Create Course\"]");

    private final By clickCancelCourseBtn = By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]");
    // </editor-fold>

    private int numberOfCoursesAvailable;
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public CourseManagementPageAndroid(AppiumDriver driver) {
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
    public void validateCourseTitleErrorMessage() {
        String expectedMessage = "required";
        validateErrorMessage(courseTitleField, expectedMessage);
    }

    @Override
    public void validateCourseDescriptionErrorMessage() {
        String expectedMessage = "required";
        validateErrorMessage(courseDescriptionField, expectedMessage);
    }

    @Override
    public void verifyAlertMessage(String expectedMessage) {
        ReportManager.getTest()
                     .info("Verifying alert message is: " + expectedMessage);
        String actualMessage = getSnackBarText().toLowerCase();
        Assert.assertTrue(
                actualMessage.contains(expectedMessage),
                "Expected alert message: " + expectedMessage + ", but actual alert message: " + actualMessage
        );
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

        Wait<AppiumDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(5))
                                                          .pollingEvery(Duration.ofMillis(300));

        boolean dialogVisible = wait.until(d -> d.getPageSource()
                                                 .contains("Create New Course"));

        if (!dialogVisible) throw new RuntimeException("Create Course dialog not visible");

        Document doc = getSourceXml();

        // checking fields
        List<String> expectedFields = new ArrayList<>(
                Arrays.asList("course title", "description", "duration", "level", "price", "thumbnail url",
                        "meeting url", "published"
                ));

        NodeList editNodes = doc.getElementsByTagName("android.widget.EditText");

        for (int i = 0; i < editNodes.getLength(); i++) {
            Element element = (Element) editNodes.item(i);

            String labelText = element.getAttribute("hint")
                                      .toLowerCase();

            Optional<String> matchedField = expectedFields.stream()
                                                          .filter(labelText::contains)
                                                          .findFirst();

            if (matchedField.isEmpty()) continue;

            String fieldName = matchedField.get();
            expectedFields.remove(fieldName);

            Assert.assertNotNull(element, fieldName + " is not displayed");

            String errorMessage = fieldName + " is not displayed";

            switch (fieldName) {
                case "course title", "description", "duration", "thumbnail url", "meeting url" ->
                        Assert.assertEquals(element.getAttribute("text"), "", errorMessage);
                case "price" -> Assert.assertEquals(element.getAttribute("text"), "0", errorMessage);
                case "published" -> Assert.assertEquals(element.getAttribute("checked"), "true", errorMessage);
            }

            logger.info(fieldName + " is found");
        }

        String fieldName = "level";
        String selectedLevel = getSelectedOptionInDropdown(fieldName);
        if (selectedLevel != null) {
            Assert.assertEquals(selectedLevel, "beginner", fieldName + " is not displayed");

            expectedFields.remove(fieldName);

            logger.info(fieldName + " is found");
        }

        fieldName = "published";
        Element checkbox = findCheckbox(doc);
        if (checkbox != null) {
            Assert.assertEquals(checkbox.getAttribute("checked"), "true", fieldName + " is not displayed");

            expectedFields.remove(fieldName);

            logger.info(fieldName + " is found");
        }

        Assert.assertEquals(expectedFields.size(), 0, "All the expected fields are not present");
    }

    private void populateCourseData(Course course) {
        if (course.getTitle() != null && !course.getTitle()
                                                .isEmpty()) this.enterCourseTitle(course.getTitle());

        if (course.getDescription() != null && !course.getDescription()
                                                      .isEmpty()) this.enterCourseDescription(course.getDescription());

        if (course.getDuration() != null && !course.getDuration()
                                                   .isEmpty())
            this.enterCourseDuration(course.getDuration());

        if (course.getLevel() != null && !course.getLevel()
                                                .isEmpty())
            this.enterCourseLevel(course.getLevel());

        if (course.getPrice() != 0)
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
        this.setDropdownValue("Level", courseLevel);
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

        boolean isChecked = Boolean.parseBoolean(element.getAttribute("checked"));

        if (publish && !isChecked) element.click();

        if (!publish && isChecked) element.click();
    }

    private void clickCreateCourseBtn() {
        closeKeyboardIfOpenAndroid();
        clickButton(createCourseBtn);
    }

    private void clickCancelCourseBtn() {
        closeKeyboardIfOpenAndroid();
        clickButton(clickCancelCourseBtn);
    }
    // </editor-fold>

}
