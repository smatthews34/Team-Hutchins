import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    static void printCommands(){
        String[] commands = {"view", "add", "remove", "undo", "redo", "list", "filter","quit", "confirm", "search", "activity"};
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

    static void printInitScreen(){
        System.out.println("\n____________________________________________________________________________");
        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println(" Team Hutchins\t\t\tCLASS SCHEDULING ASSISTANT");
        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\n");
    }

    static void printScreen(String name) {
        System.out.println("\n____________________________________________________________________________");
        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println(" Team Hutchins\t\t\tCLASS SCHEDULING ASSISTANT \t\t\tUser: " + name + " ");
        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\n");
    }


    public static void main (String[] args) throws FileNotFoundException {
        CourseList cl = new CourseList();
        User user = null;

        Scanner mainScn = new Scanner(System.in);
        printInitScreen();
        System.out.println("- Welcome to the Class Scheduling Assistant!");

        boolean loggedIn = false;

        System.out.println("- Type what you'd like to do:\n");
        System.out.println("login\t\tsign_up\t\tquit\n");
        System.out.print(">");

        Scanner initScn = new Scanner(System.in);
        String initCommand = initScn.nextLine();

        while (!loggedIn && (!initCommand.equals("quit"))){

            if(initCommand.equals("login")){
                File file = new File("encryptedInfo.des");
                Scanner loginScn = new Scanner(System.in);
                System.out.println("Enter your username:");
                System.out.print(">");
                String username = loginScn.next();
                System.out.println("Enter your password:");
                System.out.print(">");
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
                    System.out.println("Welcome, " + potentialUser.name + "!");
                    user = new User(potentialUser.username, potentialUser.password, potentialUser.name);
                    //May copy to global User variable here
                    loggedIn = true;
                    break;
                }
            }

            else if(initCommand.equals("sign_up")){ //Make sure this can be accessed with space, change to sign up later
                Scanner signScn = new Scanner(System.in);
                System.out.println("Enter new username");
                System.out.print(">");
                String username = signScn.next();
                System.out.println("Enter new password");
                System.out.print(">");
                String password = signScn.next();
                System.out.println("Enter your name");
                System.out.print(">");
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

            System.out.println("- Type what you'd like to do:\n");
            System.out.println("login\t\tsign_up\t\tquit\n");
            System.out.print(">");
            initCommand= initScn.nextLine();
        }

        printScreen(user.name);
        System.out.println("- Type what you'd like to do:");
        System.out.println("  (or type 'list' to see valid commands)\n");
        System.out.print(">");

        String commandLn = mainScn.nextLine();
        StringTokenizer st = new StringTokenizer(commandLn, " "); //Like a second scanner that parses the line from mainScn
        String command = st.nextToken();
        System.out.println(command);

        while (!command.equals("quit")){

            if(command.equals("view")){
                //TODO: This may be changed later
                tempPrint(user.schedule);
            }
           else if(command.equals("add")){
                //TODO
                Scanner add = new Scanner(System.in);
                String addOption = "";
                while(!addOption.equals("done")) {
                    System.out.println("Enter course in the format: CODE ### A, to be added or enter 'done' if finished adding: ");
                    System.out.print(">");
                    addOption = add.nextLine();
                    if (!addOption.equals("done")) {
                        Course a = cl.getCourse(addOption);

                        if (a != null) {
                            cl.addClass(a, user.schedule);
                        } else {
                            System.out.println("Please enter a valid class.");
                        }
                    }
                }
                //add.close();
            }

            else if(command.equals("remove")){  //user input should take the form 'remove ACCT 202 B'
                String code = "";
                int codeSection = 1;
                    while(st.hasMoreTokens()) {
                        code += st.nextToken();     //Concat course code like ACCT 202 B

                        if (st.hasMoreTokens()) {
                            if (codeSection != 2) {
                                code += " ";
                            }
                            else {
                                code += "  ";
                            }
                        }
                        codeSection++;
                    }

                    if(user.scheduleContains(code)) {
                        Course c = user.getCourse(code);
                        cl.removeClass(c, user.schedule);
                        System.out.println("Course removed.");
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

            else if(command.equals("activity")){
                Scanner ActScn = new Scanner(System.in);
                String act = "";
                String title, start, end, meets;
                while(!act.equals("done") || !act.equals("Done")){
                    System.out.println("Would you like to add an activity to your schedule? If yes enter 'yes'. If not enter 'done'");
                    act = ActScn.next();
                    if(act.equals("yes")||act.equals("Yes")||act.equals("Y")||act.equals("y")){
                        System.out.println("What is the Name of your Activity you are participating in?");
                        title = ActScn.nextLine();
                        System.out.println("Enter the start time of the Activity.(In military time; ex. 8:00:00)");
                        start = ActScn.next();
                        System.out.println("Enter the start time of the Activity.(In military time; ex. 13:00:00)");
                        end = ActScn.next();
                        System.out.println("Enter the day(s) that the Activity occurs on.(Ex MWF)");
                        meets = ActScn.next();
                        Course c = new Course(title, start, end, meets);
                        cl.addClass(c, user.schedule);
                    }
                    System.out.println("Would you like to add another activity? (Y/N)");
                    act = ActScn.next();
                    if(act.equals("N")||act.equals("No")||act.equals("n")||act.equals("no")){
                        break;
                    }
                }
            }

            else if(command.equals("list")){
                printCommands();

            }

            else if(command.equals("filter")) {
                Scanner filterSCNR = new Scanner(System.in);
                String filter = "";
                while(!filter.equals("done")) {
                    System.out.println("What would you like to filter by?");
                    System.out.println("The options are to filter by: days, time, department or done to exit");
                    System.out.println("To search by days, enter the first capital letter of that day, ex: MWF");
                    System.out.println("To search by times, enter the start time in military time, ex: 8:00:00");
                    System.out.println("To search by department, enter the code in all capital letters. ex: COMP");
                    System.out.println("Or enter 'done' if you are done filtering the course.");
                    filter = filterSCNR.nextLine();
                    if(filter.equals("done")||filter.equals("Done")){
                        //break;
                    }else if (filter.equals("days")) {
                        Search.filterTxtDays();
                        //break;
                    } else if (filter.equals("time")) {
                        Search.filterTxtTimes();
                        //break;
                    } else if (filter.equals("department")) {
                        Search.filterTxtDepts();
                        //break;
                    }else {
                        System.out.println("Invalid filter.");
                    }
                }
            }
            else if(command.equals("confirm")){
                Scanner confirmScan = new Scanner(System.in);
                int conflicts = ConfirmSchedule.countConflicts(user.schedule);

                if (conflicts > 0) {
                    System.out.println(conflicts + " conflict(s) exist, would you still like to" +
                            " confirm? (Y/N)");
                    System.out.print(">");
                    String answer = confirmScan.next();
                    boolean confirmed = false;
                    while (!confirmed) {
                        if (answer.equalsIgnoreCase("YES") || answer.equalsIgnoreCase("Y")) {
                            System.out.println(conflicts + " conflict(s) exists.");
                            ConfirmSchedule.scheduleFile(user.schedule);
                            System.out.println("Your schedule has been confirmed. See file.");
                            confirmed = true;

                        } else if (answer.equalsIgnoreCase("NO") || answer.equalsIgnoreCase("N")) {
                            System.out.println("Type a new command.");
                            confirmed = true;

                        } else {
                            System.out.println("Invalid input. Type Yes or No.");
                            System.out.print(">");
                            answer = confirmScan.next();
                        }
                    }
                }
                else {
                    System.out.println(conflicts + " conflicts exist. Confirming schedule now.");
                    ConfirmSchedule.scheduleFile(user.schedule);
                    System.out.println("Your schedule has been confirmed. See file.");
                }
            }
            else if(command.equals("calendar")){
                int conflicts = ConfirmSchedule.countConflicts(user.schedule);

                ConfirmSchedule.printCalendar();
                System.out.println("There are " + conflicts + " conflict(s) in your schedule.");

                Scanner calendarScan = new Scanner(System.in);
                String calendar = "";
                int dayOfMonth;

                //a while loop structure so they can observe multiple days
                while (!calendar.equals("done")){
                    System.out.println("Enter a day of the month to view classes, type 'ind' to view independent studies," +
                            " or enter 'done' to exit");
                    System.out.print(">");
                    calendar = calendarScan.next();

                    if(calendar.equals("ind")){
                        ArrayList<Course> classes = ConfirmSchedule.classesPerDay(user.schedule
                                , 8, true);
                        System.out.println(ConfirmSchedule.courseListString(classes));
                    }
                    if(!calendar.equals("ind") && !calendar.equals("done")) {
                        try {
                            //need to catch exceptions if the integer is > days in month
                            dayOfMonth = Integer.parseInt(calendar);

                            if (ConfirmSchedule.findDayOfWeek(dayOfMonth) == 1 || ConfirmSchedule.findDayOfWeek(dayOfMonth) == 7) {
                                System.out.println("There are no classes on the weekend");
                            } else {
                                ArrayList<Course> classes = ConfirmSchedule.classesPerDay(user.schedule
                                        , ConfirmSchedule.findDayOfWeek(dayOfMonth), false);
                                System.out.println(ConfirmSchedule.courseListString(classes));
                                int conflictsPerDay = ConfirmSchedule.countConflicts(classes);
                                System.out.println(conflictsPerDay + " conflict(s) on this day.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input.");
                        }
                    }
                }
            }
            else if(command.equals("search")){
                Scanner searchScan = new Scanner(System.in);
                System.out.println("Enter search term:");
                System.out.print(">");
                String searchInput = searchScan.nextLine();
                ArrayList<Course> courses = new ArrayList<>();
                Search s = new Search(searchInput);
                courses = s.getResults(searchInput);
                if (courses.isEmpty()) {
                    System.out.println("Search yielded no results, please try again");
                }
                else {
                    s.printResults(courses);
                    Scanner add = new Scanner(System.in);
                    String addOption = "";
                    System.out.println("Would you like to add a course from the search? (Yes/No)");
                    System.out.print(">");
                    addOption = searchScan.next();
                    if(addOption.equals("yes")||addOption.equals("Yes")) {
                        while (!addOption.equals("No") && !addOption.equals("no")) {
                            System.out.println("Enter course in the format: CODE ### A, to be added or enter 'no' if finished adding: ");
                            System.out.print(">");
                            addOption = add.nextLine();
                            if (!addOption.equals("No") && !addOption.equals("no")) {
                                Course a = cl.getCourse(addOption);
                                if (a != null) {
                                    cl.addClass(a, user.schedule);
                                    System.out.println("Would you like to add another course from the search? (Yes/No)");
                                    System.out.print(">");
                                    addOption = add.next();
                                } else {
                                    System.out.println("Please enter a valid class.");
                                }
                            }
                        }
                    }
                }
            }


            else {
                System.out.println("- Command '" + command + "' not recognized.");
            }

            printScreen(user.name);

           //if user is signed in...
            System.out.println("- Type what you'd like to do:");
            System.out.println("  (or type 'list' to see valid commands)\n");
            System.out.print(">");

            commandLn = mainScn.nextLine();
            st = new StringTokenizer(commandLn, " ");
            command = st.nextToken();
        }

        System.out.println("- Goodbye.\n");

    }
}
