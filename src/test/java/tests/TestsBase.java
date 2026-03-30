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
import pages.interfaces.IHomePage;
import pages.interfaces.INavigationBar;
import pages.interfaces.admin.ICourseManagementPage;
import pages.interfaces.admin.IEnrollmentsManagementPage;
import pages.interfaces.auth.ILoginPage;
import pages.interfaces.dashboard.IAdminDashboardPage;
import pages.interfaces.dashboard.IDashboardPage;
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

    protected IHomePage homePage;

    protected INavigationBar navigationBar;

    protected ILoginPage loginPage;

    protected IDashboardPage dashboardPage;

    protected IAdminDashboardPage adminDashboardPage;

    protected ICourseManagementPage courseManagementPage;

    protected IEnrollmentsManagementPage enrollmentsManagementPage;
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
        this.enrollmentsManagementPage = PageFactory.getEnrollmentsManagementPage(driver);

        SoftAssertManager.getSoftAssert();

        this.setUpPage();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult result) {
        logger.info("Tearing down...");

        AssertionError softAssertError = null;

        try {
            SoftAssertManager.getSoftAssert()
                             .assertAll();
        } catch (AssertionError ex) {
            softAssertError = ex;  // store it instead of failing immediately
        } finally {
            SoftAssertManager.remove();
        }

        if (driver != null) {
            logger.info("Quitting driver...");
            DriverFactory.quitDriver();
            driver = null;
        }

        // Fail AFTER cleanup
        if (softAssertError != null)
            Assert.fail(softAssertError.getMessage());
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
