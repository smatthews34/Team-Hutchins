import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CellNote {
    public static void sendNotification(String provider, String phone, ArrayList<Course> s){
        String schedule = "";
        String to = "";
        for (int i = 0; i < s.size(); i++){
            schedule += s.get(i).courseCode + " " + s.get(i).shortTitle + " " + s.get(i).meets + " " + s.get(i).startTime + "-" + s.get(i).endTime + "\n";
        }
        switch (provider){
            case "AT&T":
                to = phone + "@txt.att.net";
                break;
            case "Boost":
                to = phone + "@sms.myboostmobile.com";
                break;
            case "Cricket":
                to = phone + "@mms.cricketwireless.net";
                break;
            case "Google":
                to = phone + "@msg.fi.google.com";
                break;
            case "Republic":
                to = phone + "@text.republicwireless.com";
                break;
            case "Sprint":
                to = phone + "@messaging.sprintpcs.com";
                break;
            case "Straight":
                to = phone + "@vtext.com";
                break;
            case "T-Mobile":
                to = phone + "@tmomail.net";
                break;
            case "Ting":
                to = phone + "@message.ting.com";
                break;
            case "U.S.":
                to = phone + "@email.uscc.net";
                break;
            case "Verizon":
                to = phone + "@vtext.com";
                break;
            case "Virgin":
                to = phone + "@vmobl.com";
                break;
            default:
                to = "";
                break;
        }
        System.out.println("To: " + to);
        System.out.println("Message: " + schedule);

        try{
            ProcessBuilder b = new ProcessBuilder("python", System.getProperty("user.dir") + "\\PythonScripts\\alert.py", schedule, "Schedule", to);
            Process p = b.start();
            //BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //BufferedReader readers = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            //String lines =null;
            //while((lines=reader.readLine())!=null){
                //System.out.println("lines"+lines);
            //}
            //while((lines=readers.readLine())!=null){
                //System.out.println("lines"+lines);
            //}

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args)throws IOException{

        Course test_c = new Course("MATH 101", "Intro Math", "Introduction to Mathematics", "9", "10", "MWF", "SHAL", "101");
        Course test_cc = new Course("MATH 101", "Intro Math", "Introduction to Mathematics", "9", "10", "MWF", "SHAL", "101");
        Course test_ccc = new Course("MATH 101", "Intro Math", "Introduction to Mathematics", "9", "10", "MWF", "SHAL", "101");
        ArrayList<Course> s = new ArrayList<Course>();
        s.add(test_c);
        s.add(test_cc);
        s.add(test_ccc);
        sendNotification("Verizon", "9154748044",s);
    }
}
