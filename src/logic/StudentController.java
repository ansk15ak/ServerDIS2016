package logic;

import service.DBWrapper;
import shared.ReviewDTO;
import shared.StudentDTO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Klassen indeholder logikken bag den studerendes funktioner.
 */
public class StudentController extends UserController {

    private StudentDTO currentStudent;

    /**
     * StudentController har nedarvet fra UserController dvs. den har adgang til de samme metoder.
     */
    public StudentController() {
        super();
    }

    /**
     * Indlæs den studerende
     * @param currentStudent den aktuelle studerende
     */
    public void loadStudent(StudentDTO currentStudent) {
        this.currentStudent = currentStudent;
    }

    /**
     * Denne metode giver mulighed for at tilføje et review til en lektion.
     * @param review Det ønskede review
     * @return hvorvidt reviewet er tlføjet eller ej
     */
    public boolean addReview(ReviewDTO review) {

        boolean isAdded = true;

        try {
            Map<String, String> values = new HashMap();
            values.put("user_id", String.valueOf(review.getUserId()));
            values.put("lecture_id", String.valueOf(review.getLectureId()));
            values.put("rating", String.valueOf(review.getRating()));
            values.put("comment", review.getComment());
            values.put("is_deleted", "0");

            // Indsætter review i DB
            DBWrapper.insertIntoRecords("review", values);

        } catch (SQLException e) {
            e.printStackTrace();
            isAdded = false;
        }
        return isAdded;
    }

    /**
     * Denne metode giver mulighed for at slette et review.
     * @param reviewId Det review der ønskes slettet.
     * @return hvorvidt reviewet er slettet eller ej
     */
    public boolean softDeleteReview(int reviewId) {

        boolean isSoftDeleted = true;

        try {
            Map<String, String> isDeleted = new HashMap();
            isDeleted.put("is_deleted", "1");
            Map<String, String> params = new HashMap();
            params.put("id", String.valueOf(reviewId));

            // Opdater review i DB (soft delete)
            DBWrapper.updateRecords("review", isDeleted, params);

        } catch (SQLException e) {
            e.printStackTrace();
            isSoftDeleted = false;
        }
        return isSoftDeleted;
    }
}