package logic;

import service.DBWrapper;
import shared.LectureDTO;
import shared.ReviewDTO;
import shared.TeacherDTO;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Klassen indeholder logikken bag lærerens funktioner.
 */
public class TeacherController extends UserController {

    private TeacherDTO currentTeacher;

    /**
     * TeacherController har nedarvet fra UserController dvs. den har adgang til de samme metoder.
     */
    public TeacherController() {
        super();
    }

    /**
     * Indlæs læreren
     * @param currentTeacher den aktuelle lærer
     */
    public void loadTeacher(TeacherDTO currentTeacher) {
        this.currentTeacher = currentTeacher;
    }

    /**
     * Denne metode beregner review gennemsnit for en lektion.
     * @param lectureId Den ønskede lektion
     * @return
     */
    public double calculateAverageRatingOnLecture(int lectureId) {

        getReviews(lectureId);

        int numberOfReviews = getReviews(lectureId).size();
        int sumOfRatings = 0;

        for (ReviewDTO review : getReviews(lectureId)) {
            sumOfRatings = sumOfRatings + review.getRating();
        }

        double average = sumOfRatings / numberOfReviews;

        return average;
    }

    /**
     * Denne metode udregner den gennemsnitlige rating for et givent kursus ud fra dets lektioner.
     * @param course Identifier på kurset
     * @return Returnerer den gennemsnitlige rating for kurset som en Double.
     */
    public double calculateAverageRatingOnCourse(String course) {

        int lectureId = 0;
        double sumOfRatings = 0;
        double numberOfReviews = 0;

        ArrayList<LectureDTO> lectures = new ArrayList<LectureDTO>();
        lectures = getLectures(course);
        for (LectureDTO lecture : lectures) {
            lectureId = lecture.getId();
        }

        ArrayList<ReviewDTO> reviews = new ArrayList<ReviewDTO>();
        reviews = getReviews(lectureId);
        for (ReviewDTO review : reviews) {
            sumOfRatings = sumOfRatings + review.getRating();
        }

        numberOfReviews = getReviews(lectureId).size();
        double average = sumOfRatings / numberOfReviews;

        return average;
    }

    /**
     * Denne metode giver mulighed for at slette et review.
     * @param reviewId Det review der ønskes slettet.
     * @return hvorvidt reviewet er slettet eller ej
     */
    public boolean softDeleteReviewTeacher(int reviewId) {

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

    /**
     * Henter det samlede antal deltagere på kurset.
     * @param courseId Identifier på kurset.
     * @return Antal af deltagere på kurset.
     */
    public int getCourseParticipants(int courseId) {

        String table = "course_attendant";
        Map<String, String> whereStmt = new HashMap<String, String>();
        whereStmt.put("course_id", String.valueOf(courseId));
        CachedRowSet rs = null;
        int courseAttendants = 0;

        try {
            // Henter deltagere i DB
            rs = DBWrapper.getRecords(table, null, whereStmt, null, 0);

            // Henter antal af deltagere
            courseAttendants = rs.size();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courseAttendants;
    }





}