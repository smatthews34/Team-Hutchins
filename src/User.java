public class User {
    //ArrayList<course> schedule;   //change later
    String username;
    String password;
    String name;
    String major;   //I think he made some comment about the amt of info, so I made these optional
    int gradYear;

    public User(String username, String password, String name){
        //schedule = new ArrayList();
        this.username = username;
        this.password = password;
        this.name = name;
        major = "NO_MAJOR";
        gradYear = -1;
    }
}
