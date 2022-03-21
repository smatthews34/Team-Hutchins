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
    String username; //change from static later
    String password; //change from static later

    public Signup(String username, String password){
        this.username = username;
        this.password = password;
    }

    public int signupSubmit(){ //Return value of 1 is a dummy value
        try {
            if (checkIfUserValid() && checkIfPasswordValid()) {
                File file = new File("loginInfo.txt");
                File encryptedFile = new File("encryptedInfo.des");
                if (encryptedFile.exists()) {
                    return -1;
                } else {
                    boolean worked = file.createNewFile();
                    if (worked){
                        FileWriter fw = new FileWriter(file);
                        fw.write(username + "\n");
                        fw.write(password);
                        fw.close();
                        AESEncryption();
                        file.delete();
                        return 0;
                    }
                    else{
                        System.out.println("Could not open new file to log in, please try again");
                        return 1;
                    }
                }
                //Submit to the database potentially or encryption/decryption
                //Log the user in
            }
            return 1;
        } catch (IOException e){
            e.printStackTrace();
            return 1;
        }
    }

    public boolean checkIfUserValid(){
        return username != null; //May want additional test cases
    }

    public boolean checkIfPasswordValid(){
        return password != null; //May want additional test cases
    }

    public void AESEncryption() {
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
}