package shared;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Skabelon på loggen
 */
public class Logging {

    /**
    En log initieres
     */
    protected static final Logger logger = Logger.getLogger("logging");

    /**
     * Log level bestemmes ud fra konfigurationen. SEVERE er default.
     * @param debugLevel Det ønskede Log Level.
     */
    public static void setLogLevel(String debugLevel){
        if(debugLevel.equals("1")) { logger.setLevel(Level.FINEST); }
        else if(debugLevel.equals("2")) { logger.setLevel(Level.FINE); }
        else { logger.setLevel(Level.SEVERE); }
    }

    /**
     * En log entry tilføjes til loggen
     * Filehandler sørger at der oprettes en tekstfil ved navn "application.log" hvorpå data tilskrives.
     * @param ex en fejlbesked
     * @param level det valgte log level
     * @param msg beskeden der skal logges
     */
    public static void log(Exception ex, int level, String msg) {
        FileHandler fh = null;

        try {
            fh = new FileHandler("application.log", true);
            logger.addHandler(fh);
            switch (level) {
                case 1:
                    logger.log(Level.FINEST, msg, ex);
                    if (!msg.equals(""))
                        System.out.println(msg + "finest error");
                    break;
                case 2:
                    logger.log(Level.FINE, msg, ex);
                    if (!msg.equals(""))
                        System.out.println(msg + "fine error");
                    break;
                case 3:
                    logger.log(Level.SEVERE, msg, ex);
                    if (!msg.equals(""))
                        System.out.println(msg + "severe error");
                    break;
                default:
                    logger.log(Level.CONFIG, msg, ex);
                    break;

            }
        } catch (IOException ex1) {
            System.out.println("Loggen kunne ikke findes" + ex1);
        } catch (SecurityException ex1) {
            logger.log(Level.SEVERE, null, ex1);
        }finally {
            if (fh != null) fh.close();
        }
    }
}

