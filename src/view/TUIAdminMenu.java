package view;

import logic.AdminController;
import shared.AdminDTO;
import shared.StudentDTO;
import shared.UserDTO;

import java.util.Scanner;

/**
 * Her ses TUIAdminMenuen, som er administratorens brugrgrænseflade.
 **/
public class TUIAdminMenu {

    private AdminController adminController;

    public void menu(AdminDTO adminDTO) {
        adminController = new AdminController();
        Scanner input = new Scanner(System.in);
        try {

            /**
             * Her ses de muligheder som administrator har i menuen.
             **/

            System.out.println("Velkommen til Undervisningsevaluering for CBS administratorer!");
            System.out.println("Du er logget ind som: " +  adminDTO.getCbsMail());
            System.out.println("Tast 0 for at stoppe programmet og log ud.");
            System.out.println("Tast 1 for at oprette en ny bruger. ");
            System.out.println("Tast 2 for slet en bruger. ");
            System.out.println("Tast 3 for slet et review.");
            System.out.println("Tast 4 for tildeling af kursus til bruger.");

            /**
             * Her modtager vi administratorens valg (choice) til ovenstående menu,
             * og administrator sendes videre til den valgte metode.
             **/

            int choice = input.nextInt();

            switch (choice) {

                case 0: // stop program og log ud
                    System.out.println("Programmet er stoppet og du er logget ud.");
                    System.exit(0);
                    break;

                case 1: // opret bruger
                    AdminDTO newUser = new AdminDTO();
                    TUICreateUser(adminDTO, newUser);
                    adminController = new AdminController();
                    adminController.createUser(adminDTO, newUser);
                    break;

                case 2: // slet bruger
                    int userId = adminDTO.getId();
                    adminController = new AdminController();
                    adminController.deleteUser(adminDTO);
                   break;

                case 3: // slet review
                    adminController = new AdminController();
                    adminController.deleteReview(adminDTO);
                    break;

               case 4: // tildel kursus til bruger
                    adminController = new AdminController();
                    adminController.courseAssign(adminDTO);
                    break;

                default:
                    throw new Exception();
            }
        }
        /**
        * Her er en catch som træder i kraft, hvis brugeren taster en invalid vaerdi.
        **/
        catch (Exception e) {
            System.out.printf("Systemet fandt fejlen: %s \n", e);
            System.out.println("Du indtastede en forkert vaerdi, proev igen.\n");
            menu(adminDTO);
        }
    }

    /**
     * TUICreateUser tager administrators input som den nye brugers parametre: CBS mail, Password og Type
     * og sender variablerne videre til logik laget (AdminController)
     **/
    public AdminDTO TUICreateUser(AdminDTO adminDTO, AdminDTO newUser){

        Scanner mail_input = new Scanner(System.in);
        System.out.println("Indtast CBS mail: ");
        String mail = mail_input.nextLine();

        Scanner password_input = new Scanner(System.in);
        System.out.println("Indtast password: ");
        String password = password_input.nextLine();

        Scanner type_input = new Scanner(System.in);
        System.out.println("Indtast type (student, teacher, admin): ");
        String type = type_input.nextLine();

        newUser.setCbsMail(mail);
        newUser.setPassword(password);
        newUser.setType(type);

        return newUser;
    }

}

