package pages.web;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pages.BasePage;
import pages.interfaces.INavigationBar;

import java.util.logging.Logger;

public class NavigationBarWeb extends BasePage implements INavigationBar {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(NavigationBarWeb.class.getName());

    private final By navBurgerBtn = By.xpath("//button[@class='nav-burger']");

    private final By loginBtn = By.xpath("//button[contains(@class, 'mobile-menu-item') and contains(., 'Login')]");

    private final By logoutBtn = By.xpath("//button[contains(@class, 'mobile-menu-item') and contains(., 'Logout')]");

    private final By adminPanelBtn = By.xpath(
            "//button[contains(@class, 'mobile-menu-item') and contains(., 'Admin Panel')]");

    private final By adminNavBurgerBtn = By.xpath("//button[@class='admin-burger-btn']");

    private final By overviewNavBtn = By.xpath("//nav//button[contains(text(), 'Overview')]");

    private final By coursesNavBtn = By.xpath("//nav//button[contains(text(), 'Courses')]");

    private final By enrollmentsNavBtn = By.xpath("//nav//button[contains(text(), 'Enrollments')]");

    private final By backToWebsiteBtn = By.xpath(
            "//div[contains(@class, 'admin-sidebar-footer')]//button[contains(., 'Back to Website')]");
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
        this.scrollToViewThenClickButton(adminNavBurgerBtn);
    }
    // </editor-fold>

}
