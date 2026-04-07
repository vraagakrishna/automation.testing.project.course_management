package tests.admin.course;

import factory.PageFactory;
import models.Course;
import org.testng.annotations.Test;
import pages.interfaces.IHomePage;
import pages.interfaces.INavigationBar;
import pages.interfaces.admin.ICourseManagementPage;
import pages.interfaces.auth.ILoginPage;
import pages.interfaces.dashboard.IAdminDashboardPage;
import pages.interfaces.dashboard.IDashboardPage;
import tests.TestsBase;
import utils.ConfigManager;
import utils.CourseDataGenerator;

public class CreateCourseTests extends TestsBase {

    // <editor-fold desc="Class Fields / Constants">
    protected IHomePage homePage;

    protected INavigationBar navigationBar;

    protected ILoginPage loginPage;

    protected IDashboardPage dashboardPage;

    protected IAdminDashboardPage adminDashboardPage;

    protected ICourseManagementPage courseManagementPage;
    // </editor-fold>

    // <editor-fold desc="Overrides">
    @Override
    protected void setUpPage() {
        this.homePage = PageFactory.getHomePage(driver);
        this.navigationBar = PageFactory.getNavigationBar(driver);
        this.loginPage = PageFactory.getLoginPage(driver);
        this.dashboardPage = PageFactory.getDashboardPage(driver);
        this.adminDashboardPage = PageFactory.getAdminDashboardPage(driver);
        this.courseManagementPage = PageFactory.getCourseManagementPage(driver);

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

    @Override
    protected void cleanUpPage() {
        cleanUpCourse(
                navigationBar,
                adminDashboardPage,
                courseManagementPage
        );
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Test(description = "Submit Blank Form", groups = "4. Create Course Negative Tests")
    public void submitBlankForm() {
        Course course = new Course();
        courseManagementPage.addCourse(course);

        courseManagementPage.validateCourseTitleErrorMessage();

        courseManagementPage.clickCancelCourseBtn();  // Going back to Course Page
    }

    @Test(description = "Submit Title Only", groups = "4. Create Course Negative Tests", priority = 1)
    public void submitTitleOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        courseManagementPage.addCourse(course);

        courseManagementPage.validateCourseDescriptionErrorMessage();

        courseManagementPage.clickCancelCourseBtn();  // Going back to Course Page
    }

    @Test(description = "Submit Description Only", groups = "4. Create Course Negative Tests", priority = 2)
    public void submitDescriptionOnly() {
        Course course = new Course();
        course.setDescription(CourseDataGenerator.randomDescription());
        courseManagementPage.addCourse(course);

        courseManagementPage.validateCourseTitleErrorMessage();

        courseManagementPage.clickCancelCourseBtn();  // Going back to Course Page
    }

    @Test(description = "Submit Long Course Title", groups = "4. Create Course Negative Tests", priority = 3)
    public void submitLongCourseTitle() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.longCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Long Course Description", groups = "4. Create Course Negative Tests", priority = 4)
    public void submitLongCourseDescription() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.longDescription());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");
    }

    @Test(description = "Submit Alphanumeric Duration", groups = "4. Create Course Negative Tests", priority = 5)
    public void submitAlphaNumericDuration() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration("ABC@#$%");
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Random Duration", groups = "4. Create Course Negative Tests", priority = 6)
    public void submitRandomDuration() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.randomDuration());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");
    }

    @Test(description = "Submit Valid Duration", groups = "4. Create Course Negative Tests", priority = 7)
    public void submitValidDuration() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");
    }

    @Test(description = "Submit Level Beginner", groups = "4. Create Course Negative Tests", priority = 8)
    public void submitLevelBeginner() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setLevel("Beginner");
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");
    }

    @Test(description = "Submit Level Intermediate", groups = "4. Create Course Negative Tests", priority = 9)
    public void submitLevelIntermediate() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setLevel("Intermediate");
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");
    }

    @Test(description = "Submit Level Advanced", groups = "4. Create Course Negative Tests", priority = 10)
    public void submitLevelAdvanced() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setLevel("Advanced");
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");
    }

    @Test(description = "Submit Large Price", groups = "4. Create Course Negative Tests", priority = 11)
    public void submitLargePrice() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPrice(CourseDataGenerator.largePrice());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Normal Price", groups = "4. Create Course Negative Tests", priority = 12)
    public void submitNormalPrice() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPrice(CourseDataGenerator.validPrice());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");
    }

    @Test(description = "Submit Invalid Thumbnail URL", groups = "4. Create Course Negative Tests", priority = 13)
    public void submitInvalidThumbnailUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setThumbnailUrl(CourseDataGenerator.invalidUrl());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Valid Thumbnail URL", groups = "4. Create Course Negative Tests", priority = 14)
    public void submitValidThumbnailUrl() {
        String url = CourseDataGenerator.validThumbnailUrl();

        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setThumbnailUrl(url);
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");
    }

    @Test(description = "Submit Invalid Meeting URL", groups = "4. Create Course Negative Tests", priority = 15)
    public void submitInvalidMeetingUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setMeetingUrl(CourseDataGenerator.invalidUrl());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Valid Teams URL", groups = "4. Create Course Negative Tests", priority = 16)
    public void submitValidTeamsUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");
    }

    @Test(description = "Submit Unpublished Course", groups = "4. Create Course Negative Tests", priority = 17)
    public void submitUnpublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(false);
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");
    }

    @Test(description = "Submit Published Course", groups = "4. Create Course Negative Tests", priority = 18)
    public void submitPublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");
    }

    @Test(description = "Cancel Course Creation", groups = "4. Create Course Negative Tests", priority = 19)
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
