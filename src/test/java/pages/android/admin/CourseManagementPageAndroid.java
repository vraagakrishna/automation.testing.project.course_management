package pages.android.admin;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import models.Course;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pages.android.BasePageAndroid;
import pages.interfaces.admin.ICourseManagementPage;
import utils.ConfigManager;
import utils.ReportManager;
import utils.ScreenshotUtils;
import utils.SoftAssertManager;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

public class CourseManagementPageAndroid extends BasePageAndroid implements ICourseManagementPage {

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

    private final By saveCourseBtn = By.xpath("//android.widget.Button[@content-desc=\"Save Changes\"]");

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

        ConfigManager.courses.add(course);

        this.clearAddCourseForm();

        populateCourseData(course);

        ScreenshotUtils.captureAndAttach(driver, "Creating Course");

        this.clickCreateCourseBtn();
    }

    @Override
    public WebElement validateCourseIsDisplayed(Course course) {
        try {
            logger.info("Verifying course is displayed: " + course.getTitle());
            WebElement courseCardElement = findCourse(course);
            ScreenshotUtils.captureAndAttach(driver, "Course content");

            try {
                validateCourseContent(courseCardElement, course);
                return courseCardElement;
            } catch (AssertionError | NoSuchElementException ex) {
                logger.info("Course content is incorrect!");
                ScreenshotUtils.captureAndAttach(driver, "Course content is incorrect!");
                return null;
            }
        } catch (Exception ex) {
            logger.info("Course did not exist after creation!");
            SoftAssertManager.getSoftAssert()
                             .assertTrue(false, "Course should exist after creation!");
            ScreenshotUtils.captureAndAttach(driver, "Course should exist after creation!");
            return null;
        }
    }

    @Override
    public WebElement validateCourseIsDisplayedAndNoAssertion(Course course) {
        try {
            logger.info("Verifying course is displayed: " + course.getTitle());
            return findCourse(course);
        } catch (Exception ex) {
            logger.info("Course does not exist");
            return null;
        }
    }

    @Override
    public void validateCourseIsNotDisplayed(Course course) {
        try {
            logger.info("Verifying course is not displayed: " + course.getTitle());
            findCourse(course);
            ScreenshotUtils.captureAndAttach(driver, "Course content");

            SoftAssertManager.getSoftAssert()
                             .assertTrue(false, "Course should not exist after deletion!");
        } catch (Exception ex) {
            // ignored
        }
    }

    @Override
    public void editCourse(WebElement courseElement, Course course) {
        logger.info("Editing course " + course.toString());
        ReportManager.getTest()
                     .info("Edit course: " + course);

        ConfigManager.courses.add(course);

        clickCourseEditBtn(courseElement);

        populateCourseData(course);

        ScreenshotUtils.captureAndAttach(driver, "Edited Course");

        this.clickSaveCourseBtn();
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
    public void clickCancelCourseBtn() {
        logger.info("Clicking Cancel button");
        closeKeyboardIfOpen();
        clickButton(clickCancelCourseBtn);
    }

    @Override
    public void deleteCourse(WebElement courseElement) {
        logger.info("Deleting course");
        ReportManager.getTest()
                     .info("Deleting course");

        clickCourseDeleteBtn(courseElement);

        deleteCourseUsingPopup();
        verifyAlertMessage("deleted");

        ScreenshotUtils.captureAndAttach(driver, "After clicking Delete button");
    }

    @Override
    public void cancelEditCourse(WebElement courseElement, Course course) {
        logger.info("Editing course " + course.toString());
        ReportManager.getTest()
                     .info("Edit course: " + course);

        clickCourseEditBtn(courseElement);

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
        if (course.getTitle() != null)
            this.enterCourseTitle(course.getTitle());

        if (course.getDescription() != null)
            this.enterCourseDescription(course.getDescription());

        if (course.getDuration() != null)
            this.enterCourseDuration(course.getDuration());

        if (course.getLevel() != null)
            this.enterCourseLevel(course.getLevel());

        if (course.getPrice() != null)
            this.enterCoursePrice(String.format("%.0f", course.getPrice()));

        if (course.getThumbnailUrl() != null)
            this.enterCourseThumbnailUrl(course.getThumbnailUrl());

        if (course.getMeetingUrl() != null)
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
        closeKeyboardIfOpen();
        clickButton(createCourseBtn);
    }

    private void clickSaveCourseBtn() {
        closeKeyboardIfOpen();
        clickButton(saveCourseBtn);
    }

    private WebElement findCourse(Course course) {
        return getElement(
                By.xpath(
                        "//android.view.View[contains(@content-desc,'" + course.getTitle() + "')]"
                )
        );
    }

    private void validateCourseContent(WebElement courseCardElement, Course course) {
        String contentDesc = courseCardElement.getAttribute("content-desc");
        logger.info("Full content-desc: " + contentDesc);

        // Split lines
        String[] lines = contentDesc.split("\\n");

        // Validate description
        logger.info("Validating description");
        String actualDescription = lines[2].trim();
        Assert.assertEquals(
                actualDescription,
                course.getDescription(),
                "Expected description: " + course.getDescription() + ", but actual description: " + actualDescription
        );

        SoftAssert softAssert = SoftAssertManager.getSoftAssert();


        logger.info("Validate level");
        try {
            String actualLevel = lines[3].trim();
            softAssert.assertEquals(
                    actualLevel,
                    course.getLevel(),
                    "Expected level: " + course.getLevel() + ", but actual level: " + actualLevel
            );
        } catch (NoSuchElementException ex) {
            softAssert.assertTrue(
                    false,
                    "Expected course level " + course.getLevel() + ", level: " + course.getLevel() + ", but got none"
            );
        }


        logger.info("Validate duration");
        try {
            String actualDuration = lines[4].trim();
            actualDuration = actualDuration.equals("-") ? null : actualDuration;  // replacing "-" to empty string
            softAssert.assertEquals(
                    actualDuration,
                    course.getDuration(),
                    "Expected duration: " + course.getDuration() + ", but actual duration: " + actualDuration
            );
        } catch (NoSuchElementException ex) {
            softAssert.assertTrue(
                    false,
                    "Expected course duration " + course.getDuration() + ", duration: " + course.getDuration() + ", but got none"
            );
        }


        logger.info("Validate price");
        try {
            BigDecimal expectedCoursePrice = course.getPrice();
            String actualPrice = lines[5].trim();

            // price not provided or price = 0
            if (expectedCoursePrice == null || expectedCoursePrice.compareTo(BigDecimal.valueOf(0)) == 0) {
                softAssert.assertTrue(
                        actualPrice.equalsIgnoreCase("Free") || actualPrice.equalsIgnoreCase("R 0.00"),
                        "Expected no price or 'Free', but got: " + actualPrice
                );
            } else {
                String expectedCoursePriceFormatted = "R " + String.format(
                        Locale.ENGLISH, "%.2f", course.getPrice()
                );

                softAssert.assertEquals(
                        actualPrice,
                        expectedCoursePriceFormatted,
                        "Expected price: " + expectedCoursePriceFormatted + ", but actual price: " + actualPrice
                );
            }
        } catch (NoSuchElementException ex) {
            softAssert.assertTrue(
                    false,
                    "Expected course price " + course.getPrice() + ", price: " + course.getPrice() + ", but got none"
            );
        }


        // Validate Published or Draft
        logger.info("Validating Published or Draft");
        if (course.isPublished()) {
            boolean publishedTextVisible = lines[1].trim()
                                                   .equals("Published");
            softAssert.assertTrue(
                    publishedTextVisible,
                    "Course should be published but 'Published' label not found"
            );
        } else {
            boolean draftTextVisible = lines[1].trim()
                                               .equals("Draft");
            softAssert.assertTrue(
                    draftTextVisible,
                    "Course should be draft but 'Draft' label not found"
            );
        }
    }

    private void clickCourseEditBtn(WebElement courseElement) {
        WebElement editButton = courseElement.findElement(
                By.xpath(".//android.widget.Button[@content-desc='Edit']")
        );
        editButton.click();
    }

    private void clickCourseDeleteBtn(WebElement courseElement) {
        WebElement editButton = courseElement.findElement(
                By.xpath(".//android.widget.Button[@content-desc='Delete']")
        );
        editButton.click();
    }

    protected void deleteCourseUsingPopup() {
        WebElement deleteBtn = driver.findElement(AppiumBy.accessibilityId("Delete"));

        deleteBtn.click();
    }
    // </editor-fold>

}
