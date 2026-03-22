package tests.admin.course;

import models.Course;
import org.testng.annotations.Test;
import tests.TestsBase;
import utils.ConfigManager;
import utils.CourseDataGenerator;

public class CreateCourseValidationTests extends TestsBase {

    // <editor-fold desc="Overrides">
    @Override
    protected void setUpPage() {
        homePage.verifyHomePageIsDisplayed();

        navigationBar.goToLoginPage();

        loginPage.verifyLoginPageIsDisplayed();

        loginPage.loginUser(ConfigManager.getAdminEmail(), ConfigManager.getAdminPassword());

        dashboardPage.verifyDashboardPageIsDisplayed();

        navigationBar.goToAdminPanel();

        adminDashboardPage.verifyAdminDashboardIsDisplayed();

        adminDashboardPage.navigateToManageCourses();

        courseManagementPage.verifyCourseManagementPageIsDisplayed();

        courseManagementPage.clickAddCourseBtn();

        courseManagementPage.verifyBlankCourseFormIsDisplayed();
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Test(description = "Submit Blank Form", groups = "2. Create Course Negative Tests")
    public void submitBlankForm() {
        Course course = new Course();
        courseManagementPage.addCourse(course);

        courseManagementPage.validateCourseTitleErrorMessage("fill");
    }

    @Test(description = "Submit Title Only", groups = "2. Create Course Negative Tests", priority = 1)
    public void submitTitleOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        courseManagementPage.addCourse(course);

        courseManagementPage.validateCourseDescriptionErrorMessage("fill");
    }

    @Test(description = "Submit Description Only", groups = "2. Create Course Negative Tests", priority = 2)
    public void submitDescriptionOnly() {
        Course course = new Course();
        course.setDescription(CourseDataGenerator.randomDescription());
        courseManagementPage.addCourse(course);

        courseManagementPage.validateCourseTitleErrorMessage("fill");
    }

    @Test(description = "Submit Long Course Title", groups = "2. Create Course Negative Tests", priority = 3)
    public void submitLongCourseTitle() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.longCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Long Course Description", groups = "2. Create Course Negative Tests", priority = 4)
    public void submitLongCourseDescription() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.longDescription());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("success");
    }

    @Test(description = "Submit Alphanumeric Duration", groups = "2. Create Course Negative Tests", priority = 5)
    public void submitAlphaNumericDuration() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration("ABC@#$%");
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Large Price", groups = "2. Create Course Negative Tests", priority = 6)
    public void submitLargePrice() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPrice(CourseDataGenerator.largePrice());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Invalid Thumbnail URL", groups = "2. Create Course Negative Tests", priority = 7)
    public void submitInvalidThumbnailUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setThumbnailUrl(CourseDataGenerator.invalidUrl());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Valid Thumbnail URL", groups = "2. Create Course Negative Tests", priority = 8)
    public void submitValidThumbnailUrl() {
        String url = CourseDataGenerator.validThumbnailUrl();

        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setThumbnailUrl(url);
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("success");
    }

    @Test(description = "Submit Invalid Meeting URL", groups = "2. Create Course Negative Tests", priority = 9)
    public void submitInvalidMeetingUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setMeetingUrl("https://zoom.us/j/123456");
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Valid Teams URL", groups = "2. Create Course Negative Tests", priority = 10)
    public void submitValidTeamsUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("success");
    }

    @Test(description = "Submit Unpublished Course", groups = "2. Create Course Negative Tests", priority = 11)
    public void submitUnpublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(false);
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("success");
    }

    @Test(description = "Submit Published Course", groups = "2. Create Course Negative Tests", priority = 12)
    public void submitPublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("success");
    }

    @Test(description = "Cancel Course Creation", groups = "2. Create Course Negative Tests", priority = 13)
    public void cancelCourseCreation() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);
        courseManagementPage.cancelCourse(course);

        courseManagementPage.verifyCourseManagementPageIsDisplayed();

        courseManagementPage.clickAddCourseBtn();

        courseManagementPage.verifyBlankCourseFormIsDisplayed();
    }
    // </editor-fold>

}
