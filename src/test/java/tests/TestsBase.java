package tests;

import factory.DriverFactory;
import io.appium.java_client.AppiumDriver;
import models.Course;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import pages.interfaces.INavigationBar;
import pages.interfaces.admin.ICourseManagementPage;
import pages.interfaces.admin.IEnrollmentsManagementPage;
import pages.interfaces.auth.ILoginPage;
import pages.interfaces.dashboard.IAdminDashboardPage;
import pages.interfaces.dashboard.IDashboardPage;
import services.AppiumServiceManager;
import utils.ConfigManager;
import utils.LoggingManager;
import utils.ReportManager;
import utils.SoftAssertManager;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.logging.Logger;

public class TestsBase {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(TestsBase.class.getName());

    protected AppiumDriver driver;
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @BeforeSuite
    public void setupSuite() {
        LoggingManager.configureLogging();
    }

    @BeforeMethod
    public void setUp(Method method, ITestResult result) throws MalformedURLException {
        paceTest();

        ReportManager.startTest(result);

        logger.info("Setting up the driver");

        driver = DriverFactory.initDriver();

        DriverFactory.setDriver(driver);

        SoftAssertManager.getSoftAssert();

        ConfigManager.courses.clear();
        this.setUpPage();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult result) {
        cleanUpPage();

        logger.info("Tearing down...");

        if (driver != null) {
            logger.info("Quitting driver...");
            DriverFactory.quitDriver();
            driver = null;
        }
    }

    @AfterSuite
    public void stopAppium() {
        AppiumServiceManager.stopService();
    }
    // </editor-fold>

    // <editor-fold desc="Protected Methods">
    protected void setUpPage() {
        // the inherited classes will implement this, if necessary
    }

    protected void cleanUpPage() {
        // the inherited classes will implement this, if necessary
    }

    protected void loginAsAdminAndVerify(
            ILoginPage loginPage,
            IDashboardPage dashboardPage
    ) {
        loginPage.loginUser(ConfigManager.getAdminEmail(), ConfigManager.getAdminPassword());

        dashboardPage.verifyDashboardPageIsDisplayed();
    }

    protected void loginAsUserAndVerify(
            ILoginPage loginPage,
            IDashboardPage dashboardPage
    ) {
        loginPage.loginUser(ConfigManager.getUserEmail(), ConfigManager.getUserPassword());

        dashboardPage.verifyDashboardPageIsDisplayed();
    }

    protected void cleanUpCourse(
            INavigationBar navigationBar,
            IAdminDashboardPage adminDashboardPage,
            ICourseManagementPage courseManagementPage
    ) {
        ConfigManager.courses
                .stream()
                .filter(course -> course.getTitle() != null || !course.getTitle()
                                                                      .isEmpty())
                .forEach(course -> {
                    logger.info("Cleaning up course: " + course);

                    navigationBar.clickOverviewBtn();

                    adminDashboardPage.navigateToManageCourses();

                    courseManagementPage.verifyCourseManagementPageIsDisplayed();

                    WebElement courseElement = courseManagementPage.validateCourseIsDisplayedAndNoAssertion(course);

                    if (courseElement == null) {
                        logger.info("Course does not exist; nothing to clean up");
                    } else {
                        courseManagementPage.deleteCourse(courseElement);
                    }
                });

        ConfigManager.courses.clear();
    }

    protected WebElement getCourse(
            Course course,
            ICourseManagementPage courseManagementPage,
            INavigationBar navigationBar,
            IAdminDashboardPage adminDashboardPage
    ) {
        WebElement courseElement = courseManagementPage.validateCourseIsDisplayed(course);

        if (courseElement == null) {
            navigationBar.clickOverviewBtn();

            adminDashboardPage.navigateToManageCourses();

            courseManagementPage.verifyCourseManagementPageIsDisplayed();

            courseElement = courseManagementPage.validateCourseIsDisplayed(course);
        }

        return courseElement;
    }

    protected WebElement addCourseAndGetCourse(
            Course course,
            ICourseManagementPage courseManagementPage,
            INavigationBar navigationBar,
            IAdminDashboardPage adminDashboardPage
    ) {
        courseManagementPage.addCourse(course);

        courseManagementPage.verifyAlertMessage("created");

        return getCourse(
                course,
                courseManagementPage,
                navigationBar,
                adminDashboardPage
        );
    }

    protected WebElement editCourseAndGetCourse(
            Course course,
            Course editedCourse,
            ICourseManagementPage courseManagementPage,
            INavigationBar navigationBar,
            IAdminDashboardPage adminDashboardPage
    ) {
        WebElement courseElement = getCourse(
                course,
                courseManagementPage,
                navigationBar,
                adminDashboardPage
        );

        courseManagementPage.editCourse(courseElement, editedCourse);

        courseManagementPage.verifyAlertMessage("updated");

        return getCourse(
                editedCourse,
                courseManagementPage,
                navigationBar,
                adminDashboardPage
        );
    }

    protected void AddCourseAndEnrollUserToCourse(
            Course course,
            ICourseManagementPage courseManagementPage,
            INavigationBar navigationBar,
            IEnrollmentsManagementPage enrollmentsManagementPage,
            IAdminDashboardPage adminDashboardPage
    ) {
        WebElement courseElement = addCourseAndGetCourse(
                course,
                courseManagementPage,
                navigationBar,
                adminDashboardPage
        );

        if (courseElement == null) {
            return;
        }

        navigationBar.clickEnrollmentsBtn();

        enrollmentsManagementPage.clickEnroll(
                course.getTitle(),
                ConfigManager.getUserEmail(),
                "Enrolling user to " + (course.isPublished() ? "Published" : "Unpublished") + " course",
                course.isPublished()
        );

        enrollmentsManagementPage.searchForEnrollment(
                course.getTitle(),
                ConfigManager.getUserEmail(),
                course.isPublished()
        );
    }

    private void paceTest() {
        // A 5-10 second pause before every single test
        // gives the Cloudflare WAF time to "forget" the previous session
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // </editor-fold>

}
