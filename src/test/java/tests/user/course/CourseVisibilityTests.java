package tests.user.course;

import factory.PageFactory;
import models.Course;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import pages.interfaces.IHomePage;
import pages.interfaces.INavigationBar;
import pages.interfaces.admin.ICourseManagementPage;
import pages.interfaces.admin.IEnrollmentsManagementPage;
import pages.interfaces.auth.ILoginPage;
import pages.interfaces.dashboard.IAdminDashboardPage;
import pages.interfaces.dashboard.IDashboardPage;
import pages.interfaces.dashboard.IUserDashboardPage;
import pages.interfaces.user.ICoursePage;
import tests.TestsBase;
import utils.CourseDataGenerator;
import utils.ScreenshotUtils;
import utils.SoftAssertManager;

public class CourseVisibilityTests extends TestsBase {

    // <editor-fold desc="Class Fields / Constants">
    protected IHomePage homePage;

    protected INavigationBar navigationBar;

    protected ILoginPage loginPage;

    protected IDashboardPage dashboardPage;

    protected IAdminDashboardPage adminDashboardPage;

    protected ICourseManagementPage courseManagementPage;

    protected IEnrollmentsManagementPage enrollmentsManagementPage;

    protected IUserDashboardPage userDashboardPage;

    protected ICoursePage coursePage;
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
        this.enrollmentsManagementPage = PageFactory.getEnrollmentsManagementPage(driver);
        this.userDashboardPage = PageFactory.getUserDashboardPage(driver);
        this.coursePage = PageFactory.getCoursePage(driver);

        homePage.verifyHomePageIsDisplayed();

        navigateToLoginPageAndLoginAsAdmin();

        navigationBar.goToAdminPanel();

        adminDashboardPage.verifyAdminDashboardIsDisplayed();

        adminDashboardPage.navigateToManageCourses();

        courseManagementPage.verifyCourseManagementPageIsDisplayed();

        courseManagementPage.clickAddCourseBtn();

        courseManagementPage.verifyBlankCourseFormIsDisplayed();
    }

    @Override
    protected void cleanUpPage() {
        navigateToLoginPageAndLoginAsAdmin();

        navigationBar.goToAdminPanel();

        adminDashboardPage.navigateToManageCourses();

        courseManagementPage.verifyCourseManagementPageIsDisplayed();

        cleanUpCourse(
                navigationBar,
                adminDashboardPage,
                courseManagementPage
        );
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Test(description = "User does not see Unpublished Course", groups = "7. User Course Visibility Tests")
    public void userDoesNotSeeUnpublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(false);

        addCourseAndDoCourseVisibilityValidation(course);
    }

    @Test(description = "User sees Published Course", groups = "7. User Course Visibility Tests", priority = 1)
    public void userSeesPublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        addCourseAndDoCourseVisibilityValidation(course);
    }

    @Test(description = "User sees Published Course with Duration", groups = "7. User Course Visibility Tests", priority = 2)
    public void userSeesPublishedCourseWithDuration() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setPublished(true);

        addCourseAndDoCourseVisibilityValidation(course);
    }

    @Test(description = "User sees Published Course with Level Intermediate", groups = "7. User Course Visibility Tests", priority = 3)
    public void userSeesPublishedCourseWithLevelIntermediate() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setLevel("Intermediate");
        course.setPublished(true);

        addCourseAndDoCourseVisibilityValidation(course);
    }

    @Test(description = "User sees Published Course with Level Advanced", groups = "7. User Course Visibility Tests", priority = 4)
    public void userSeesPublishedCourseWithLevelAdvanced() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setLevel("Advanced");
        course.setPublished(true);

        addCourseAndDoCourseVisibilityValidation(course);
    }

    @Test(description = "User sees Published Course with Price", groups = "7. User Course Visibility Tests", priority = 5)
    public void userSeesPublishedCourseWithPrice() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPrice(CourseDataGenerator.validPrice());
        course.setPublished(true);

        addCourseAndDoCourseVisibilityValidation(course);
    }

    @Test(description = "User sees Published Course with Thumbnail", groups = "7. User Course Visibility Tests", priority = 6)
    public void userSeesPublishedCourseWithThumbnail() {
        String url = CourseDataGenerator.validThumbnailUrl();

        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setThumbnailUrl(url);
        course.setPublished(true);

        addCourseAndDoCourseVisibilityValidation(course);
    }

    @Test(description = "User sees edited Title for Published Course", groups = "7. User Course Visibility Tests", priority = 7)
    public void userSeesEditedTitleForPublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        addCourseAndDoCourseVisibilityValidation(course);

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setTitle(CourseDataGenerator.randomCourseName());

        editCourseAndDoCourseVisibilityValidation(course, editedCourse);
    }

    @Test(description = "User sees edited Description for Published Course", groups = "7. User Course Visibility Tests", priority = 8)
    public void userSeesEditedDescriptionForPublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        addCourseAndDoCourseVisibilityValidation(course);

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setDescription(CourseDataGenerator.randomDescription());

        editCourseAndDoCourseVisibilityValidation(course, editedCourse);
    }

    @Test(description = "User sees edited Level for Published Course", groups = "7. User Course Visibility Tests", priority = 9)
    public void userSeesEditedLevelForPublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        addCourseAndDoCourseVisibilityValidation(course);

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setLevel("Intermediate");

        editCourseAndDoCourseVisibilityValidation(course, editedCourse);
    }

    @Test(description = "User sees edited Price for Published Course", groups = "7. User Course Visibility Tests", priority = 10)
    public void userSeesEditedPriceForPublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        addCourseAndDoCourseVisibilityValidation(course);

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setPrice(CourseDataGenerator.validPrice());

        editCourseAndDoCourseVisibilityValidation(course, editedCourse);
    }

    @Test(description = "User sees edited Thumbnail URL for Published Course", groups = "7. User Course Visibility Tests", priority = 11)
    public void userSeesEditedThumbnailUrlForPublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        addCourseAndDoCourseVisibilityValidation(course);

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());

        editCourseAndDoCourseVisibilityValidation(course, editedCourse);
    }

    @Test(description = "User does not see edited Unpublished Course", groups = "7. User Course Visibility Tests", priority = 12)
    public void userDoesNotSeeEditedUnpublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        addCourseAndDoCourseVisibilityValidation(course);

        // Edit course
        Course editedCourse = new Course(course);
        editedCourse.setPublished(false);

        editCourseAndDoCourseVisibilityValidation(course, editedCourse);
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private void logOutAdminFromAdminDashboard() {
        navigationBar.clickBackToWebsiteBtn();

        int attempts = 0;
        int maxAttempts = 5;

        while (attempts < maxAttempts) {
            try {
                dashboardPage.verifyDashboardPageIsDisplayed();

                logOut();
                break;
            } catch (NoSuchElementException | TimeoutException ex) {
                SoftAssertManager.getSoftAssert()
                                 .assertTrue(false, "Did not navigate to Dashboard after clicking on 'Back to Home'");
                ScreenshotUtils.captureAndAttach(
                        driver,
                        "Did not navigate to Dashboard after clicking on 'Back to Home'"
                );
                navigationBar.clickBackToWebsiteBtn();
            } finally {
                attempts++;
            }
        }
    }

    private void logOut() {
        navigationBar.logout();

        homePage.verifyHomePageIsDisplayed();
    }

    private void navigateToLoginPageAndLoginAsAdmin() {
        navigationBar.goToLoginPage();

        loginPage.verifyLoginPageIsDisplayed();

        loginAsAdminAndVerify(loginPage, dashboardPage);
    }

    private void addCourseAndDoCourseVisibilityValidation(Course course) {
        WebElement courseElement = addCourseAndGetCourse(
                course,
                courseManagementPage,
                navigationBar,
                adminDashboardPage
        );

        if (courseElement == null) {
            return;
        }

        courseVisibilityValidation(course);
    }

    private void editCourseAndDoCourseVisibilityValidation(Course course, Course editedCourse) {
        navigateToLoginPageAndLoginAsAdmin();

        navigationBar.goToAdminPanel();

        adminDashboardPage.verifyAdminDashboardIsDisplayed();

        adminDashboardPage.navigateToManageCourses();

        courseManagementPage.verifyCourseManagementPageIsDisplayed();

        WebElement courseElement = editCourseAndGetCourse(
                course,
                editedCourse,
                courseManagementPage,
                navigationBar,
                adminDashboardPage
        );

        if (courseElement == null) {
            return;
        }

        courseVisibilityValidation(editedCourse);
    }

    private void courseVisibilityValidation(Course course) {
        logOutAdminFromAdminDashboard();

        navigationBar.goToLoginPage();

        loginPage.verifyLoginPageIsDisplayed();

        loginAsUserAndVerify(loginPage, dashboardPage);

        // START: Verify Course is displayed
        userDashboardPage.clickViewAllCourses();

        WebElement userCourseElement = coursePage.findCourse(course);

        if (userCourseElement == null) {
            // course is published and does not exist
            if (course.isPublished()) {
                SoftAssertManager.getSoftAssert()
                                 .assertTrue(false, "User cannot see Published Course: " + course.getTitle());
            }
        } else {
            // course is unpublished and does exist
            if (!course.isPublished()) {
                SoftAssertManager.getSoftAssert()
                                 .assertTrue(false, "User can see Unpublished Course: " + course.getTitle());
            }

            // course exists and published
            else {
                coursePage.validateCourseDetails(course, userCourseElement, false);
            }
        }
        // END: Verify Course is displayed

        logOut();
    }
    // </editor-fold>

}
