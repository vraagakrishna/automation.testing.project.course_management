package services;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import utils.ConfigManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class AppiumServiceManager {

    private static final Logger logger = Logger.getLogger(AppiumServiceManager.class.getName());

    private static final String LOGS_DIR = System.getProperty("user.dir") + File.separator +
            "Reports" + File.separator + "Logs";

    private static AppiumDriverLocalService service;

    public static String startServiceIfRequired() {
        String externalUrl = ConfigManager.getAppiumServerUrl();

        // if env var exists, use external Appium
        if (externalUrl != null && !externalUrl.isEmpty()) {
            logger.info("Using existing Appium server: " + externalUrl);
            return externalUrl;
        }

        logger.info("Starting local Appium service...");
        if (service == null || !service.isRunning()) {
            // Create only the parent directory
            File logDir = new File(LOGS_DIR);
            if (!logDir.exists()) logDir.mkdirs();

            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String destinationPath = LOGS_DIR + File.separator + "appium_" + timestamp + ".log";

            service = new AppiumServiceBuilder()
                    .usingAnyFreePort()
                    .withArgument(GeneralServerFlag.ALLOW_INSECURE, "*:chromedriver_autodownload")
                    .withLogFile(new File(destinationPath))
                    .withArgument(GeneralServerFlag.LOG_LEVEL, "info:debug")
                    .withArgument(GeneralServerFlag.LOG_TIMESTAMP)
                    .build();
            service.start();

            logger.info("Appium logs will be written to: " + destinationPath);
        }

        String url = service.getUrl()
                            .toString();

        logger.info("Local Appium started at: " + url);

        return url;
    }

    public static void stopService() {
        String externalUrl = ConfigManager.getAppiumServerUrl();

        // if using external server, do NOT stop
        if (externalUrl != null && !externalUrl.isEmpty()) {
            logger.info("Using existing Appium server: " + externalUrl);
            return;
        }

        if (service != null && service.isRunning()) {
            logger.info("Stopping local Appium service...");
            service.stop();
            service = null;
        }
    }

}
