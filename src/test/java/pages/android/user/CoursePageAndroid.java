package pages.android.user;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import models.Course;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;
import pages.android.BasePageAndroid;
import pages.interfaces.user.ICoursePage;
import pages.web.user.CoursePageWeb;
import utils.ScreenshotUtils;
import utils.SoftAssertManager;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.logging.Logger;

public class CoursePageAndroid extends BasePageAndroid implements ICoursePage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(CoursePageWeb.class.getName());


    // </editor-fold>

    // <editor-fold desc="Ctor">
    public CoursePageAndroid(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public WebElement findCourse(Course course) {
        logger.info("Finding course: " + course.getTitle());
        try {
            WebElement courseCardElement = findCourse(course.getTitle());
            ScreenshotUtils.captureAndAttach(driver, "Course content");

            return courseCardElement;
        } catch (Exception ex) {
            logger.info("Course did not exist!");
            ScreenshotUtils.captureAndAttach(driver, "Course does not exist!");
            return null;
        }
    }

    @Override
    public void validateCourseDetails(Course course, WebElement courseCardElement, boolean shouldEnroll) {
        logger.info("Validate course details: " + course);

        SoftAssert softAssert = SoftAssertManager.getSoftAssert();


        String contentDesc = courseCardElement.getAttribute("content-desc");
        logger.info("Full content-desc: " + contentDesc);

        // Split lines
        String[] lines = contentDesc.split("\\n");


        // Validate description
        logger.info("Validating description");
        try {
            String actualDescription = lines[3].trim();

            softAssert.assertEquals(
                    actualDescription,
                    course.getDescription(),
                    "Expected description: " + course.getDescription() + ", but actual description: " + actualDescription
            );
        } catch (NoSuchElementException ex) {
            softAssert.assertTrue(
                    false,
                    "Course description is not found"
            );
        }


        // Validate level
        logger.info("Validating level");
        try {
            String actualLevel = lines[4].trim();
            softAssert.assertTrue(
                    actualLevel.equalsIgnoreCase(course.getLevel()),
                    "Expected level: " + course.getLevel() + ", but actual level: " + actualLevel
            );
        } catch (NoSuchElementException ex) {
            softAssert.assertTrue(
                    false,
                    "Course level is not found"
            );
        }


        // Validate price
        logger.info("Validating price");
        try {
            BigDecimal expectedCoursePrice = course.getPrice();
            String actualPrice = lines[5].trim();

            // price not provided or price = 0
            if (expectedCoursePrice == null || expectedCoursePrice.compareTo(BigDecimal.valueOf(0)) == 0) {
                softAssert.assertTrue(
                        actualPrice.equalsIgnoreCase("Free") ||
                                actualPrice.equalsIgnoreCase("R0.00") ||
                                actualPrice.equalsIgnoreCase("R 0.00"),
                        "Expected no price or 'Free', but got: " + actualPrice
                );
            } else {
                String expectedCoursePriceFormatted = String.format(
                        Locale.ENGLISH, "%.2f", course.getPrice()
                );

                softAssert.assertTrue(
                        actualPrice.equalsIgnoreCase("R" + expectedCoursePriceFormatted) ||
                                actualPrice.equalsIgnoreCase("R " + expectedCoursePriceFormatted),
                        "Expected price: " + expectedCoursePriceFormatted + ", but actual price: " + actualPrice
                );
            }
        } catch (NoSuchElementException ex) {
            softAssert.assertTrue(
                    false,
                    "Course price is not found"
            );
        }


        /*
        // Validate thumbnail
        logger.info("Validating thumbnail");
        try {
            List<WebElement> images = courseCardElement.findElements(By.xpath("./img"));
            String expectedUrl = course.getThumbnailUrl();

            // Image exists
            if (!images.isEmpty()) {
                WebElement img = images.get(0);
                String actualUrl = img.getAttribute("src");

                softAssert.assertTrue(
                        expectedUrl != null && !expectedUrl.isBlank(),
                        "Image is displayed but no expected thumbnail URL was provided"
                );

                softAssert.assertEquals(
                        actualUrl,
                        expectedUrl,
                        "Expected course thumbnail URL: " + expectedUrl +
                                ", but found: " + actualUrl
                );
            }

            // No image → fallback div
            else {
                WebElement thumbnailDiv = courseCardElement.findElement(By.xpath("./div[1]"));
                String style = thumbnailDiv.getAttribute("style");

                String actualUrl = extractBackgroundUrl(style);

                softAssert.assertTrue(
                        expectedUrl == null || expectedUrl.isBlank(),
                        "Expected image thumbnail, but fallback UI is shown"
                );

                softAssert.assertTrue(
                        actualUrl == null,
                        "Expected default thumbnail (no URL), but found: " + actualUrl
                );
            }
        } catch (NoSuchElementException ex) {
            softAssert.assertTrue(
                    false,
                    "Course thumbnail is not found"
            );
        }
        */


        // Validate enrolled
        logger.info("Validating enrolled");
        try {
            WebElement enrolled = courseCardElement.findElement(By.xpath(".//android.widget.Button"));
            String enrolledText = enrolled.getAttribute("content-desc");

            boolean userEnrolled = enrolledText.equalsIgnoreCase("Enrolled");

            softAssert.assertEquals(
                    shouldEnroll,
                    userEnrolled,
                    "Expected enrolled: " + shouldEnroll + ", but actual enrolled: " + userEnrolled
            );
        } catch (NoSuchElementException ex) {
            softAssert.assertTrue(
                    false,
                    "Course enrolled is not found"
            );
        }
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private WebElement findCourse(String courseTitle) {
        return getElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().descriptionContains(\"" + courseTitle + "\"))"
        ));
    }
    // </editor-fold>

}
