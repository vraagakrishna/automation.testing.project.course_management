package utils;

import com.github.javafaker.Faker;

public class CourseDataGenerator {

    private static final Faker faker = new Faker();

    public static String randomCourseName() {
        return faker.educator()
                    .course() +
                " at " +
                faker.educator()
                     .university();
    }

    public static String longCourseName() {
        return faker.lorem()
                    .characters(300);
    }

    public static String randomDescription() {
        return faker.lorem()
                    .paragraph();
    }

    public static String longDescription() {
        return faker.lorem()
                    .characters(5000);
    }

    public static String randomDuration() {
        return faker.lorem()
                    .characters(20);
    }

    public static String validDuration() {
        return faker.number()
                    .numberBetween(1, 100) + " hours";
    }

    public static long largePrice() {
        return faker.number()
                    .randomNumber(8, true);
    }

    public static String validThumbnailUrl() {
        return faker.internet()
                    .image();
    }

    public static String invalidUrl() {
        return faker.lorem()
                    .word();
    }

    public static String validTeamsLink() {
        return "https://teams.microsoft.com/l/meetup-join/" +
                faker.internet()
                     .uuid();
    }

}
