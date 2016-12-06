package logic;

import security.Digester;
import service.DBWrapper;
import shared.*;
import view.TUIAdminMenu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Klassen indeholder logikken bag administratorens funktioner.
 **/
public class AdminController extends UserController {

    private TUIAdminMenu tuiAdminMenu;


    /**
     * AdminControlleren har nedarvet fra UserControlleren dvs. den har adgang til de samme metoder.
     */
    public AdminController() {
        super();
    }

    /**
     * Denne metode giver mulighed for at slette et review på en lektion.
     * @param adminDTO Et administrator DTO
     */
    public void deleteReview(AdminDTO adminDTO) {

        // Print alle studier
        for (StudyDTO studyDTO : getStudies()) {
            System.out.println("id: " + studyDTO.getId() + " - forkortelse: " + studyDTO.getShortname() + " - name: " + studyDTO.getName());
        }
        Scanner input1 = new Scanner(System.in);
        System.out.println("Indtast id for det ønskede studie: ");

        // Vælg studie
        int idStudyChoice = input1.nextInt();

        // Print alle kurser på det valgte studie
        for (CourseDTO courseDTO : getCourses(idStudyChoice)) {
            System.out.println("Id: " + courseDTO.getDisplaytext() + " - Name: " + courseDTO.getCode());
        }
                // Vælg kursus
                Scanner input2 = new Scanner(System.in);
                System.out.println("Indtast id for ønskede kursus: ");
                String idCourseChoice = input2.nextLine();

                // Print alle lektioner på det valgte kursus
                for (LectureDTO lectureDTO : getLectures(idCourseChoice)) {
                    System.out.println("id: " + lectureDTO.getId() + " " + lectureDTO.getDescription() + " - " + lectureDTO.getType());
                }

                // Vælg lektion
                System.out.println("Indtast id for ønskede lecture: ");
                Scanner input3 = new Scanner(System.in);
                int idLectureChoice = input3.nextInt();

                // Print alle revies på den valgte lektion
                for (ReviewDTO reviewDTO : getReviews(idLectureChoice)) {
                    System.out.println("id: " + reviewDTO.getId() + " - Rating: " + reviewDTO.getRating() + " - Comment: " + reviewDTO.getComment() + " - Soft delete: " + reviewDTO.isDeleted());

                }

                // Vælg review
                System.out.println("Indtast id for ønskede review der skal slettes: ");
                Scanner input4 = new Scanner(System.in);
                int idReviewChoice = input4.nextInt();

                // Slet af review i DB (soft delete)
                UserController u = new UserController();
                u.softDeleteReview(0,idReviewChoice);

                // Vis menu
                TUIAdminMenu tuiAdminMenu = new TUIAdminMenu();
                tuiAdminMenu.menu(adminDTO);
            }

    /**
     * Denne metode giver mulighed for at slette en bruger.
     */
    public void deleteUser(AdminDTO adminDTO) {

        // Print alle brugere
        for (UserDTO user : getUsers())
        {
            System.out.println("Id: " + user.getId() +  " Type: " + user.getType() + "  CBS mail: " + user.getCbsMail()) ;
        }

        // Vælg bruger
        Scanner input = new Scanner(System.in);
        System.out.println("Indtast id på bruger der skal slettes: ");
        int userIdDelete = input.nextInt();

        // Slet bruger
        try{
            Map<String, String> course_attendant = new HashMap<String, String>();
            course_attendant.put("user_id", String.valueOf(userIdDelete));

            Map<String, String> id = new HashMap<String, String>();
            id.put("id", String.valueOf(userIdDelete));

            // Slet af deltagelse i DB
            DBWrapper.deleteRecords("course_attendant", course_attendant);
            // Slet af bruger i DB
            DBWrapper.deleteRecords("user", id);

            System.out.println("Brugeren er slettet" + "\n");
            TUIAdminMenu tuiAdminMenu = new TUIAdminMenu();
            tuiAdminMenu.menu(adminDTO);

        } catch(SQLException e) {
            e.printStackTrace();
            Logging.log(e, 1, "Brugeren kunne ikke slettes" + "\n");
        }
    }

    /**
     * Denne metode giver mulighed for at oprette en ny bruger.
     * @param adminDTO Et administrator DTO
     * @param newUser Et bruger DTO
     **/
    public void createUser(AdminDTO adminDTO, AdminDTO newUser) {

        String mail = newUser.getCbsMail();
        String password = newUser.getPassword();
        String type = newUser.getType();

        //Hash password ved opret bruger.
        String securePw = Digester.hashWithSalt(password);
        String securePw2 = Digester.hashWithSalt((securePw));

        // Eksisterer brugeren allerede?
        for (AdminDTO user : getUsers()) {
            if (mail.equals(user)) {
                System.out.println("brugeren findes allerede" + "\n");
                tuiAdminMenu = new TUIAdminMenu();
                tuiAdminMenu.menu(adminDTO);
            }
        }

        // Opfylder password kravene for et password?
        if ((password.matches(".*[a-zA-Z]+.*"))) {
            try {
                Map<String, String> userCreate = new HashMap<String, String>();

                userCreate.put("cbs_mail", String.valueOf(mail));
                userCreate.put("password", String.valueOf(securePw2));
                userCreate.put("type", String.valueOf(type));

                // Oprettelse af bruger i DB
                DBWrapper.insertIntoRecords("user", userCreate);
                System.out.println("Brugeren " + mail + " er nu oprettet." + "\n");

                // Vis menu
                TUIAdminMenu tUIAdminMenu = new TUIAdminMenu();
                tUIAdminMenu.menu(adminDTO);

            } catch (SQLException e) {
                e.printStackTrace();
                Logging.log(e, 1, "Brugeren kunne ikke oprettes" + "\n");
            }
        } else {
            System.out.println("Forkert værdi i password. Prøv igen " + "\n");
            tuiAdminMenu = new TUIAdminMenu();
            tuiAdminMenu.menu(adminDTO);
        }
    }

    /**
     * Denne metode giver mulighed for at tildele et kursus til brugeren.
     * @param adminDTO Et administrator DTO
     */
    public void courseAssign (AdminDTO adminDTO){

        // Print alle brugere
        for (UserDTO user : getUsers())
        {
            System.out.println("Id: " + user.getId() +  " Type: " + user.getType() + "  CBS mail: " + user.getCbsMail()) ;
        }

        // Vælg bruger
        Scanner input = new Scanner(System.in);
        System.out.println("Indtast id for bruger som skal tildeles kursus: ");
        int idUserChoice = input.nextInt();

        // Print alle studier
        for (StudyDTO studyDTO: getStudies()){
            System.out.println("id: " + studyDTO.getId() + " - forkortelse: " + studyDTO.getShortname() + " - name: " + studyDTO.getName() );
        }

        // Vælg studie
        Scanner inputStudy = new Scanner(System.in);
        System.out.println("Indtast id for det studie brugeren skal tilknyttet: ");
        int idStudyChoice = inputStudy.nextInt();

        // print kurser indenfor det valgte studie
        for (CourseDTO courseDTO : getCourseStudy(idStudyChoice)) {
            System.out.println("Id: " + courseDTO.getId() + " - Name: " + courseDTO.getCode() + " " + courseDTO.getDisplaytext());
        }

        // Vælg kursus
        Scanner input2 = new Scanner(System.in);
        System.out.println("Indtast id for ønskede kursus brugeren skal tildeles: ");
        int idCourseChoice = input2.nextInt();

        // tildel valgt kursus til valgt bruger
        setUserCourse(idCourseChoice, idUserChoice);
        System.out.println("Brugeren er tilknyttet " + idCourseChoice);

        // Vis menu
        TUIAdminMenu tuiAdminMenu = new TUIAdminMenu();
        tuiAdminMenu.menu(adminDTO);
    }

    /**
     * Denne metode henter alle kurser indenfor et given studie.
     * @param idStudyChoice Identifier for det valgte studie
     * @return
     */
    public ArrayList<CourseDTO> getCourseStudy(int idStudyChoice){

        ArrayList<CourseDTO> courses = new ArrayList<CourseDTO>();

        try {
            Map<String, String> params = new HashMap();
            params.put("study_id", String.valueOf(idStudyChoice));
            String[] attributes = new String[]{"name", "code", "id"};

            // Hent kurser i DB
            ResultSet rs = DBWrapper.getRecords("course", attributes, params, null, 0);

            while (rs.next()) {
                CourseDTO courses1 = new CourseDTO();
                courses1.setId(rs.getString("id"));
                courses1.setCode(rs.getString("code"));
                courses1.setDisplaytext(rs.getString("name"));

                courses.add(courses1);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente getCouses");

        }

        return courses;
    }

    /**
     * Denne metode tildeler et kursus til en bruger
     * @param idCourseChoice Det kursus der ønskes tildelt
     * @param userId Den bruger kurset ønskes tildelt til
     * @return Et bruger DTO
     */
    private ArrayList<UserDTO> setUserCourse(int idCourseChoice, int userId) {

        ArrayList<UserDTO> userscourse = new ArrayList<UserDTO>();
        try {
            Map<String, String> params = new HashMap();
            params.put("course_id", Integer.toString(idCourseChoice));
            params.put("user_id", Integer.toString(userId));

            // tildeler kursus til bruger i DB
            DBWrapper.insertIntoRecords("course_attendant", params);

        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e, 1, "Funktionen kunne ikke hente kurserne" + "\n");
        }

        return userscourse;
    }

    /**
     * Denne metode henter alle brugere.
     * @return Et administrator DTO
     */
    private ArrayList<AdminDTO> getUsers() {

        ArrayList<AdminDTO> users = new ArrayList<AdminDTO>();
        try {
            String[] attributes = {"id", "cbs_mail", "type"};

            // Hent bruger i DB
            ResultSet rs = DBWrapper.getRecords("user", attributes, null, null, 0);

            while (rs.next()) {
                AdminDTO user = new AdminDTO();
                user.setId(rs.getInt("id"));
                user.setCbsMail(rs.getString("cbs_mail"));
                user.setType(rs.getString("type"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e, 1, "Funktionen getUsers kunne ikke hente Brugerne" + "\n");
        }

        return users;
    }

    /**
     * Denne metode henter alle kurser.
     * @return Et kursus DTO
     */
    private ArrayList<CourseDTO> getCourses() {

        ArrayList<CourseDTO> courses = new ArrayList<CourseDTO>();

        try {
            String[] attributes = new String[]{"name", "code", "id"};

            // Hent alle kurser i DB
            ResultSet rs = DBWrapper.getRecords("course", attributes, null, null, 0);

            while (rs.next()) {
                CourseDTO course = new CourseDTO();
                course.setDisplaytext(rs.getString("name"));
                course.setCode(rs.getString("code"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente getCourses");
        }

        return courses;
    }

    /**
     * Denne metode henter alle lektioner indenfor en et given kursus.
     * @param course Det kursus der ønskes lektioner for.
     * @return Et lektion DTO
     */
    public ArrayList<LectureDTO> getLectures(String course) {

        ArrayList<LectureDTO> lectures = new ArrayList<LectureDTO>();

        try {
            Map<String, String> params = new HashMap();
            params.put("course_id", String.valueOf(course));
            String[] attributes = new String[]{"description", "type", "id"};

            // Hent alle lektioner i DB
            ResultSet rs = DBWrapper.getRecords("lecture", attributes, params, null, 0);

            while (rs.next()) {
                LectureDTO lecture = new LectureDTO();
                lecture.setLectureId(rs.getInt("id"));
                lecture.setType(rs.getString("type"));
                lecture.setDescription(rs.getString("description"));
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
     * Denne metode henter alle reviews for en given lektion.
     * @param lectureId Den lektion der ønskes reviews for.
     * @return Et review DTO
     */
    public ArrayList<ReviewDTO> getReviews(int lectureId) {

        ArrayList<ReviewDTO> reviews = new ArrayList<ReviewDTO>();

        try {
            Map<String, String> params = new HashMap();
            params.put("lecture_id", String.valueOf(lectureId));
            String[] attributes = {"id", "user_id", "lecture_id", "rating", "comment"};

            // Henter review for den valgte lektion i DB
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
            Logging.log(e, 2, "Kunne ikke hente getReviews");
        }

        return reviews;
    }

    /**
     * Denne metode henter alle studier.
     * @return Et studie DTO
     */
    private ArrayList<StudyDTO> getStudies() {

        ArrayList<StudyDTO> studies = new ArrayList<StudyDTO>();

        try {

            // Hent alle studier i DB
            ResultSet rs = DBWrapper.getRecords("study", null, null, null, 0);

            while (rs.next()) {
                StudyDTO study = new StudyDTO();
                study.setName(rs.getString("name"));
                study.setShortname(rs.getString("shortname"));
                study.setId(rs.getInt("id"));
                studies.add(study);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente Studies");
        }

        return studies;
    }
}