package tests.admin.enrollment;

import factory.PageFactory;
import models.Course;
import org.testng.annotations.Test;
import pages.interfaces.IHomePage;
import pages.interfaces.INavigationBar;
import pages.interfaces.admin.ICourseManagementPage;
import pages.interfaces.admin.IEnrollmentsManagementPage;
import pages.interfaces.auth.ILoginPage;
import pages.interfaces.dashboard.IAdminDashboardPage;
import pages.interfaces.dashboard.IDashboardPage;
import tests.TestsBase;
import utils.ConfigManager;
import utils.CourseDataGenerator;

public class CourseEnrollmentTests extends TestsBase {

    // <editor-fold desc="Class Fields / Constants">
    protected IHomePage homePage;

    protected INavigationBar navigationBar;

    protected ILoginPage loginPage;

    protected IDashboardPage dashboardPage;

    protected IAdminDashboardPage adminDashboardPage;

    protected ICourseManagementPage courseManagementPage;

    protected IEnrollmentsManagementPage enrollmentsManagementPage;
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

        homePage.verifyHomePageIsDisplayed();

        navigationBar.goToLoginPage();

        loginPage.verifyLoginPageIsDisplayed();

        loginAsAdminAndVerify(loginPage, dashboardPage);

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
        cleanUpCourse(
                navigationBar,
                adminDashboardPage,
                courseManagementPage
        );
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Test(description = "Enroll user to Unpublished Course", groups = "5. Enrollment Tests")
    public void enrollUserToUnpublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(false);

        AddCourseAndEnrollUserToCourse(
                course,
                courseManagementPage,
                navigationBar,
                enrollmentsManagementPage,
                adminDashboardPage
        );
    }

    @Test(description = "Enroll user to Published Course", groups = "5. Enrollment Tests")
    public void enrollUserToPublishedCourse() {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(true);

        AddCourseAndEnrollUserToCourse(
                course,
                courseManagementPage,
                navigationBar,
                enrollmentsManagementPage,
                adminDashboardPage
        );
    }
    // </editor-fold>

}
