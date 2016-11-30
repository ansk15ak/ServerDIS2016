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

public class TeacherController extends UserController {

    private TeacherDTO currentTeacher;

    public TeacherController() {
        super();
    }

    public void loadTeacher(TeacherDTO currentTeacher) {
        this.currentTeacher = currentTeacher;
    }

    public double calculateAverageRatingOnLecture(int lectureId) {
        //DecimalFormat df = new DecimalFormat("#.00");

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
     * Udregner den gennemsnitlige rating for et givent kursus ud fra dets lektioner.
     * @param course ID'et på kurset
     * @return Returnerer den gennemsnitlige rating for kurset som en Integer.
     */

    /**
     * Gennemsøger tabellen course_attendant for brugere med et bestemt course_id.
     *
     * @param course id'et på det kursus man ønsker samlet antal deltagere for.
     * @return det samlede antal der er tilmeldt kurset.
     */

    public double calculateAverageRatingOnCourse(String course) {

        int lectureId = 0;
        double sumOfRatings = 0;
        double numberOfReviews = 0;

        // for (LectureDTO lecture : getLectures1(courseId)) {
        for (LectureDTO lecture : getLectures(course)) {
            lectureId = lecture.getId();
        }

        //for (ReviewDTO review : getReviews1(lectureId)) {
        for (ReviewDTO review : getReviews(lectureId)) {
            sumOfRatings = sumOfRatings + review.getRating();
        }

        //numberOfReviews = getReviews1(lectureId).size();
        numberOfReviews = getReviews(lectureId).size();

        double average = sumOfRatings / numberOfReviews;

        return average;
    }

    public boolean softDeleteReviewTeacher(int reviewId) {
        boolean isSoftDeleted = true;

        try {
            Map<String, String> isDeleted = new HashMap();

            isDeleted.put("is_deleted", "1");

            Map<String, String> params = new HashMap();
            params.put("id", String.valueOf(reviewId));
            //params.put("user_id", String.valueOf(userId));

            DBWrapper.updateRecords("review", isDeleted, params);
            return isSoftDeleted;

        } catch (SQLException e) {
            e.printStackTrace();
            isSoftDeleted = false;
        }
        return isSoftDeleted;
    }


    /**
     * Udregner hvor mange som har deltaget i evaluering af lektionen ud fra samlet antal tilmeldte studerende på kurset.
     *
     * @param courseId ID'et på lektionen.
     * @return Mængden af studerende som har deltaget i evalueringen, udregnet som en procentsats.
     */

    public int getCourseParticipants(int courseId) {

        //Forbered MySQL statement
        String table = "course_attendant";
        Map<String, String> whereStmt = new HashMap<String, String>();
        whereStmt.put("course_id", String.valueOf(courseId));
        CachedRowSet rs = null;
        int courseAttendants = 0;

        //Query courseattendant og find samtlige instanser af det courseID
        try {
            rs = DBWrapper.getRecords(table, null, whereStmt, null, 0);
            courseAttendants = rs.size();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courseAttendants;

    }





}