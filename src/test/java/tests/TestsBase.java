package tests;

import factory.DriverFactory;
import factory.PageFactory;
import io.appium.java_client.AppiumDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import pages.interfaces.HomePage;
import pages.interfaces.NavigationBar;
import pages.interfaces.admin.CourseManagementPage;
import pages.interfaces.auth.LoginPage;
import pages.interfaces.dashboard.AdminDashboardPage;
import pages.interfaces.dashboard.DashboardPage;
import services.AppiumServiceManager;
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

    protected HomePage homePage;

    protected NavigationBar navigationBar;

    protected LoginPage loginPage;

    protected DashboardPage dashboardPage;

    protected AdminDashboardPage adminDashboardPage;

    protected CourseManagementPage courseManagementPage;
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @BeforeSuite
    public void setupSuite() {
        LoggingManager.configureLogging();
    }

    @BeforeMethod
    public void setUp(Method method, ITestResult result) throws MalformedURLException {
        ReportManager.startTest(result);

        logger.info("Setting up the driver");

        driver = DriverFactory.initDriver();

        DriverFactory.setDriver(driver);

        this.homePage = PageFactory.getHomePage(driver);
        this.navigationBar = PageFactory.getNavigationBar(driver);
        this.loginPage = PageFactory.getLoginPage(driver);
        this.dashboardPage = PageFactory.getDashboardPage(driver);
        this.adminDashboardPage = PageFactory.getAdminDashboardPage(driver);
        this.courseManagementPage = PageFactory.getCourseManagementPage(driver);

        SoftAssertManager.getSoftAssert();

        this.setUpPage();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult result) {
        logger.info("Tearing down...");

        try {
            SoftAssertManager.getSoftAssert();
        } catch (AssertionError ex) {
            Assert.fail(ex.getMessage());
        } finally {
            SoftAssertManager.remove();
        }

        if (driver != null) {
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
    // </editor-fold>

}
