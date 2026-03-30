package tests.admin.enrollment;

import models.Course;
import org.testng.annotations.Test;
import tests.TestsBase;
import utils.ConfigManager;
import utils.CourseDataGenerator;

public class CourseEnrollmentTests extends TestsBase {

    @Override
    protected void setUpPage() {
        homePage.verifyHomePageIsDisplayed();

        navigationBar.goToLoginPage();

        loginPage.verifyLoginPageIsDisplayed();

        loginPage.loginUser(ConfigManager.getAdminEmail(), ConfigManager.getAdminPassword());

        dashboardPage.verifyDashboardPageIsDisplayed();

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

    @Test(description = "Enroll user to Unpublished Course", groups = "2. Enrollment Tests")
    public void enrollUserToUnpublishedCourse() {
        enrollUserToCourse(false);
    }

    @Test(description = "Enroll user to Published Course", groups = "2. Enrollment Tests")
    public void enrollUserToPublishedCourse() {
        enrollUserToCourse(true);
    }

    private void enrollUserToCourse(boolean publish) {
        Course course = new Course();
        course.setTitle(CourseDataGenerator.randomCourseName());
        course.setDescription(CourseDataGenerator.randomDescription());
        course.setPublished(publish);
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        navigationBar.clickEnrollmentsBtn();

        enrollmentsManagementPage.clickEnroll(
                course.getTitle(),
                ConfigManager.getUserEmail(),
                "Enrolling user to " + (publish ? "Published" : "Unpublished") + " course",
                publish
        );

        enrollmentsManagementPage.searchForEnrollment(
                course.getTitle(),
                ConfigManager.getUserEmail(),
                publish
        );
    }

}
