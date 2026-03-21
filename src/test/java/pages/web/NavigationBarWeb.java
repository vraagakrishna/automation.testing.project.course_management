package pages.web;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pages.BasePage;
import pages.interfaces.NavigationBar;

import java.util.logging.Logger;

public class NavigationBarWeb extends BasePage implements NavigationBar {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(NavigationBarWeb.class.getName());

    private final By navBurgerBtn = By.xpath("//button[@class='nav-burger']");

    private final By loginBtn = By.xpath("//button[contains(@class, 'mobile-menu-item') and contains(., 'Login')]");

    private final By logoutBtn = By.xpath("//button[contains(@class, 'mobile-menu-item') and contains(., 'Logout')]");

    private final By adminPanelBtn = By.xpath(
            "//button[contains(@class, 'mobile-menu-item') and contains(., 'Admin Panel')]");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public NavigationBarWeb(AppiumDriver driver) {
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

        this.alertUtils.verifyIfConfirmationAlertMessageIsCorrect(
                "Are you sure you want to logout?",
                true
        );
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
