package pages.interfaces.admin;

public interface IEnrollmentsManagementPage {

    void completeAllCourses(String userEmail);

    void clickEnroll(String courseName, String userEmail, String enrollmentNotes, boolean shouldWork);

    void searchForEnrollment(String courseName, String userEmail, boolean shouldExist);

}
