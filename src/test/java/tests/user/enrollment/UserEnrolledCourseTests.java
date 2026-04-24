package tests.user.enrollment;

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
import utils.ConfigManager;
import utils.CourseDataGenerator;
import utils.ScreenshotUtils;
import utils.SoftAssertManager;

public class UserEnrolledCourseTests extends TestsBase {

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

        // START: clean up to remove all courses from user
        navigationBar.clickEnrollmentsBtn();

        enrollmentsManagementPage.completeAllCourses(ConfigManager.getUserEmail());

        navigationBar.clickOverviewBtn();
        // END: clean up to remove all courses from user

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
    @Test(description = "User sees Enrolled Course", groups = "User Enrolled Course Tests")
    public void userSeesEnrolledCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setDuration(CourseDataGenerator.validDuration());
        course.setLevel("Beginner");
        course.setPrice(CourseDataGenerator.validPrice());
        course.setThumbnailUrl(CourseDataGenerator.validThumbnailUrl());
        course.setMeetingUrl(CourseDataGenerator.validTeamsLink());
        course.setPublished(true);

        enrollUserToCourseAndDoCourseVisibilityValidation(course);
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

    private void enrollUserToCourseAndDoCourseVisibilityValidation(Course course) {
        AddCourseAndEnrollUserToCourse(
                course,
                courseManagementPage,
                navigationBar,
                enrollmentsManagementPage,
                adminDashboardPage
        );

        courseVisibilityValidation(course);
    }

    private void courseVisibilityValidation(Course course) {
        logOutAdminFromAdminDashboard();

        navigationBar.goToLoginPage();

        loginPage.verifyLoginPageIsDisplayed();

        loginAsUserAndVerify(loginPage, dashboardPage);

        // START: Verify Enrolled for Course
        userDashboardPage.validateEnrolledCourse(course);
        // END: Verify Enrolled for Course

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
                coursePage.validateCourseDetails(course, userCourseElement, true);
            }
        }
        // END: Verify Course is displayed

        logOut();
    }
    // </editor-fold>

}
