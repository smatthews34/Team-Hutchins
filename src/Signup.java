import java.io.*;

import java.io.*;

public class Signup {
    static String username; //change from static later
    static String password; //change from static later

    public Signup(String username, String password){
        this.username = username;
        this.password = password;
        dbSubmit();
    }

    public static void dbSubmit(){
        try {
            if (checkIfValid()) {
                File file = new File("loginInfo.txt");
                if (file.exists()) {
                    System.out.println("There is already a registered account with these credentials");
                } else {
                    boolean worked = file.createNewFile();
                    if (worked){
                        FileWriter fw = new FileWriter(file);
                        fw.write(username + "\n");
                        fw.write(password);
                        fw.close();
                        System.out.println("Successfully Registered");
                    }
                    else{
                        System.out.println("Could not open new file to log in, please try again");
                    }
                }
                //Submit to the database potentially or encryption/decryption
                //Log the user in
            }
        } catch (IOException e){
            System.out.println("Error with file config");
            e.printStackTrace();
        }
    }

    public static boolean checkIfValid(){
        return username != null && password != null; //May want additional test cases
    }

    public static void main (String[] args){
        Signup.username = "username";
        Signup.password = "password";
        Signup.dbSubmit();
    }

}