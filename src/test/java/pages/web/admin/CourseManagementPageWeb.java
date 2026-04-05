package pages.web.admin;

import io.appium.java_client.AppiumDriver;
import models.Course;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import pages.interfaces.admin.ICourseManagementPage;
import pages.web.BasePageWeb;
import utils.AlertUtils;
import utils.ReportManager;
import utils.ScreenshotUtils;
import utils.SoftAssertManager;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

public class CourseManagementPageWeb extends BasePageWeb implements ICourseManagementPage {

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

    private final By saveCourseBtn = By.xpath("//button[contains(., \"Update Course\") and @type=\"submit\"]");

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
    public WebElement validateCourseIsDisplayed(Course course) {
        try {
            logger.info("Verifying course is displayed: " + course.getTitle());
            WebElement courseCardElement = findCourse(course);
            scrollIntoView(courseCardElement);
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
    public void deleteCourse(WebElement courseElement) {
        logger.info("Deleting course");
        ReportManager.getTest()
                     .info("Deleting course");

        clickCourseDeleteBtn(courseElement);

        alertUtils.verifyIfConfirmationAlertMessageIsCorrect(
                "Are you sure you want to delete this course?",
                true
        );

        alertUtils.verifyIfAlertMessageIsCorrect(
                "Course deleted successfully!"
        );

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
        String expectedMessage = "fill";
        String actualMessage = getValidationMessage(courseTitleField);
        Assert.assertTrue(
                actualMessage.contains(expectedMessage),
                "Expected message: " + expectedMessage + ", but actual message: " + actualMessage
        );
    }

    @Override
    public void validateCourseDescriptionErrorMessage() {
        String expectedMessage = "fill";
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
        closeKeyboardIfOpen();
        clickButton(createCourseBtn);
    }

    private void clickSaveCourseBtn() {
        closeKeyboardIfOpen();
        clickButton(saveCourseBtn);
    }

    private void clickCancelCourseBtn() {
        clickButton(clickCancelCourseBtn);
    }

    private WebElement findCourse(Course course) {
        return getElement(
                By.xpath(
                        "//div[contains(@class,'courses-grid')]" +
                                "//h3[normalize-space()='" + course.getTitle() + "']/ancestor::div[2]"
                )
        );
    }

    private void validateCourseContent(WebElement courseCardElement, Course course) {
        // Validate description
        logger.info("Validating description");
        WebElement description = courseCardElement.findElement(By.tagName("p"));
        String actualDescription = description.getText()
                                              .trim();
        Assert.assertEquals(
                actualDescription,
                course.getDescription(),
                "Expected description: " + course.getDescription() + ", but actual description: " + actualDescription
        );


        SoftAssert softAssert = SoftAssertManager.getSoftAssert();


        logger.info("Validating level and duration");
        try {
            List<WebElement> badges = courseCardElement.findElements(
                    By.xpath("./div[2]/div[1]/span")
            );

            // normalise text (avoid repeated getText calls)
            List<String> badgeTexts = badges.stream()
                                            .map(e -> e.getText()
                                                       .trim())
                                            .toList();


            // Validate level
            logger.info("Validating level");
            if (badgeTexts.isEmpty()) {
                softAssert.assertTrue(
                        false,
                        "Expected course level: " + course.getLevel() + ", but no badges found"
                );
            } else {
                String actualLevel = badgeTexts.get(0);
                softAssert.assertTrue(
                        actualLevel.equalsIgnoreCase(course.getLevel()),
                        "Expected course level: " + course.getLevel() + ", but actual course level: " + actualLevel
                );
            }


            // Validate duration
            logger.info("Validating duration");
            String expectedDuration = course.getDuration();
            if (expectedDuration != null && !expectedDuration.isBlank()) {
                if (badgeTexts.size() < 2) {
                    softAssert.assertTrue(
                            false,
                            "Expected course duration: " + expectedDuration + ", but duration badge not found"
                    );
                } else {
                    String actualDuration = badgeTexts.get(1);

                    softAssert.assertTrue(
                            actualDuration.toLowerCase()
                                          .contains(expectedDuration.toLowerCase()),
                            "Expected course duration: " + expectedDuration + ", but actual course duration: " + actualDuration
                    );
                }
            } else {
                logger.info("Duration not provided in expected data -> skipping validation");
            }
        } catch (NoSuchElementException ex) {
            softAssert.assertTrue(
                    false,
                    "Expected course level " + course.getLevel() + ", duration: " + course.getLevel() + ", but got none"
            );
        }


        // Validate price
        logger.info("Validating price");
        try {
            BigDecimal expectedCoursePrice = course.getPrice();

            List<WebElement> priceElements = courseCardElement.findElements(
                    By.xpath(".//span[contains(text(),'R')]")
            );

            // price not provided or price = 0
            if (expectedCoursePrice == null || expectedCoursePrice.compareTo(BigDecimal.valueOf(0f)) == 0) {
                if (priceElements.isEmpty()) {
                    String errorMsg = expectedCoursePrice == null
                            ? "No price provided -> price not displayed (correct)"
                            : "Expected price 'Free' or 'R0.00', but got nothing";
                    logger.info(errorMsg);
                } else {
                    String actual = priceElements.get(0)
                                                 .getText()
                                                 .trim();

                    softAssert.assertTrue(
                            actual.equalsIgnoreCase("Free") || actual.equalsIgnoreCase("R0.00"),
                            "Expected no price or 'Free', but got: " + actual
                    );
                }
            } else {
                if (priceElements.isEmpty()) {
                    softAssert.assertTrue(
                            false,
                            "Expected price: " + expectedCoursePrice + ", but nothing displayed"
                    );
                } else {
                    String actual = priceElements.get(0)
                                                 .getText()
                                                 .trim();

                    String expectedCoursePriceFormatted = "R" + String.format(
                            Locale.ENGLISH, "%.2f", expectedCoursePrice
                    );

                    softAssert.assertEquals(
                            actual,
                            expectedCoursePriceFormatted,
                            "Expected course price: " + expectedCoursePriceFormatted + ", but actual course price: " + actual
                    );
                }
            }
        } catch (NoSuchElementException ex) {
            softAssert.assertTrue(
                    false,
                    "Expected course price: " + course.getPrice() + ", but got none"
            );
        }


        // Validate thumbnail
        logger.info("Validating thumbnail");
        try {
            WebElement thumbnail = courseCardElement.findElement(By.xpath("./div[1]"));
            String thumbnailStyle = thumbnail.getAttribute("style");

            String expectedUrl = course.getThumbnailUrl();
            String actualUrl = extractBackgroundUrl(thumbnailStyle);

            // No thumbnail expected
            if (expectedUrl == null || expectedUrl.isBlank()) {
                // Should not contain url(...)
                softAssert.assertTrue(
                        actualUrl == null,
                        "Expected default thumbnail (no URL), but found: " + actualUrl
                );
            }

            // Thumbnail expected
            else {
                softAssert.assertTrue(
                        actualUrl != null,
                        "Expected course thumbnail URL: " + expectedUrl + ", but no URL found in style"
                );

                softAssert.assertEquals(
                        actualUrl,
                        expectedUrl,
                        "Expected course thumbnail URL: " + expectedUrl + ", but actual course thumbnail url found: " + actualUrl
                );
            }
        } catch (NoSuchElementException ex) {
            softAssert.assertTrue(
                    false,
                    "Course thumbnail is not found"
            );
        }


        // Validate Published or Draft
        logger.info("Validating Published or Draft");
        if (course.isPublished()) {
            boolean publishedTextVisible = verifyIfTextDisplayedInElement(courseCardElement, "Published");
            softAssert.assertTrue(
                    publishedTextVisible,
                    "Course should be published but 'Published' label not found"
            );
        } else {
            boolean draftTextVisible = verifyIfTextDisplayedInElement(courseCardElement, "Draft");
            softAssert.assertTrue(
                    draftTextVisible,
                    "Course should be draft but 'Draft' label not found"
            );
        }
    }

    private void clickCourseEditBtn(WebElement courseElement) {
        WebElement editButton = courseElement.findElement(By.xpath(".//button[1]"));
        clickButton(editButton);
    }

    private void clickCourseDeleteBtn(WebElement courseElement) {
        WebElement deleteButton = courseElement.findElement(By.xpath(".//button[2]"));
        clickButton(deleteButton);
    }
    // </editor-fold>

}
