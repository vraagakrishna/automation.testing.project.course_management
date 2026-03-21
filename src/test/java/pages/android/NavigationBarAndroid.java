package pages.android;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pages.BasePage;
import pages.interfaces.NavigationBar;

import java.util.logging.Logger;

public class NavigationBarAndroid extends BasePage implements NavigationBar {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(HomePageAndroid.class.getName());

    private final By navBurgerBtn = By.xpath("//android.widget.Button");

    private final By loginBtn = By.xpath("//android.widget.Button[@content-desc=\"Login / Sign Up\"]");

    private final By logoutBtn = By.xpath("//android.widget.Button[@content-desc=\"Logout\"]");

    private final By adminPanelBtn = By.xpath("//android.widget.Button[@content-desc=\"Admin Panel\"]");
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
        this.clickButton(logoutBtn);
    }

    @Override
    public void goToAdminPanel() {
        this.clickNavBurger();
        this.clickButton(adminPanelBtn);
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private void clickNavBurger() {
        this.clickButton(navBurgerBtn);
    }
    // </editor-fold>

}
