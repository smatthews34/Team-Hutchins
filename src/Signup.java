import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Signup {
    String username;
    String password;
    String name;

    public Signup(String username, String password, String name){
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public int signupSubmit(){ //Return value of 1 is a dummy value
        try {
            if (checkIfUserValid() && checkIfPasswordValid() && checkIfNameValid()) {
                File file = new File("loginInfo.txt");
                for (int i = 0; i < checkFile(); i++) { //Check if username is already taken from all available encrypted files (checkFile() times)
                    FileInputStream fis = new FileInputStream("encryptedInfo" + i + ".des");
                    FileInputStream ivFis = new FileInputStream("iv" + i + ".enc");
                    FileInputStream saltFis = new FileInputStream("salt" + i + ".enc");
                    AESDecryption(saltFis, ivFis, fis);
                    File plainFile = new File("plainfile_decrypted.txt");
                    Scanner fileScan = new Scanner(plainFile);
                    String fileUsername = fileScan.next();
                    if (fileUsername.equals(username)){
                        fileScan.close();
                        plainFile.delete();
                        return -1; //Username already taken
                    }
                    fileScan.close();
                    file.delete();
                }
                    boolean worked = file.createNewFile();
                    if (worked){
                        FileWriter fw = new FileWriter(file);
                        fw.write(username + "\n");
                        fw.write(password + "\n");
                        fw.write(name);
                        fw.close();
                        AESEncryption();
                        file.delete();
                        return 0; //No errors
                    }
                    else{
                        System.out.println("Could not open new file to log in, please try again");
                        return 1;
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

    public boolean checkIfNameValid(){
        return name != null; //May want additional test cases
    }

    private int checkFile(){ //Returns the next safe index to create encrypted files
        //POTENTIALLY UTILIZE INCREMENTING LOOP TO CHECK HOW MANY PEOPLE THERE ARE AND CREATE A NEW FILE
        int i = 0;
        while(true){
            File file = new File("encryptedInfo" + i + ".des");
            if (!file.exists()){
                break;
            }
            i++;
        }
        return i;
    }

    private void AESEncryption() {
        // file to be encrypted
        try {
            FileInputStream inFile = new FileInputStream("loginInfo.txt");

            // encrypted file
            int number = checkFile();
            FileOutputStream outFile = new FileOutputStream("encryptedInfo" + number + ".des");

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
            FileOutputStream saltOutFile = new FileOutputStream("salt" + number +".enc");
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
            FileOutputStream ivOutFile = new FileOutputStream("iv" + number + ".enc");
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

    private void AESDecryption(FileInputStream saltFis, FileInputStream ivFis, FileInputStream fis) {
        try {
            String password = "javapapers";
            // reading the salt
            // user should have secure mechanism to transfer the
            // salt, iv and password to the recipient
            byte[] salt = new byte[8];
            saltFis.read(salt);
            saltFis.close();

            // reading the iv
            byte[] iv = new byte[16];
            ivFis.read(iv);
            ivFis.close();

            SecretKeyFactory factory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
                    256);
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            // file decryption
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            FileOutputStream fos = new FileOutputStream("plainfile_decrypted.txt");
            byte[] in = new byte[64];
            int read;
            while ((read = fis.read(in)) != -1) {
                byte[] output = cipher.update(in, 0, read);
                if (output != null)
                    fos.write(output);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                fos.write(output);
            fis.close();
            fos.flush();
            fos.close();
        } catch (Exception e) {

        }
    }

}