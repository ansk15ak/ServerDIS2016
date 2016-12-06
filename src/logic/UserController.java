package logic;

import security.Digester;
import shared.*;

import java.sql.Timestamp;
import java.util.ArrayList;

import service.DBWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Klassen indeholder logikken bag brugerens funktioner.
 */
public class UserController {

    // Indlæser brugerens kurser
    public static void main(String[] args) {
        UserController controller = new UserController();
        controller.getCourses(1);
    }

    public UserController() {
    }

    /**
     * Denne metode logger brugeren ind på klienten.
     * @param cbs_email brugerens e-mail adresse
     * @param password brugerens kodeord
     * @return Et bruger DTO
     */
    public UserDTO login(String cbs_email, String password) {

        UserDTO user = new UserDTO();
        String securedPW = Digester.hashWithSalt(password);

        try {
            Map<String, String> params = new HashMap();
            params.put("cbs_mail", String.valueOf(cbs_email));
            params.put("password", String.valueOf(securedPW));
            String[] attributes = {"id","type","cbs_mail"};

            // Henter brugeren i DB
            ResultSet rs = DBWrapper.getRecords("user", attributes, params, null, 0);

            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setType(rs.getString("type"));
                user.setCbsMail(rs.getString("cbs_mail"));
                System.out.print("User found" + "\n");
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("User not found" + "\n");
        return null;
    }

    /**
     * Denne metode henter alle reviews for en given lektion.
     * @param lectureId Den lektion der ønskes reviews for.
     * @return Et review DTO
     */
    public ArrayList<ReviewDTO> getReviews(int lectureId) {

        ArrayList<ReviewDTO> reviews = new ArrayList<ReviewDTO>();

        try {
            Map<String, String> params = new HashMap();
            params.put("lecture_id", String.valueOf(lectureId));
            params.put("is_deleted", "0");
            String[] attributes = {"id", "user_id", "lecture_id", "rating", "comment"};

            // Henter reviews i DB
            ResultSet rs = DBWrapper.getRecords("review", attributes, params, null, 0);

            while (rs.next()) {
                ReviewDTO review = new ReviewDTO();
                review.setId(rs.getInt("id"));
                review.setUserId(rs.getInt("user_id"));
                review.setLectureId(rs.getInt("lecture_id"));
                review.setRating(rs.getInt("rating"));
                review.setComment(rs.getString("comment"));
                reviews.add(review);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente getReviews");
        }

        return reviews;
    }

    /**
     * Denne metode henter alle reviews for en given bruger.
     * @param userId Den bruger der ønskes reviews for.
     * @return Et review DTO
     */
    public ArrayList<ReviewDTO> getReviewsFromUserId(int userId) {

        ArrayList<ReviewDTO> reviews = new ArrayList<ReviewDTO>();

        try {
            Map<String, String> params = new HashMap();
            params.put("user_id", String.valueOf(userId));
            params.put("is_deleted", "0");
            String[] attributes = {"id", "user_id", "lecture_id", "rating", "comment"};

            // Henter reviews i DB
            ResultSet rs = DBWrapper.getRecords("review", attributes, params, null, 0);

            while (rs.next()) {
                ReviewDTO review = new ReviewDTO();
                review.setId(rs.getInt("id"));
                review.setUserId(rs.getInt("user_id"));
                review.setLectureId(rs.getInt("lecture_id"));
                review.setRating(rs.getInt("rating"));
                review.setComment(rs.getString("comment"));
                reviews.add(review);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente getReviews");
        }

        return reviews;
    }

    /**
     * Denne metode henter alle lektioner indenfor en et given kursus.
     * @param code Det kursus der ønskes lektioner for.
     * @return Et lektion DTO
     */
    public ArrayList<LectureDTO> getLectures(String code) {

        ArrayList<LectureDTO> lectures = new ArrayList<LectureDTO>();

        try {
            Map<String, String> params = new HashMap();
            params.put("course_id", code);

            // Henter lektioner i DB
            ResultSet rs = DBWrapper.getRecords("lecture", null, params, null, 0);

            while (rs.next()) {
                LectureDTO lecture = new LectureDTO();
                //lecture.setStartDate(rs.getTimestamp("start"));
                //lecture.setEndDate(rs.getTimestamp("end"));
                lecture.setLectureId(rs.getInt("id"));
                lecture.setType(rs.getString("type"));
                lecture.setDescription(rs.getString("description"));
                lecture.setLocation(rs.getString("location"));
                //lecture.setCourseId(rs.getInt("courseId"));
                lectures.add(lecture);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        Logging.log(e,2,"Kunne ikke hente getLecture");

        }

        return lectures;
    }

    /**
     * Denne metode giver mulighed for at slette et review for en given bruger.
     * @param userId Den ønskede bruger.
     * @param reviewId Det review der ønskes slettet.
     * @return hvorvidt reviewet er slettet eller ej
     */
    public boolean softDeleteReview(int userId, int reviewId) {

        boolean isSoftDeleted = true;

        try {
            Map<String, String> isDeleted = new HashMap();
            isDeleted.put("is_deleted", "1");
            Map<String, String> whereParams = new HashMap();

            if(userId != 0) {
                whereParams.put("user_id", String.valueOf(userId));
                whereParams.put("id",String.valueOf(reviewId));
            }

            whereParams.put("id", String.valueOf(reviewId));

            // Henter review i DB
            DBWrapper.updateRecords("review", isDeleted, whereParams);

        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,2,"Softdelete kunne ikke slette review, SoftDeleteReview.");
            isSoftDeleted = false;
        }

        return isSoftDeleted;
    }

    /**
     * Denne metode henter alle kurser for en bruger.
     * @param userId Den ønskede bruger.
     * @return Et kursus DTO
     */
    public ArrayList<CourseDTO> getCourses(int userId) {

        ArrayList<CourseDTO> courses = new ArrayList<CourseDTO>();

        try {
            Map<String, String> params = new HashMap();
            Map<String, String> joins = new HashMap();
            params.put("course_attendant.user_id", String.valueOf(userId));
            joins.put("course_attendant", "course_id");
            String[] attributes = new String[]{"name", "code", "course_id"};

            // Henter brugerens kurser i DB
            ResultSet rs = DBWrapper.getRecords("course", attributes, params, joins, 0);

            while (rs.next()) {
                CourseDTO course = new CourseDTO();
                course.setDisplaytext(rs.getString("name"));
                course.setCode(rs.getString("code"));
                course.setId(rs.getString("course_id"));
                courses.add(course);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente getCourses");
        }

        return courses;
    }

    /**
     * Denne metode henter alle studier for et kursus.
     * @param shortname Det ønskede kursus shortname.
     * @return Et studie DTO
     */
    public ArrayList<StudyDTO> getStudies(String shortname) {

        ArrayList<StudyDTO> studies = new ArrayList<StudyDTO>();

        try {
            Map<String, String> params = new HashMap();
            params.put("shortname", String.valueOf(shortname));
            String[] attributes = new String[]{"name", "shortname", "id"};

            // Henter alle kursets studier i DB
            ResultSet rs = DBWrapper.getRecords("study", attributes, params, null, 0);

            while (rs.next()) {
                StudyDTO study = new StudyDTO();
                study.setName(rs.getString("name"));
                study.setShortname(rs.getString("shortname"));
                study.setId(rs.getInt("id"));
                studies.add(study);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente getCourses");
        }

        return studies;
    }
}