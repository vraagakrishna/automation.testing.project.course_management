package tests.admin.course;

import factory.PageFactory;
import models.Course;
import org.openqa.selenium.WebElement;
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

public class EditCourseTests extends TestsBase {

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
    // <editor-fold desc="Basic Validation">
    @Test(description = "Submit Blank Form", groups = "6. Edit Course Tests")
    public void submitBlankForm() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setTitle("");
        editedCourse.setDescription("");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.validateCourseTitleErrorMessage();

        courseManagementPage.clickCancelCourseBtn();  // Going back to Course Page
    }

    @Test(description = "Submit Title Only", groups = "6. Edit Course Tests", priority = 1)
    public void submitTitleOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // edit course
        Course editedCourse = new Course(course);
        editedCourse.setDescription("");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.validateCourseDescriptionErrorMessage();

        courseManagementPage.clickCancelCourseBtn();  // Going back to Course Page
    }

    @Test(description = "Submit Description Only", groups = "6. Edit Course Tests", priority = 2)
    public void submitDescriptionOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setTitle("");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.validateCourseTitleErrorMessage();

        courseManagementPage.clickCancelCourseBtn();  // Going back to Course Page
    }

    @Test(description = "Cancel Course Editing", groups = "6. Edit Course Tests", priority = 3)
    public void cancelCourseCreation() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        courseManagementPage.cancelEditCourse(courseElement, course);

        courseManagementPage.verifyCourseManagementPageIsDisplayed();

        // validate course details are not updated
        courseManagementPage.validateCourseIsDisplayed(course);

        // validate Add Form is blank
        courseManagementPage.clickAddCourseBtn();
        courseManagementPage.verifyBlankCourseFormIsDisplayed();

        courseManagementPage.clickCancelCourseBtn();  // Going back to Course Page
    }

    @Test(description = "Delete Course", groups = "6. Edit Course Tests", priority = 4)
    public void deleteCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        courseManagementPage.deleteCourse(courseElement);

        courseManagementPage.verifyCourseManagementPageIsDisplayed();

        courseManagementPage.validateCourseIsNotDisplayed(course);
    }
    // </editor-fold>

    // <editor-fold desc="Basic Validation on Unpublished">
    @Test(description = "Submit Unpublished Long Course Title", groups = "6. Edit Course Tests", priority = 5)
    public void submitUnpublishedLongCourseTitle() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setTitle(CourseDataGenerator.longCourseName());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Unpublished Long Course Description", groups = "6. Edit Course Tests", priority = 6)
    public void submitUnpublishedLongCourseDescription() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDescription(CourseDataGenerator.longDescription());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Unpublished Alphanumeric Duration", groups = "6. Edit Course Tests", priority = 7)
    public void submitUnpublishedAlphaNumericDuration() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDuration("ABC@#$%");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Unpublished Random Duration", groups = "6. Edit Course Tests", priority = 8)
    public void submitUnpublishedRandomDuration() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDuration(CourseDataGenerator.randomDuration());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Unpublished Valid Duration", groups = "6. Edit Course Tests", priority = 9)
    public void submitUnpublishedValidDuration() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDuration(CourseDataGenerator.validDuration());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Unpublished Level Beginner", groups = "6. Edit Course Tests", priority = 10)
    public void submitUnpublishedLevelBeginner() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setLevel("Beginner");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Unpublished Level Intermediate", groups = "6. Edit Course Tests", priority = 11)
    public void submitUnpublishedLevelIntermediate() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setLevel("Intermediate");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Unpublished Level Advanced", groups = "6. Edit Course Tests", priority = 12)
    public void submitUnpublishedLevelAdvanced() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setLevel("Advanced");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Unpublished Large Price", groups = "6. Edit Course Tests", priority = 13)
    public void submitUnpublishedLargePrice() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setPrice(CourseDataGenerator.largePrice());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Unpublished Invalid Thumbnail URL", groups = "6. Edit Course Tests", priority = 14)
    public void submitUnpublishedInvalidThumbnailUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setThumbnailUrl(CourseDataGenerator.invalidUrl());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Unpublished Valid Thumbnail URL", groups = "6. Edit Course Tests", priority = 15)
    public void submitUnpublishedValidThumbnailUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Unpublished Invalid Meeting URL", groups = "6. Edit Course Tests", priority = 16)
    public void submitUnpublishedInvalidMeetingUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setMeetingUrl(CourseDataGenerator.invalidUrl());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Unpublished Valid Teams URL", groups = "6. Edit Course Tests", priority = 17)
    public void submitUnpublishedValidTeamsUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setMeetingUrl(CourseDataGenerator.validTeamsLink());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }
    // </editor-fold>

    // <editor-fold desc="Basic Validation on Published">
    @Test(description = "Submit Published Long Course Title", groups = "6. Edit Course Tests", priority = 18)
    public void submitPublishedLongCourseTitle() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setTitle(CourseDataGenerator.longCourseName());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Published Long Course Description", groups = "6. Edit Course Tests", priority = 19)
    public void submitPublishedLongCourseDescription() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDescription(CourseDataGenerator.longDescription());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Published Alphanumeric Duration", groups = "6. Edit Course Tests", priority = 20)
    public void submitPublishedAlphaNumericDuration() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDuration("ABC@#$%");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Published Random Duration", groups = "6. Edit Course Tests", priority = 21)
    public void submitPublishedRandomDuration() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDuration(CourseDataGenerator.randomDuration());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Published Valid Duration", groups = "6. Edit Course Tests", priority = 22)
    public void submitPublishedValidDuration() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDuration(CourseDataGenerator.validDuration());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Published Level Beginner", groups = "6. Edit Course Tests", priority = 23)
    public void submitPublishedLevelBeginner() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setLevel("Beginner");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Published Level Intermediate", groups = "6. Edit Course Tests", priority = 24)
    public void submitPublishedLevelIntermediate() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setLevel("Intermediate");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Published Level Advanced", groups = "6. Edit Course Tests", priority = 25)
    public void submitPublishedLevelAdvanced() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setLevel("Advanced");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Published Large Price", groups = "6. Edit Course Tests", priority = 26)
    public void submitPublishedLargePrice() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setPrice(CourseDataGenerator.largePrice());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Published Invalid Thumbnail URL", groups = "6. Edit Course Tests", priority = 27)
    public void submitPublishedInvalidThumbnailUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setThumbnailUrl(CourseDataGenerator.invalidUrl());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Published Valid Thumbnail URL", groups = "6. Edit Course Tests", priority = 28)
    public void submitPublishedValidThumbnailUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Submit Published Invalid Meeting URL", groups = "6. Edit Course Tests", priority = 29)
    public void submitPublishedInvalidMeetingUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setMeetingUrl(CourseDataGenerator.invalidUrl());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("fail");
    }

    @Test(description = "Submit Published Valid Teams URL", groups = "6. Edit Course Tests", priority = 30)
    public void submitPublishedValidTeamsUrl() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setMeetingUrl(CourseDataGenerator.validTeamsLink());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }
    // </editor-fold>

    // <editor-fold desc="Edit Unpublished Course">
    @Test(description = "Edit Unpublished Title Only", groups = "6. Edit Course Tests", priority = 31)
    public void editUnpublishedTitleOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(false);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setTitle(CourseDataGenerator.randomCourseName());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Unpublished Description Only", groups = "6. Edit Course Tests", priority = 32)
    public void editUnpublishedDescriptionOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(false);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Unpublished Duration Only", groups = "6. Edit Course Tests", priority = 33)
    public void editUnpublishedDurationOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(false);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDuration(CourseDataGenerator.validDuration());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Unpublished Level Only", groups = "6. Edit Course Tests", priority = 34)
    public void editUnpublishedLevelOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(false);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setLevel("Intermediate");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Unpublished Price Only", groups = "6. Edit Course Tests", priority = 35)
    public void editUnpublishedPriceOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(false);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setPrice(CourseDataGenerator.validPrice());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Unpublished Thumbnail URL Only", groups = "6. Edit Course Tests", priority = 36)
    public void editUnpublishedThumbnailUrlOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(false);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Unpublished Meeting Link Only", groups = "6. Edit Course Tests", priority = 37)
    public void editUnpublishedMeetingUrlOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(false);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setMeetingUrl(CourseDataGenerator.validTeamsLink());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Unpublished course to Published", groups = "6. Edit Course Tests", priority = 38)
    public void editUnpublishedToPublishedOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(false);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setPublished(true);

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }
    // </editor-fold>

    // <editor-fold desc="Edit Published Course">
    @Test(description = "Edit Published Title Only", groups = "6. Edit Course Tests", priority = 39)
    public void editPublishedTitleOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setTitle(CourseDataGenerator.randomCourseName());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Published Description Only", groups = "6. Edit Course Tests", priority = 40)
    public void editPublishedDescriptionOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDescription(CourseDataGenerator.randomDescription());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Published Duration Only", groups = "6. Edit Course Tests", priority = 41)
    public void editPublishedDurationOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDuration(CourseDataGenerator.validDuration());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Published Level Only", groups = "6. Edit Course Tests", priority = 42)
    public void editPublishedLevelOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setLevel("Intermediate");

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Published Price Only", groups = "6. Edit Course Tests", priority = 43)
    public void editPublishedPriceOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setPrice(CourseDataGenerator.validPrice());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Published Thumbnail URL Only", groups = "6. Edit Course Tests", priority = 44)
    public void editPublishedThumbnailUrlOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Published Meeting Link Only", groups = "6. Edit Course Tests", priority = 45)
    public void editPublishedMeetingUrlOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setMeetingUrl(CourseDataGenerator.validTeamsLink());

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }

    @Test(description = "Edit Published course to Unpublished", groups = "6. Edit Course Tests", priority = 46)
    public void editPublishedToUnpublishedOnly() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        if (courseElement == null) {
            return;
        }

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setPublished(false);

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");
    }
    // </editor-fold>
    // </editor-fold>

}
