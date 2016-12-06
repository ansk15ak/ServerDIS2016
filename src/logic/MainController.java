package logic;

import security.Digester;
import service.DBWrapper;
import shared.*;
import view.TUIAdminMenu;
import view.TUIMainMenu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * MainControlleren er den første controlleren der bliver kørt.
 */
public class MainController {

    private AdminDTO admin;
    private TeacherDTO teacher;
    private StudentDTO student;
    private Digester digester;
    private AdminController adminCtrl;
    private TeacherController teacherCtrl;
    private StudentController studentCtrl;
    private TUIMainMenu tuiMainMenu;
    private TUIAdminMenu tuiAdminMenu;

    public MainController() {
        adminCtrl = new AdminController();
        teacherCtrl = new TeacherController();
        studentCtrl = new StudentController();
    }

    /**
     * Dette er login metoden som er baseret på variablerne mail og password, som er indtastet af brugeren.
     * @param mail brugerens e-mail adressen
     * @param password brugerens kodeord
     */
    public void login (String mail, String password) {

        /**
         * Her hashes passwordet (med salt), som så derefter er et sikret password, (det her er anden gang det hashes)
         */
        String securedPassword = Digester.hashWithSalt(password);

        try {

            Map<String, String> loginMail = new HashMap<String, String>();
            loginMail.put("cbs_mail", String.valueOf(mail));
            loginMail.put("password", String.valueOf(password));

            // Henter brugeren i DB
            ResultSet result = DBWrapper.getRecords("user", null, loginMail, null, 0);

            while (result.next()) {
                UserDTO type = new UserDTO();
                type.setType(result.getString("type"));

                // Brugeren er en lærer
                if (type.equals("teacher")) {
                    teacherCtrl = new TeacherController();
                    TeacherDTO teacherDTO = new TeacherDTO();
                    teacherDTO.setCbsMail(mail);
                    teacherDTO.setPassword(securedPassword);
                    teacherCtrl.loadTeacher(teacher);
                }

                // Brugeren er en studerende
                if (type.equals("student")) {
                    studentCtrl = new StudentController();
                    StudentDTO studentDTO = new StudentDTO();
                    studentDTO.setCbsMail(mail);
                    studentDTO.setPassword(securedPassword);
                    studentCtrl.loadStudent(student);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,1,"Brugeren kunne ikke logge ind som teacher eller student.");
            System.out.println("invalid login.");
        }
    }

    /**
     * Denne metode logger administrator ind i terminalen.
     */
    public int loginAdmin (AdminDTO adminDTO) {

        /**
         * I dette tilfælde er der ikke hashet første gang
         * Derfor hashes der to gange her for at få samme hash værdi.
         */

        String password = adminDTO.getPassword();
        String mail = adminDTO.getCbsMail();

        // Hasher på "server" siden
        String securedPassword = Digester.hashWithSalt(password);

        try {
            Map<String, String> loginMail = new HashMap<String, String>();
            loginMail.put("cbs_mail", String.valueOf(mail));
            loginMail.put("password", String.valueOf(securedPassword));

            // Henter brugeren i DB
            ResultSet result = DBWrapper.getRecords("user", null, loginMail, null, 0);

            while(result.next()) {
                String type = result.getString("type");

                // Validere om brugeren er en administrator
                if (type.equals("admin")) {
                    AdminDTO admin = new AdminDTO();
                    admin.setCbsMail(mail);
                    admin.setPassword(securedPassword);

                    TUIAdminMenu tuiAdminMenu = new TUIAdminMenu();
                    tuiAdminMenu.menu(admin);
                }
                if (type != "admin") {
                    //Login error. No rows returned for username and password
                    System.out.println("Forkert indtastet type. Prøv igen.");
                    TUIMainMenu tuiMainMenu = new TUIMainMenu();
                    tuiMainMenu.tUILogIn(adminDTO);
                    return 10;
                }
            }
        }
        catch (SQLException e) {
            System.out.print(e.getMessage());
            System.out.println("Du indtastede en forkert vaerdi, proev igen. \n");
            Logging.log(e,1,"Brugeren kunne ikke logge ind som admin");
            TUIMainMenu tuiMainMenu = new TUIMainMenu();
            tuiMainMenu.tUILogIn(adminDTO);
        }

        TUIMainMenu tuiMainMenu = new TUIMainMenu();
        tuiMainMenu.tUILogIn(adminDTO);
        return 200;
    }
}