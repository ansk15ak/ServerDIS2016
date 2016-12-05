
import com.google.gson.Gson;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;

import logic.CBSParser;
import logic.ConfigLoader;
import logic.MainController;
import logic.UserController;
import shared.AdminDTO;
import shared.Logging;
import shared.UserDTO;
import view.TUIMainMenu;

import javax.ws.rs.*;
import java.io.PrintStream;

/**
 * Run klassen indeholder main metoden, som starter serveren op.
 *
 */

public class Run {

    /**
     * Denne metode anvendes af JVM til at starte eksekveringen af programmet.
     * Serveren startes op og en url til serveren tilbydes i TUI.
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {

        HttpServer server = null;

        // Configurationsfilen loades
        ConfigLoader.parseConfig();

        try {
            PrintStream stdout = System.out;
            System.setOut(null);
            server = HttpServerFactory.create("http://" + ConfigLoader.SERVER_ADDRESS + ":" + ConfigLoader.SERVER_PORT + "/");
            System.setOut(stdout);

            server.start();

            // Log initieres og log level bestemmes
            Logging.setLogLevel(ConfigLoader.DEBUG);

            // Data indlæses i database
            //CBSParser.parseCBSData();

            // Vejledning med url printes til TUI
            System.out.println("Server running");
            System.out.println("Visit: http://" + ConfigLoader.SERVER_ADDRESS + ":" + ConfigLoader.SERVER_PORT + "/");

            AdminDTO adminDTO = new AdminDTO();
            TUIMainMenu tuiMainMenu = new TUIMainMenu();
            tuiMainMenu.tUILogIn(adminDTO);

            System.out.println("Hit return to stop...");
            System.in.read();
            System.out.println("Stopping server");
            System.out.println("Server stopped");
            System.out.println();

        }
        catch(Exception e){
            Logging.log(e, 3, "Fejl. System startede ikke!");
            System.out.println(e);
            System.exit(20);
        }

    }

}
