package tests;

import factory.DriverFactory;
import factory.PageFactory;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;
import pages.interfaces.DashboardPage;
import pages.interfaces.HomePage;
import pages.interfaces.LoginPage;
import pages.interfaces.NavigationBar;
import utils.SoftAssertManager;

import java.net.MalformedURLException;
import java.util.logging.Logger;

public class TestsBase {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(TestsBase.class.getName());

    protected final SoftAssert softAssert = SoftAssertManager.getSoftAssert();

    protected AppiumDriver driver;

    protected HomePage homePage;

    protected NavigationBar navigationBar;

    protected LoginPage loginPage;

    protected DashboardPage dashboardPage;
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public TestsBase() {
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @BeforeClass
    public void setUp() throws MalformedURLException {
        logger.info("Setting up the driver");

        DriverFactory.initDriver();

        driver = DriverFactory.getDriver();

        this.homePage = PageFactory.getHomePage(driver);
        this.navigationBar = PageFactory.getNavigationBar(driver);
        this.loginPage = PageFactory.getLoginPage(driver);
        this.dashboardPage = PageFactory.getDashboardPage(driver);

        this.setUpPage();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest() {
        logger.info("Tearing down test...");

        SoftAssert softAssert = SoftAssertManager.getSoftAssert();

        try {
            softAssert.assertAll();
        } finally {
            SoftAssertManager.remove();
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        logger.info("Tearing down...");

        if (driver != null) {
            DriverFactory.quitDriver();
            driver = null;
        }
    }
    // </editor-fold>

    // <editor-fold desc="Protected Methods">
    protected void setUpPage() {
        // the inherited classes will implement this, if necessary
    }
    // </editor-fold>

}
