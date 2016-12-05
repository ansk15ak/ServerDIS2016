package view;

import logic.AdminController;
import logic.MainController;
import security.Digester;
import shared.AdminDTO;
import shared.UserDTO;

import java.util.Scanner;

/**
 * TUIMainMenu er den første menu som administrator ser inden der logges ind.
 **/
public class TUIMainMenu {

    private MainController mainController;
    private AdminController adminController;
    private UserDTO user;
    private TUIAdminMenu tuiAdminMenu;

 /**
 * TUILogIn er menuen hvor administrator kan vælge mellem at logge ind eller lukke programmet.
 **/
    public AdminDTO tUILogIn(AdminDTO adminDTO) {

        MainController mainController = new MainController();
        Scanner input = new Scanner(System.in);
        String mail="";
        String password = "";

        try {

            System.out.println("Velkommen til Undervisningsevaluering for CBS administratorer!");
            System.out.println("Tast 0 for at stoppe programmet");
            System.out.println("Tast 1 for og Logge ind som admin");

            int choice = input.nextInt();

            /**
            * Denne switch tager administrators input (choice) og sender det videre til den valgte case.
            **/
            switch (choice) {
                case 0:
                    System.out.println("Programmet er stoppet.");
                    System.exit(0);
                    break;
                case 1:
                    Scanner input1 = new Scanner(System.in);
                    System.out.println("Indtast mail:");
                    mail = input1.nextLine();

                    Scanner input2 = new Scanner(System.in);
                    System.out.println("Indtast password:");
                    password = input2.nextLine();

                    // hash password at login ("klient" side)
                    String securePW = Digester.hashWithSalt(password);

                    adminDTO.setCbsMail(mail);
                    adminDTO.setPassword(securePW);

                    mainController.loginAdmin(adminDTO);

                    break;
                default:
                    throw new Exception();
            }
        }

        /**
        * Her er en catch som træder i kraft, hvis administratoreren taster en forkert vaerdi.
         */
        catch (Exception e) {
            System.out.printf("Systemet fandt fejlen: %s \n", e);
            System.out.println("Det indtastede er ugyldigt, menuen kører forfra" + "\n");
            tUILogIn(adminDTO);
        }

        return adminDTO;
    }
}
