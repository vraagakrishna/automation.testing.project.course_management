package pages.web.dashboard;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import pages.interfaces.dashboard.IUserDashboardPage;
import pages.web.BasePageWeb;

import java.util.logging.Logger;

public class UserDashboardPageWeb extends BasePageWeb implements IUserDashboardPage {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(UserDashboardPageWeb.class.getName());

    private final By viewAllCoursesBtn = By.xpath("//button[contains(., 'View All')]");
    // </editor-fold>

    // <editor-fold desc="Ctor">
    public UserDashboardPageWeb(AppiumDriver driver) {
        super(driver);
    }
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    @Override
    public void clickViewAllCourses() {
        logger.info("Clicking View All courses button");
        clickButton(viewAllCoursesBtn);
    }
    // </editor-fold>

}
