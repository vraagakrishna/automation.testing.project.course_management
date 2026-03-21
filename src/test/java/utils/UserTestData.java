package utils;

import com.github.javafaker.Faker;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class UserTestData {

    // <editor-fold desc="Class Fields / Constants">
    private static final Logger logger = Logger.getLogger(UserTestData.class.getName());

    private static final Faker faker = new Faker();

    private static final List<String> DOMAINS = List.of(
            "gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "icloud.com"
    );

    public static String weakPassword = faker.internet()
                                             .password(1, 5, true, true, true);

    private static String password = generateFakePassword();

    private static final String firstName = generateFakeFirstName();

    private static final String lastName = generateFakeLastName();

    private static final String email = generateFakeEmail();
    // </editor-fold>

    // <editor-fold desc="Getters and Setters">
    public static String getWeakPassword() {
        return weakPassword;
    }

    public static String getPassword() {
        return password;
    }

    public static String getFirstName() {
        return firstName;
    }


    public static String getLastName() {
        return lastName;
    }


    public static String getEmail() {
        return email;
    }
    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private static String sanitize(String input) {
        return input.replaceAll("[^A-Za-z0-9]", "");
    }

    private static String randomDomain() {
        int idx = ThreadLocalRandom.current()
                                   .nextInt(DOMAINS.size());
        return DOMAINS.get(idx);
    }

    private static String generateFakeFirstName() {
        return sanitize(faker.name()
                             .firstName());
    }

    private static String generateFakeLastName() {
        return sanitize(faker.name()
                             .lastName());
    }

    private static String generateFakeEmail() {
        return lastName + "." + firstName + "." + faker.number()
                                                       .numberBetween(0, 10000) + "@" + randomDomain();
    }

    private static String generateFakePassword() {
        String newPassword;

        do {
            newPassword = faker.internet()
                               .password(8, 16, true, true, true);
        } while (!newPassword.matches(".*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/].*") || newPassword.length() < 8);

        return newPassword;
    }

    private void generateNewPassword() {
        password = generateFakePassword();
    }
    // </editor-fold>

}
