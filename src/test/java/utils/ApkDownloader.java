package utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class ApkDownloader {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(ApkDownloader.class.getName());
    // </editor-fold>

    // <editor-fold desc="Public Methods">
    public static void downloadApk(String apkUrl, String destination) {
        File apkFile = new File(destination);

        try {
            // Create parent directories if they don't exist
            File parentDir = apkFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (apkFile.exists() && apkFile.length() > 1_000_000) {
                logger.info("Valid APK already exists. Skipping download.");
                return;
            }

            logger.info("Downloading APK...");
            try (InputStream in = new URL(apkUrl).openStream()) {
                Files.copy(in, apkFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            logger.info("Validating APK size...");
            long fileSize = apkFile.length();

            if (fileSize < 1_000_000) {  // 1 MB threshold
                throw new RuntimeException(
                        "Downloaded APK appears invalid. File size is only " + fileSize + " bytes."
                );
            }

            logger.info("APK downloaded successfully. Size: " + fileSize + " bytes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // </editor-fold>

}
