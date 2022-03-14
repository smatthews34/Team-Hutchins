public class Signup {
    String username;
    String password;

    public Signup(String username, String password){
        this.username = username;
        this.password = password;
        dbSubmit();
    }

    public void dbSubmit(){
        if (checkIfValid()){
            //Submit to the database
            //Log the user in
        }
    }

    public boolean checkIfValid(){
        return username != null && password != null; //May want additional test cases
    }
}