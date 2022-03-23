import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    static void printCommands(){
        String[] commands = {"view", "add", "remove", "undo", "redo", "list", "filter","quit"};
        System.out.println("- Valid commands:");
        for(String s : commands){
            System.out.println("\t" + s);
        }
    }

    static void tempPrint(ArrayList<Course> schedule){
    System.out.println("- Current Schedule:");
        for (int i = 0; i < schedule.size(); i++){
            System.out.println(schedule.get(i).courseCode);
        }

    }

    static void printScreen(){
        System.out.println("\n___________________________________________________");
        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println(": Team Hutchins\t\t\tCLASS SCHEDULING ASSISTANT :");
        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::\n");
    }

    static void printEndScreen(){
        System.out.println("___________________________________________________\n");

    }


    public static void main (String[] args) {
        CourseList cl = new CourseList();
        User user = new User("jimatheey123", "mypassword", "Jimatheey"); //Potentially change to null later

        Course testC1 = new Course("ACCT 202 B", "PRIN OF ACCOUNT", "PRINCIPLES OF ACCOUNTING II", "8:00:00", "8:50:00", "MWF",
                "HAL", "306");
        Course testC2 = new Course("BIOL 234 A", "CELL BIOLOGY", "CELL BIOLOGY", "9:00:00", "9:50:00", "MWF",
                "HAL", "208");

        //Temporary for testing
        System.out.println("Add:");
        cl.addClass(testC1,user.schedule);
        cl.addClass(testC2, user.schedule);

        tempPrint(user.schedule);
        //end temp

        Scanner mainScn = new Scanner(System.in);
        printScreen();
        System.out.println("- Welcome to the Class Scheduling Assistant!");
        System.out.println("- Type what you'd like to do:\n");
        System.out.println("login\t\tsign_up\t\tquit\n");
        System.out.print(">");
        String commandLn = mainScn.nextLine();
        StringTokenizer st = new StringTokenizer(commandLn, " "); //Like a second scanner that parses the line from mainScn
        String command = st.nextToken();

        while (!command.equals("quit")){
            if(command.equals("login")){
                File file = new File("encryptedInfo.des");
                Scanner loginScn = new Scanner(System.in);
                System.out.println("Enter your username");
                String username = loginScn.next();
                System.out.println("Enter your password");
                String password = loginScn.next();

                Login l = new Login(username, password);
                User potentialUser = l.loginSubmit();

                if (potentialUser == null && file.exists()){
                    System.out.println("Username or Password are invalid, Please Try Again");
                }
                else if (potentialUser == null && !file.exists()) {
                    System.out.println("User is not registered, Please Sign Up");
                }
                else {
                    System.out.println(potentialUser.username + potentialUser.password + potentialUser.name); //Temporary
                    //May copy to global User variable here
                }
            }

            else if(command.equals("view")){
                //TODO: This may be changed later
                tempPrint(user.schedule);
            }

           else if(command.equals("sign_up")){ //Make sure this can be accessed with space, change to sign up later
                Scanner signScn = new Scanner(System.in);
                System.out.println("Enter new username");
                String username = signScn.next();
                System.out.println("Enter new password");
                String password = signScn.next();
                System.out.println("Enter your name");
                String name = signScn.next();
                Signup s = new Signup(username, password, name);

                if (!s.checkIfUserValid()){
                    System.out.println("Username is null, please try again");
                }
                else if (!s.checkIfPasswordValid()){
                    System.out.println("Password is null, please try again");
                }
                else if (!s.checkIfNameValid()){
                    System.out.println("Name is null, please try again");
                }
                else{ //Username, password, and name are valid
                    int errno = s.signupSubmit();
                    if (errno == 0){
                        System.out.println("Successfully Registered, use 'login' command to log in");
                    }
                    else if (errno == -1){ //There is already a registered account with these credentials
                        System.out.println("There is already a registered account on this machine, " +
                                "please use 'login' command to log in"); //Potentially allow multiple users in Sprint 2
                    }
                }
                //Scan user input for username and password, check for validity
                //If valid complete sign up & log in, if not valid redo prompt or exit
            }

           else if(command.equals("add")){
                //TODO
                Scanner add = new Scanner(System.in);
                String addOption = "";
                while(!addOption.equals("done")) {
                    System.out.println("Enter course in the format; CODE ### A, to be added or enter 'done' if finished adding: ");
                    addOption = add.nextLine();
                    if (!addOption.equals("done")) {
                        Course a = cl.getCourse(addOption);
                        if (!a.equals(null)) {
                            cl.addClass(a, user.schedule);
                        } else {
                            System.out.println("Please enter a valid class.");
                        }
                    }
                }
                add.close();
            }

            else if(command.equals("remove")){  //user input should take the form 'remove ACCT 202 B'
                String code = "";
                    while(st.hasMoreTokens()) {
                        code += st.nextToken();     //Concat course code like ACCT 202 B

                        if (st.hasMoreTokens()) {
                            code += " ";
                        }
                    }

                    if(user.scheduleContains(code)) {
                        Course c = user.getCourse(code);
                        cl.removeClass(c, user.schedule);
                        tempPrint(user.schedule);
                    }

                    else {
                        System.out.println("Error: Course not found.");
                    }
            }

            else if(command.equals("undo")){
                cl.undo(user.schedule);
                tempPrint(user.schedule);

            }

            else if(command.equals("redo")){
                cl.redo(user.schedule);
                tempPrint(user.schedule);

            }

            else if(command.equals("list")){
                printCommands();

            }
            else if(command.equals("filter")){
                System.out.println("What would you like to filter by?");
                System.out.println("The options are to filter by: day, time or department");
                String filter = st.nextToken();
                if(filter.equals("days")){
                    Search.filterTxtDays();
                }
                else if(filter.equals("time")){
                    Search.filterTxtTimes();
                }
                else if(filter.equals("department")){
                    Search.filterTxtDepts();
                }
                else{
                    System.out.println("Not a valid filter");
                    printEndScreen();
                    printScreen();
                }
            }
            //etc

           else {
                System.out.println("- Command '" + command + "' not recognized.");
            }

            printEndScreen();
            printScreen();

           //if user is signed in...
            System.out.println("- Type what you'd like to do:");
            System.out.println("  (or type 'list' to see valid commands)\n");
            System.out.print(">");
            commandLn = mainScn.nextLine();
            st = new StringTokenizer(commandLn, " ");
            command = st.nextToken();
        };

        System.out.println("- Goodbye.\n");

    }
}
