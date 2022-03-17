import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Signup {
    static String username; //change from static later
    static String password; //change from static later

    public Signup(String username, String password){
        this.username = username;
        this.password = password;
        signupSubmit();
    }

    public static void signupSubmit(){
        try {
            if (checkIfValid()) {
                File file = new File("loginInfo.txt");
                File encryptedFile = new File("encryptedInfo.des");
                if (encryptedFile.exists()) {
                    System.out.println("There is already a registered account with these credentials");
                } else {
                    boolean worked = file.createNewFile();
                    if (worked){
                        FileWriter fw = new FileWriter(file);
                        fw.write(username + "\n");
                        fw.write(password);
                        fw.close();
                        AESEncryption();
                        file.delete();
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

    public static void AESEncryption() {
        // file to be encrypted
        try {
            FileInputStream inFile = new FileInputStream("loginInfo.txt");

            // encrypted file
            FileOutputStream outFile = new FileOutputStream("encryptedInfo.des");

            // password to encrypt the file
            String password = "javapapers";

            // password, iv and salt should be transferred to the other end
            // in a secure manner

            // salt is used for encoding
            // writing it to a file
            // salt should be transferred to the recipient securely
            // for decryption
            byte[] salt = new byte[8];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(salt);
            FileOutputStream saltOutFile = new FileOutputStream("salt.enc");
            saltOutFile.write(salt);
            saltOutFile.close();

            SecretKeyFactory factory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
                    256);
            SecretKey secretKey = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            //
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();

            // iv adds randomness to the text and just makes the mechanism more
            // secure
            // used while initializing the cipher
            // file to store the iv
            FileOutputStream ivOutFile = new FileOutputStream("iv.enc");
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            ivOutFile.write(iv);
            ivOutFile.close();

            //file encryption
            byte[] input = new byte[64];
            int bytesRead;

            while ((bytesRead = inFile.read(input)) != -1) {
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null)
                    outFile.write(output);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                outFile.write(output);

            inFile.close();
            outFile.flush();
            outFile.close();
        } catch (Exception e) {

        }

    }




    public static void main (String[] args){
        Signup.username = "user";
        Signup.password = "pass";
        Signup.signupSubmit();
    }

}