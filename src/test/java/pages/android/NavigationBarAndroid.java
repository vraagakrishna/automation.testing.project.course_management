package pages.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import pages.interfaces.INavigationBar;

import java.util.logging.Logger;

public class NavigationBarAndroid extends BasePageAndroid implements INavigationBar {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(HomePageAndroid.class.getName());

    private final By navBurgerBtn = By.xpath("//android.widget.Button");

    private final By loginBtn = AppiumBy.androidUIAutomator("new UiScrollable(" +
            "new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"Login / Sign Up\")" +
            ")");

    private final By logoutBtn = By.xpath("//android.widget.Button[@content-desc=\"Logout\"]");

    private final By adminPanelBtn = By.xpath("//android.widget.Button[@content-desc=\"Admin Panel\"]");

    private final By adminNavBurgerBtn = AppiumBy.androidUIAutomator("new UiSelector().clickable(true).instance(0)");

    private final By overviewNavBtn = By.xpath("//android.widget.Button[@content-desc=\"Overview\"]");

    private final By coursesNavBtn = By.xpath("//android.widget.Button[@content-desc=\"Courses\"]");

    private final By enrollmentsNavBtn = By.xpath("//android.widget.Button[@content-desc=\"Enrollments\"]");

    private final By backToWebsiteBtn = By.xpath("//android.widget.Button[@content-desc=\"Back to Home\"]");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public NavigationBarAndroid(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Overrides">
    @Override
    public void goToLoginPage() {
        this.clickNavBurger();
        this.clickButton(loginBtn);
    }

    @Override
    public void logout() {
        logger.info("Logging out...");
        this.clickNavBurger();

        int attempts = 0;
        int maxAttempts = 5;

        while (attempts < maxAttempts) {
            try {
                this.clickButton(logoutBtn);
                break;
            } catch (NoSuchElementException | TimeoutException ex) {
                this.clickNavBurger();
            } finally {
                attempts++;
            }
        }
    }

    @Override
    public void goToAdminPanel() {
        this.clickNavBurger();
        this.clickButton(adminPanelBtn);
    }

    @Override
    public void clickBackToWebsiteBtn() {
        this.clickAdminNavBurger();
        this.clickButton(backToWebsiteBtn);
    }

    @Override
    public void clickCoursesBtn() {
        this.clickAdminNavBurger();
        this.clickButton(coursesNavBtn);
    }

    @Override
    public void clickOverviewBtn() {
        this.clickAdminNavBurger();
        this.clickButton(overviewNavBtn);
    }

    @Override
    public void clickEnrollmentsBtn() {
        this.clickAdminNavBurger();
        this.clickButton(enrollmentsNavBtn);
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private void clickNavBurger() {
        this.clickButton(navBurgerBtn);
    }

    private void clickAdminNavBurger() {
        this.clickButton(adminNavBurgerBtn);
    }
    // </editor-fold>

}
