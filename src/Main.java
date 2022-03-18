import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    static void printCommands(){
        String[] commands = {"view", "add", "remove", "undo", "redo", "list", "quit"};
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
        User user = new User("jimatheey123", "mypassword", "Jimatheey");

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
        System.out.println("login\t\tsign up\t\tquit\n");
        System.out.print(">");
        String commandLn = mainScn.nextLine();
        StringTokenizer st = new StringTokenizer(commandLn, " ");
        String command = st.nextToken();

        while (!command.equals("quit")){
            if(command.equals("login")){
                //TODO
                System.out.println("- TODO: login");

            }

            else if(command.equals("view")){
                //TODO: This may be changed later
                tempPrint(user.schedule);
            }

           else if(command.equals("sign up")){
                //TODO
                System.out.println("- TODO: sign up");
            }

           else if(command.equals("add")){
                //TODO
                System.out.println("- TODO: add");

            }

            else if(command.equals("remove")){
                //System.out.println("Contains: " + user.scheduleContains("BIOL 234 A"));
                String code = "";
                    while(st.hasMoreTokens()) {
                        code += st.nextToken();

                        if (st.hasMoreTokens()) {
                            code += " ";
                        }
                    }
                //System.out.println("Contains: " + user.scheduleContains(code));
                //System.out.println("code:" + code);
                    if(user.scheduleContains(code)) {
                        Course c = user.getCourse(code);
                        cl.removeClass(c, user.schedule);
                        tempPrint(user.schedule);
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
