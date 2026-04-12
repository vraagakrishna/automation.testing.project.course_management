package pages.web.user;

import io.appium.java_client.AppiumDriver;
import models.Course;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;
import pages.interfaces.user.ICoursePage;
import pages.web.BasePageWeb;
import utils.ScreenshotUtils;
import utils.SoftAssertManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class CoursePageWeb extends BasePageWeb implements ICoursePage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(CoursePageWeb.class.getName());


    // </editor-fold>

    // <editor-fold desc="Ctor">
    public CoursePageWeb(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public WebElement findCourse(Course course) {
        logger.info("Finding course: " + course.getTitle());
        try {
            WebElement courseCardElement = findCourse(course.getTitle());
            scrollIntoView(courseCardElement);
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


        // Validate description
        logger.info("Validating description");
        try {
            WebElement description = courseCardElement.findElement(By.tagName("p"));
            String actualDescription = description.getText()
                                                  .trim();
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


        // Validate level and price
        logger.info("Validating level and price");
        try {
            List<WebElement> badges = courseCardElement.findElements(
                    By.xpath("./div[2]/span")
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


            // Validate price
            BigDecimal expectedCoursePrice = course.getPrice();

            if (badgeTexts.size() < 2) {
                if (expectedCoursePrice != null) {
                    softAssert.assertTrue(
                            false,
                            "Expected course price: " + expectedCoursePrice + ", but price badge not found"
                    );
                }
            } else {
                String actual = badgeTexts.get(1)
                                          .trim();

                if (expectedCoursePrice == null || expectedCoursePrice.compareTo(BigDecimal.valueOf(0f)) == 0) {
                    softAssert.assertTrue(
                            actual.equalsIgnoreCase("Free") || actual.equalsIgnoreCase("R0.00"),
                            "Expected no price or 'Free', but got: " + actual
                    );
                } else {
                    String expectedCoursePriceFormatted = "R" + String.format(
                            Locale.ENGLISH, "%.0f", expectedCoursePrice
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
                    "Expected course level " + course.getLevel() + ", duration: " + course.getLevel() + ", but got none"
            );
        }


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


        // Validate enrolled
        logger.info("Validating enrolled");
        try {
            WebElement enrolled = courseCardElement.findElement(By.xpath("./button"));
            String enrolledText = enrolled.getText();

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

    // <editor-fold desc="Public Methods">
    private WebElement findCourse(String courseTitle) {
        return getElement(
                By.xpath(
                        "//div[contains(@class,'courses-grid')]" +
                                "//h3[normalize-space()='" + courseTitle + "']/ancestor::div[1]"
                )
        );
    }
    // </editor-fold>

}
