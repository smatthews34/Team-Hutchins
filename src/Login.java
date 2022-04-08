import java.io.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Login {

    public String username;
    public String password;
    public String name;

    public Login (String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User loginSubmit() {
        if (checkMatch() == 0) {
            return new User(username, password, name);
        }
        else {
            return null;
        }
    }

    private int checkMatch(){ //Checks files for valid login
        try {
            for (int i = 0; i < checkFile(); i++) {
                FileInputStream fis = new FileInputStream("encryptedInfo" + i + ".des");
                FileInputStream ivFis = new FileInputStream("iv" + i + ".enc");
                FileInputStream saltFis = new FileInputStream("salt" + i + ".enc");
                AESDecryption(saltFis, ivFis, fis);
                File file = new File("plainfile_decrypted.txt");
                Scanner fileScan = new Scanner(file);
                String fileUsername = fileScan.next();
                String filePassword = fileScan.next();
                name = fileScan.next();
                if (fileUsername.equals(username) && filePassword.equals(password)){
                    fileScan.close();
                    file.delete();
                    return 0; //Successful Login
                }
                fileScan.close();
                file.delete();
            }
            return -1; //Username or Password is incorrect
        } catch (FileNotFoundException e) {
            return 1; //FileNotFoundException was thrown
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
}
