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
        if (checkMatch()) {
            return new User(username, password, name);
        }
        else {
            return null;
        }
    }

    private boolean checkMatch(){ //Checks file for valid login
        try {
            AESDecryption();
            File file = new File("plainfile_decrypted.txt");
            Scanner fileScan = new Scanner(file);
            String fileUsername = fileScan.next();
            String filePassword = fileScan.next();
            name = fileScan.next();
            if (!fileUsername.equals(username)) {
                fileScan.close();
                file.delete();
                return false;
            }
            else if (!filePassword.equals(password)) {
                fileScan.close();
                file.delete();
                return false;
            }
            fileScan.close();
            file.delete();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    private void AESDecryption() {
        try {
            String password = "javapapers";
            // reading the salt
            // user should have secure mechanism to transfer the
            // salt, iv and password to the recipient
            FileInputStream saltFis = new FileInputStream("salt.enc");
            byte[] salt = new byte[8];
            saltFis.read(salt);
            saltFis.close();

            // reading the iv
            FileInputStream ivFis = new FileInputStream("iv.enc");
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
            FileInputStream fis = new FileInputStream("encryptedInfo.des");
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
