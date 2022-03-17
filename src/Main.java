import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static void printCommands(){
        String[] commands = {"add", "remove", "undo", "redo", "list", "quit"};
        System.out.println("- Valid commands:");
        for(String s : commands){
            System.out.println("\t" + s);
        }
    }

    static void tempPrint(ArrayList<Course> schedule){

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

        //basic temp tests of addClass(), removeClass(), undo(), redo() - feel free to add your own
        //add
        System.out.println("Add:");
        cl.addClass(testC1,user.schedule);
        cl.updateHistory("add", testC1);
        cl.addClass(testC2, user.schedule);
        cl.updateHistory("add", testC2);

        tempPrint(user.schedule);

        //undo
        System.out.println("Undo:");
        cl.undo(user.schedule);
        tempPrint(user.schedule);

        //redo
        System.out.println("Redo:");
        cl.redo(user.schedule);
        tempPrint(user.schedule);

        //remove
        System.out.println("Remove:");
        cl.removeClass(testC1, user.schedule);
        tempPrint(user.schedule);


        Scanner mainScn = new Scanner(System.in);
        printScreen();
        System.out.println("- Welcome to the Class Scheduling Assistant!");
        System.out.println("- Type what you'd like to do:\n");
        System.out.println("login\t\tsign up\t\tquit\n");
        System.out.print(">");
        String command = mainScn.nextLine();

        while (!command.equals("quit")){
            if(command.equals("login")){
                //TODO
                System.out.println("- TODO: login");
                printEndScreen();
            }
           else if(command.equals("sign up")){
                //TODO
                System.out.println("- TODO: sign up");
                printEndScreen();
            }

           else if(command.equals("add")){
                //TODO
                System.out.println("- TODO: add");
                printEndScreen();
            }

            else if(command.equals("remove")){
                //TODO
                System.out.println("- TODO: remove");
                printEndScreen();
            }

            else if(command.equals("undo")){
                //TODO
                System.out.println("- TODO: undo");
                printEndScreen();
            }

            else if(command.equals("redo")){
                //TODO
                System.out.println("- TODO: redo");
                printEndScreen();
            }

            else if(command.equals("list")){
                printCommands();
                printEndScreen();
            }
            //etc

           else {
                System.out.println("- Command not recognized.");
                printEndScreen();
            }


            printScreen();

           //if user is signed in...
            System.out.println("- Type what you'd like to do:");
            System.out.println("  (or type 'list' to see valid commands)\n");
            System.out.print(">");
            command = mainScn.nextLine();
        };

        System.out.println("- Goodbye.\n");

    }
}
