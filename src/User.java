import java.util.ArrayList;

public class User {
    ArrayList<Course> schedule;   //change later
    String username;
    String password;
    String name;
    String email;
    String major;   //I think he made some comment about the amt of info, so I made these optional
    int gradYear;

    public User(String username, String password, String name){
        schedule = new ArrayList();
        this.username = username;
        this.password = password;
        this.name = name;
        major = "NO_MAJOR";
        gradYear = -1;
    }

    //this is a lot like checkDouble(), may want to streamline in future
    public boolean scheduleContains(String code){
        for (Course c : schedule) {
            if (c.courseCode.equals(code)) {
                return true;
            }
        }
        return false;
    }

    public Course getCourse(String code) {
        //this can definitely be more efficient eventually
        for (Course c : schedule) {
            if (c.courseCode.equals(code)) {
                return c;
            }
        }
        return null;
    }

    public void printSchedule(){
        for(Course c: schedule){
            System.out.println(c.toString());
        }
    }

    public void setEmail(String email){
        this.email = email;
    }



}
