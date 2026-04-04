package tests;

import factory.DriverFactory;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
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

        SoftAssertManager.getSoftAssert();

        this.setUpPage();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult result) {
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
    // </editor-fold>

}
