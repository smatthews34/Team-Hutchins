import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {
    String username;
    File f;
    public Logger logger;
    FileHandler fh;

    public Logging(String user) throws IOException {
        this.username = user;
        f = new File(user+".txt");
        if (!f.exists()){
            f.createNewFile();
            System.out.println("Log File created: " + f.getName());
        }
        fh = new FileHandler(user+".txt",true);
        logger = Logger.getLogger("Log");
        logger.addHandler(fh);
        SimpleFormatter form = new SimpleFormatter();
        fh.setFormatter(form);
    }
    public void Action(String s) {
        this.logger.info(s);
    }
    public void logConflict(String s) {
        this.logger.warning(s);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcom to Logging Testing!");
        //Logging lg1 = new Logging("tom");
        //lg1.logger.setLevel(Level.ALL);
        //lg1.Action("Hi");
        //lg1.logger.info("Hello testing logger.");
        //lg1.logger.warning("conflict with scheduling accepted");//
        System.out.println("Thank you.");
    }
}
