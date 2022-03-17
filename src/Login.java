import java.io.*;
import java.util.*;

public class Login {

    static public String username; //Change from static later
    static public String password; //Change from static later

    public Login (String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static boolean checkMatch(){
        try {
            File file = new File("loginInfo.txt");
            Scanner fileScan = new Scanner(file);
            String fileUsername = fileScan.next();
            String filePassword = fileScan.next();
            if (!fileUsername.equals(username)) {
                fileScan.close();
                return false;
            }
            else if (!filePassword.equals(password)) {
                fileScan.close();
                return false;
            }
            fileScan.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("User is not registered, Please Sign Up");
            return false;
        }
    }

    public static void main (String[] args) {
        Login.username = "username";
        Login.password = "password";
        System.out.println(Login.checkMatch());
    }

}
