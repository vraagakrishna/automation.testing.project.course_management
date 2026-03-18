package tests;

import factory.DriverFactory;
import factory.PageFactory;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pages.interfaces.HomePage;

import java.net.MalformedURLException;
import java.util.logging.Logger;

public class TestsBase {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(TestsBase.class.getName());

    protected AppiumDriver driver;

    protected HomePage homePage;
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
    }

    @AfterClass
    public void tearDown() {
        logger.info("Tearing down...");

        if (driver != null) {
            DriverFactory.quitDriver();
            driver = null;
        }
    }
    // </editor-fold>

}
