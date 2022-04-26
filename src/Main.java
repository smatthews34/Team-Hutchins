import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    static void printCommands(){
        String[] commands = {"view", "add", "remove", "undo", "redo", "list", "filter", "quit", "confirm", "search", "activity", "lucky", "auto", "logout", "message", "resolve", "calendar", "clear"};
        System.out.println("- Valid commands:");
        for(String s : commands){
            System.out.println("\t" + s);
        }
    }

    static void tempPrint(ArrayList<Course> schedule){
    System.out.println("- Current Schedule:");
        for (int i = 0; i < schedule.size(); i++){
            System.out.println(schedule.get(i).courseCode + " " + schedule.get(i).shortTitle + " " + schedule.get(i).meets + " " + schedule.get(i).startTime + "-" +schedule.get(i).endTime);
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


    public static void main (String[] args) throws Exception {
        CourseList cl = new CourseList();
        User user = null;
        Logging lg;
        Boolean auto = false;

        Scanner mainScn = new Scanner(System.in);
        printInitScreen();
        System.out.println("- Welcome to the Class Scheduling Assistant!");

        boolean loggedIn = false;
        boolean isGuest = false; //Use to prevent a guest user from saving a schedule

        System.out.println("- Type what you'd like to do:\n");
        System.out.println("login\t\tsign_up\t\tguest\t\tquit\n");
        System.out.print(">");

        Scanner initScn = new Scanner(System.in);
        String initCommand = initScn.nextLine();
        //fixes the quit from main screen
        if (initCommand.equals("quit")){
            //save schedule if logged in
            if(loggedIn && !isGuest){
                ApachePOI.writeSchedule(user.username,user.schedule);
            }
            System.out.println("Goodbye!");
            System.exit(0);
        }

        while (!loggedIn && (!initCommand.equals("quit"))){

            if(initCommand.equals("login")){
                Scanner loginScn = new Scanner(System.in);
                System.out.println("Enter your username:");
                System.out.print(">");
                String username = loginScn.next();
                System.out.println("Enter your password:");
                System.out.print(">");
                String password = loginScn.next();

                Login l = new Login(username, password);
                User potentialUser = l.loginSubmit();

                if (potentialUser == null){
                    System.out.println("Username or Password are invalid, Please Try Again");
                }
                else {
                    System.out.println("Welcome, " + potentialUser.name + "!");
                    user = new User(potentialUser.username, potentialUser.password, potentialUser.name);
                    //if user has old schedule, read it in
                    user.schedule = ApachePOI.readSchedule(user.username);
                    //May copy to global User variable here
                    loggedIn = true;
                    break;
                }
            }

            else if (initCommand.equals("guest")){
                System.out.println("You are now signed in with a guest account, you may create a schedule " +
                        "but will not be able to save it until you sign up");
                user = new User("guest", "", "guest");
                loggedIn = true;
                isGuest = true;
                break;
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
                        System.out.println("There is already a registered account on this machine with the specified " +
                                "username, please use the 'sign_up' command to sign up with a different username");
                    }
                }
                //Scan user input for username and password, check for validity
                //If valid complete sign up & log in, if not valid redo prompt or exit
            }

            System.out.println("- Type what you'd like to do:\n");
            System.out.println("login\t\tsign_up\t\tguest\t\tquit\n");
            System.out.print(">");
            initCommand= initScn.nextLine();
        }

        printScreen(user.name);
        lg = new Logging(user.username);
        if(!user.username.equals("guest")){
            lg.Action(user.username + " has logged in and begun scheduling.");
        }else{
            lg.Action("A Guest has started to schedule");
        }
        System.out.println("- Type what you'd like to do:");
        System.out.println("  (or type 'list' to see valid commands)\n");
        System.out.print(">");

        String commandLn = mainScn.nextLine();
        StringTokenizer st = new StringTokenizer(commandLn, " "); //Like a second scanner that parses the line from mainScn
        String command = st.nextToken();
        System.out.println(command);

        while (!command.equals("quit") && !command.equals("logout")){

            if(command.equals("view")){
                //TODO: This may be changed later
                lg.Action(user.username + " entered \"view\".");
                tempPrint(user.schedule);
            }
           else if(command.equals("add")){
                //TODO
                Scanner add = new Scanner(System.in);
                String addOption = "";
                while(!addOption.equals("done")) {
                    System.out.println("Enter course in the format: \"CODE ### A\" or for a lab \"CODE ### A L\", to be added or enter 'done' if finished adding: ");
                    System.out.print(">");
                    addOption = add.nextLine();
                    if (!addOption.equals("done")) {
                        if(addOption.charAt(addOption.length()-1) == 'L' && addOption.length() == 12){
                            char c = addOption.charAt(addOption.length()-3);
                            addOption = addOption.substring(0,addOption.length()-3);
                            addOption = addOption + " " + c + "    L";
                        }else{
                            char c = addOption.charAt(addOption.length()-1);
                            addOption = addOption.substring(0,addOption.length()-1);
                            addOption = addOption + " " + c;
                        }
                        Course a = cl.getCourse(addOption);

                        if (a != null) {
                            Boolean c = cl.checkConfliction(a,user.schedule);
                            Boolean d = cl.checkDouble(a, user.schedule);
                            //cl.addClass(a, user.schedule);
                            //*****avoid the conflict and duplicate*****
                            //checks to see if the course being added is a duplicate.
                            if(cl.checkDouble(a, user.schedule)){
                                System.out.println("That course already is on your schedule, cannot be added.");
                            }else if(cl.checkConfliction(a, user.schedule)){ //checks to see if the course conflicts
                                System.out.println("There is a time conflict with your schedule."); //alerts the user there is a conflict
                                Scanner scn = new Scanner(System.in);
                                String answer = "";
                                while (!answer.equals("No")&&!answer.equals("no")&&!answer.equals("yes")&&!answer.equals("Yes")&&!answer.equals("N")&&!answer.equals("n")&&!answer.equals("Y")&&!answer.equals("y")) { //gives the user the ability to add if conflicting.
                                    System.out.println("Would you like to add anyway? (Y/N");
                                    answer = scn.next();
                                    if (answer.equals("Y") || answer.equals("y") || answer.equals("yes") || answer.equals("Yes")) {
                                        user.schedule.add(a);
                                        cl.addClass(a,user.schedule);
                                        System.out.println("Conflicting course added.");
                                        //cl.updateHistory("add", a);
                                        break;
                                    } else if (answer.equals("N") || answer.equals("n") || answer.equals("no") || answer.equals("No")) {
                                        System.out.println("Conflicting course was not added.");
                                        break;
                                    } else {
                                        System.out.println("Invalid response please select Y or N.");
                                    }
                                }
                            }else{ //if the course is not a duplicate or a not conflicting course it wil be added to the user's schedule.
                                //cl.updateHistory("add", a);
                                user.schedule.add(a);
                                cl.addClass(a,user.schedule);
                                System.out.println("The course has successfully been added to your schedule.");
                            }
                            //
                            if(d){
                                lg.logConflict(user.username + " has attempted to add the course: " + a + ", that is a duplicate of a course on their current schedule.");
                            }else if(c && user.schedule.contains(a)){
                                lg.logConflict(user.username + " added the course: " + a + " that conflicts with a course on their schedule.");
                            }else if(c && !user.schedule.contains(a)){
                                lg.logConflict(user.schedule + " has attempted to add the course: " + a + " that conflicts with their schedule but elected not to add it.");
                            }else{
                                lg.Action(user.username + " has added the course: " + a + ".");
                            }
                        } else {
                            System.out.println("Please enter a valid class.");
                            lg.logger.warning(user.username + " added the invalid course: " + addOption);
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
                        lg.Action(user.username + " Successfully removed the course: " + c);
                        System.out.println("Course removed.");
                        tempPrint(user.schedule);
                    }

                    else {
                        System.out.println("Error: Course not found.");
                        lg.logger.warning(user.username + " has tried to remove the invalid course: " + code );
                    }
            }

            else if(command.equals("undo")){
                cl.undo(user.schedule);
                tempPrint(user.schedule);
                lg.Action(user.username + " has undone their last action");

            }

            else if(command.equals("redo")){
                cl.redo(user.schedule);
                tempPrint(user.schedule);
                lg.Action(user.username + " has redone their last action");

            }

            else if(command.equals("resolve")){
                //
                Scanner res = new Scanner(System.in);
                String resInput = "";
                while (!resInput.equals("Done")&&!resInput.equals("done")&& ConfirmSchedule.countConflicts(user.schedule) > 0){
                    ArrayList<Course> con = cl.conflictResolution(user.schedule);
                    System.out.println("Conflicting Courses on your Current Schedule:");
                    for (int p = 0; p < con.size(); p++){
                        System.out.println(con.get(p));
                    }
                    System.out.println("Which course would you remove to help resolve?");
                    resInput = res.nextLine();
                    Course c = user.getCourse(resInput);
                    if(user.schedule.contains(c)) {
                        //Course c = user.getCourse(resInput);
                        cl.removeClass(c, user.schedule);
                        lg.Action(user.username + " Successfuly resolved a time conflict from the course: " + c);
                    }else if(resInput.equals("Done")||resInput.equals("done")){
                        System.out.println("You still have " + ConfirmSchedule.countConflicts(user.schedule) + " conflict(s) remaining.");
                        lg.logConflict(user.username + " after trying to resolves conflicts still has " + ConfirmSchedule.countConflicts(user.schedule) + " conflict(s) remaining.");
                        break;
                    }else{
                        System.out.println("Invalid Response.");
                        lg.logger.warning(user.username + " has enter an invalid input for the conflict resolution screen.");
                    }
                }
                if(ConfirmSchedule.countConflicts(user.schedule) > 0){
                    System.out.println("There are currently no conflicts with your schedule.");
                }
            }

            else if(command.equals("message")){
                System.out.println("Sorry :( Due To The Grove City FireWall the Feature will not work.");
                /*CellNote c = new CellNote();
                String provider = "";
                String phone = "";
                Scanner messageScan = new Scanner(System.in);

                while (!provider.equals("done")){
                    System.out.println("Who is you cell phone provider?");
                    provider = messageScan.next();
                    if(provider.equals("AT&T")||provider.equals("Boost")||provider.equals("Cricket")||provider.equals("Google")||provider.equals("Republic")||provider.equals("Sprint")||provider.equals("Straight")||provider.equals("T-Mobile")||provider.equals("Ting")||provider.equals("U.S.")||provider.equals("Verizon")||provider.equals("Virgin")){
                        break;
                    }else{
                        System.out.println("Sorry we do not have your provider on file. Please try again or enter 'done' if finished.");
                        provider = messageScan.next();
                        if (provider.equals("done")||provider.equals("Done")){
                            break;
                        }
                    }
                }
                while ((!provider.equals("done")||!provider.equals("Done")) && (!phone.equals("done")||!phone.equals("Done"))){
                    System.out.println("Please enter your ten-digit phone number without the dashes or parenthesis(Example: 5554748044)");
                    phone = messageScan.next();
                    if (phone.length() == 10){
                        break;
                    }else{
                        System.out.println("Incorrect length number please try again or enter 'done' now to quit");
                        phone = messageScan.next();
                        if (phone.equals("done")||phone.equals("Done")){
                            break;
                        }
                    }
                }
                if((!provider.equals("done")||!provider.equals("Done")) && (!phone.equals("done")||!phone.equals("Done"))){
                    c.sendNotification(provider, phone, user.schedule);
                }else{
                    System.out.println("Either invalid phone number or cell provider.");
                }
                */

            }

            else if(command.equals("auto")){
                Scanner autoScn = new Scanner(System.in);
                String year ="";
                String semester ="";
                if (auto !=true){
                    while(!year.equals("Fresh")&&!year.equals("Soph")&&!year.equals("Junior")&&!year.equals("Senior")&&!year.equals("fresh")&&!year.equals("soph")&&!year.equals("junior")&&!year.equals("senior")){
                        System.out.println("What year are you? (Fresh, Soph, Junior, Senior)");
                        year = autoScn.next();
                    }
                    while(!semester.equals("F")&&!semester.equals("S")&&!semester.equals("f")&&!semester.equals("s")){
                        System.out.println("What Semester? ('F' for Fall or 'S' for Spring)");
                        semester = autoScn.next();
                    }
                    cl.autoFill(year,semester, user.schedule);
                    System.out.println("Your Schedule has successfully been generated with the base requirements of a " + year + " during the " + semester + " semester.");
                    lg.Action(user.username + " has auto-generated their course schedule for " + year + " in the " + semester + " semester.");
                    auto = true;
                }else{
                    System.out.println("You have already completed an auto schedule.");
                    lg.logger.warning(user.username + " attempted to auto-generate their schedule but they already have.");
                }
            }

            else if(command.equals("lucky")){
                Scanner luckyScn = new Scanner(System.in);
                String lucky = "";
                while(!lucky.equals("N")||!lucky.equals("n")) {
                    System.out.println("Are you feeling lucky? (Y/N)");
                    lucky = luckyScn.next();
                    if (lucky.equals("Y") || lucky.equals("y")) {
                        cl.FeelingLucky(user.schedule);
                        lg.Action(user.username + " was feeling lucky and added the course: " + user.schedule.get(user.schedule.size()-1) + " to their schedule.");
                        System.out.println("Would you like to try again?(Y/N)");
                        if(lucky.equals("N") || lucky.equals("n")){
                            System.out.println("Guess you're not feeling lucky anymore...");
                            lg.Action(user.username + " was not feeling lucky anymore.");
                        }
                    }else{
                        System.out.println("Not feeling lucky I guess...");
                        lg.Action(user.username + " is not feeling lucky anymore.");
                        break;
                    }
                }

            }

            else if(command.equals("activity")){
                Scanner ActScn = new Scanner(System.in);
                String act = "";
                String title, start, end, meets;
                while(!act.equals("done") || !act.equals("Done")){
                    System.out.println("Would you like to add an activity to your schedule? If yes enter 'yes'. If not enter 'done'");
                    act = ActScn.next();
                    if(act.equals("yes")||act.equals("Yes")||act.equals("Y")||act.equals("y")){

                        System.out.println("Enter the start time of the Activity.(In military time; ex. 8:00:00)");
                        start = ActScn.next();
                        System.out.println("Enter the start time of the Activity.(In military time; ex. 13:00:00)");
                        end = ActScn.next();
                        System.out.println("Enter the day(s) that the Activity occurs on.(Ex MWF)");
                        meets = ActScn.next();
                        System.out.println("What is the Name of your Activity you are participating in?");
                        title = ActScn.nextLine();
                        Course c = new Course(title, start, end, meets);
                        Boolean cc = cl.checkConfliction(c,user.schedule);
                        Boolean d = cl.checkDouble(c, user.schedule);
                        cl.addClass(c, user.schedule);
                        if(d){
                            lg.logConflict(user.username + " has attempted to add a personal activity: " + c + " that they already have on their schedule.");
                        }else if(cc && user.schedule.contains(c)){
                            lg.logConflict(user.username + " added the activity: " + c + " that conflicts with a course/activity on their schedule.");
                        }else if(cc && !user.schedule.contains(c)){
                            lg.logConflict(user.schedule + " has attempted to add the activity: " + c + ", that conflicts with their schedule but elected not to add it.");
                        }else{
                            lg.Action(user.username + " has added the activity " + c);
                        }
                    }
                    System.out.println("Would you like to add another activity? (Y/N)");
                    act = ActScn.next();
                    if(act.equals("N")||act.equals("No")||act.equals("n")||act.equals("no")){
                        lg.Action(user.username + " has elected not to add another activity.");
                        break;
                    }else if(!act.equals("Y")||!act.equals("y")||!act.equals("Yes")||!act.equals("yes")){
                        System.out.println("Invalid response, exiting to main screen.");
                        lg.logger.warning(user.username + " has inputted an invalid response when asked if they would like to add another personal activity.");
                    }
                }
            }

            else if(command.equals("list")){
                lg.Action(user.username + " has entered the \"list\" command");
                printCommands();

            }

            else if(command.equals("clear")){
                String clear = "";
                Scanner clearer = new Scanner(System.in);
                clear = clearer.next();
                System.out.println("Would you like to clear the schedule? (yes/no) Enter done to quit");
                while(!clear.equalsIgnoreCase("done")){
                    if(clear.equalsIgnoreCase("yes")){
                        CourseList.courseList.clear();
                    }
                    else if(clear.equalsIgnoreCase("no")){
                        System.out.println("Here are the list of commands");
                        printCommands();
                    }
                    else{
                        System.out.println("Please enter yes, no, or done");
                        clear = clearer.next();
                    }
                }
                auto = false;
            }

            else if(command.equals("filter")) {
                Scanner filterSCNR = new Scanner(System.in);
                Scanner add = new Scanner(System.in);
                String filter = "";
                while(!filter.equals("done")) {
                    System.out.println("What would you like to filter by?");
                    System.out.println("The options are to filter by: days, time, department, building or done to exit");
                    System.out.println("To search by days, enter the first capital letter of that day, ex: MWF");
                    System.out.println("To search by times, enter the start time in military time, ex: 8:00:00");
                    System.out.println("To search by department, enter the code in all capital letters. ex: COMP");
                    System.out.println("To search by building, enter the building code in all capital letters. ex: STEM");
                    System.out.println("Or enter 'done' if you are done filtering the course.");
                    filter = filterSCNR.nextLine();
                    if(filter.equals("done")||filter.equals("Done")){
                        //break;
                    }else if (filter.equals("days")) {
                        Search.filterTxtDays();
                        lg.Action(user.username + " applied a filter to all of the courses by day(s) the course meets");
                        //break;
                    } else if (filter.equals("time")) {
                        Search.filterTxtTimes();
                        lg.Action(user.username + " applied a filter to all of the courses by the time the course occurs.");
                        //break;
                    } else if (filter.equals("department")) {
                        Search.filterTxtDepts();
                        lg.Action(user.username + " applied a filter to all of the courses by the department of the courses.");
                        //break;
                    }else if(filter.equals("building")){
                        Search.filterTxtBuildings();
                        lg.Action(user.username + " applied a filter to all of the courses by the building of the courses.");
                            //break;
                    }
                    else {
                        System.out.println("Invalid filter.");
                        lg.logger.warning(user.username + " username tried filter the courses by an invalid filter option.");
                    }

                    System.out.println("Would you like to add from the filtered results (Yes/No)");
                    if(filter.equalsIgnoreCase("yes")){
                        String addO = "";
                        while(!addO.equals("done")) {
                            System.out.println("Enter course in the format: \"CODE ### A\" or for a lab \"CODE ### A L\", to be added or enter 'done' if finished adding: ");
                            System.out.print(">");
                            addO = add.nextLine();
                            if (!addO.equals("done")) {
                                if(addO.charAt(addO.length()-1) == 'L' && addO.length() == 12){
                                    char c = addO.charAt(addO.length()-3);
                                    addO = addO.substring(0,addO.length()-3);
                                    addO = addO + " " + c + "    L";
                                }else{
                                    char c = addO.charAt(addO.length()-1);
                                    addO = addO.substring(0,addO.length()-1);
                                    addO = addO + " " + c;
                                }
                                Course a = cl.getCourse(addO);

                                if (a != null) {
                                    Boolean c = cl.checkConfliction(a,user.schedule);
                                    Boolean d = cl.checkDouble(a, user.schedule);
                                    //cl.addClass(a, user.schedule);
                                    //*****avoid the conflict and duplicate*****
                                    //checks to see if the course being added is a duplicate.
                                    if(cl.checkDouble(a, user.schedule)){
                                        System.out.println("That course already is on your schedule, cannot be added.");
                                    }else if(cl.checkConfliction(a, user.schedule)){ //checks to see if the course conflicts
                                        System.out.println("There is a time conflict with your schedule."); //alerts the user there is a conflict
                                        Scanner scn = new Scanner(System.in);
                                        String answer = "";
                                        while (!answer.equals("No")&&!answer.equals("no")&&!answer.equals("yes")&&!answer.equals("Yes")&&!answer.equals("N")&&!answer.equals("n")&&!answer.equals("Y")&&!answer.equals("y")) { //gives the user the ability to add if conflicting.
                                            System.out.println("Would you like to add anyway? (Y/N");
                                            answer = scn.next();
                                            if (answer.equals("Y") || answer.equals("y") || answer.equals("yes") || answer.equals("Yes")) {
                                                user.schedule.add(a);
                                                cl.addClass(a,user.schedule);
                                                System.out.println("Conflicting course added.");
                                                //cl.updateHistory("add", a);
                                                break;
                                            } else if (answer.equals("N") || answer.equals("n") || answer.equals("no") || answer.equals("No")) {
                                                System.out.println("Conflicting course was not added.");
                                                break;
                                            } else {
                                                System.out.println("Invalid response please select Y or N.");
                                            }
                                        }
                                    }else{ //if the course is not a duplicate or a not conflicting course it wil be added to the user's schedule.
                                        //cl.updateHistory("add", a);
                                        user.schedule.add(a);
                                        cl.addClass(a,user.schedule);
                                        System.out.println("The course has successfully been added to your schedule.");
                                    }
                                    //
                                    if(d){
                                        lg.logConflict(user.username + " has attempted to add the course: " + a + ", that is a duplicate of a course on their current schedule.");
                                    }else if(c && user.schedule.contains(a)){
                                        lg.logConflict(user.username + " added the course: " + a + " that conflicts with a course on their schedule.");
                                    }else if(c && !user.schedule.contains(a)){
                                        lg.logConflict(user.schedule + " has attempted to add the course: " + a + " that conflicts with their schedule but elected not to add it.");
                                    }else{
                                        lg.Action(user.username + " has added the course: " + a + ".");
                                    }
                                } else {
                                    if (!addO.equalsIgnoreCase("done")){
                                        System.out.println("Please enter a valid class.");
                                        lg.logger.warning(user.username + " added the invalid course: " + addO);
                                    }
                                }
                            }
                        }
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
                            lg.Action(user.username + " has confirmed and saved their schedule with " + conflicts + " conflict(s).");
                            System.out.println("Your schedule has been confirmed. See file.");
                            confirmed = true;

                        } else if (answer.equalsIgnoreCase("NO") || answer.equalsIgnoreCase("N")) {
                            System.out.println("Type a new command.");
                            lg.Action(user.username + " has denied to confirm and saved their schedule.");
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
                    lg.Action(user.username + " has confirmed and saved their schedule with zero conflicts.");
                    System.out.println("Your schedule has been confirmed. See file.");
                }
                System.out.println("Would you like to have the schedule emailed to you? (yes/no)");
                Scanner mailScanner = new Scanner(System.in);
                String mail = mailScanner.next();
                if(mail.equalsIgnoreCase("Yes")){
                    System.out.println("Please enter your email address");
                    Scanner address = new Scanner(System.in);
                    String send = address.next();
                    email.emailSender(send);
                }
                else if(mail.equalsIgnoreCase("no")){
                    System.out.println("Schedule was not emailed. Thank you!");
                }
                else{
                    System.out.println("Please enter yes or no");
                    mail = mailScanner.next();
                }
            }
            else if(command.equals("calendar")){
                int conflicts = ConfirmSchedule.countConflicts(user.schedule);

                ConfirmSchedule.printCalendar(user.schedule);
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
                            //catch if int is out of bounds
                            dayOfMonth = Integer.parseInt(calendar);
                            if(dayOfMonth > 31){
                                System.out.println("That integer is out of bounds.");
                                continue;
                            }

                            //print the day
                            LocalDate userDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), dayOfMonth);
                            System.out.println(userDate.getDayOfWeek() + " " + userDate.getMonth() + " " + dayOfMonth);

                            if (ConfirmSchedule.findDayOfWeek(dayOfMonth) == 1 || ConfirmSchedule.findDayOfWeek(dayOfMonth) == 7) {
                                System.out.println("There are no classes on the weekend");
                                lg.logConflict(user.username + " has tried to print a day of the week where there are no courses or activities scheduled for.");
                            } else {
                                ArrayList<Course> classes = ConfirmSchedule.classesPerDay(user.schedule
                                        , ConfirmSchedule.findDayOfWeek(dayOfMonth), false);
                                System.out.println(ConfirmSchedule.courseListString(classes));
                                int conflictsPerDay = ConfirmSchedule.countConflicts(classes);
                                System.out.println(conflictsPerDay + " conflict(s) on this day.");
                                lg.Action(user.username + " has printed out their schedule for " + dayOfMonth + " where the day has " + conflictsPerDay + " conflict(s)");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input.");
                            lg.logger.warning(user.username + " has enter and invalid input for the calendar view command.");
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
                    lg.logger.warning(user.username + " has performed a search that yielded no results for: "+ searchInput);
                }
                else {
                    s.printResults(courses);
                    Scanner add = new Scanner(System.in);
                    String addOption = "";
                    lg.Action(user.username + " has performed a successful search for: " + searchInput);
                    System.out.println("Would you like to add a course from the search? (Yes/No)");
                    System.out.print(">");
                    addOption = searchScan.next();
                    if (addOption.equals("yes") || addOption.equals("Yes")) {
                        while (!addOption.equals("No") && !addOption.equals("no")) {
                            System.out.println("Enter course in the format: CODE ### A, to be added or enter 'no' if finished adding: ");
                            System.out.print(">");
                            addOption = add.nextLine();
                            if (!addOption.equals("No") && !addOption.equals("no")) {
                                Course a = cl.getCourse(addOption);
                                if (a != null) {
                                    Boolean c = cl.checkConfliction(a, user.schedule);
                                    Boolean d = cl.checkDouble(a, user.schedule);

                                    cl.addClass(a, user.schedule);
                                    if (d) {
                                        lg.logConflict(user.username + " has attempted to add from the search the course: " + a + ", that conflicts with a course on their current schedule.");
                                    } else if (c && user.schedule.contains(a)) {
                                        lg.logConflict(user.username + " added the course: " + a + " from the search that conflicts with a course on their schedule.");
                                    } else if (c && !user.schedule.contains(a)) {
                                        lg.logConflict(user.schedule + " has attempted to add from search the course: " + a + ", that conflicts with their schedule but elected not to add it.");
                                    } else {
                                        lg.Action(user.username + " has added the course: " + a + " from the search");
                                    }
                                    System.out.println("Would you like to add another course from the search? (Yes/No)");
                                    System.out.print(">");
                                    addOption = add.next();
                                } else {
                                    System.out.println("Please enter a valid class.");
                                    lg.logger.warning(user.username + " tried to add the invalid course: " + addOption + " from the search.");
                                }
                            }
                        }
                    }
                    String filterOption = "";
                    System.out.println("Would you like to filter your search results? (Yes/No)");
                    System.out.print(">");
                    filterOption = searchScan.next();
                    if (filterOption.equalsIgnoreCase("yes")) {
                        ArrayList<Course> filteredResults = new ArrayList<>();
                        boolean wrong = false;
                        while (true) {
                            System.out.println("Continue filtering or use command 'done' when finished");
                            //Filter, rerun
                            System.out.println("What would you like to filter by?: department, time, days");
                            String cmd = searchScan.next();
                            if (cmd.equalsIgnoreCase("department")) {
                                //Filter by department
                                //Get list of results (Courses), Apply filter to that data
                                System.out.println("Enter department code that you would like to filter by");
                                String deptCode = searchScan.next();
                                for (int i = 0; i < courses.size(); i++) {
                                    //Check to see if valid
                                    if (courses.get(i).courseCode.toLowerCase().contains(deptCode.toLowerCase())) {
                                        filteredResults.add(courses.get(i));
                                    }
                                }
                            } else if (cmd.equalsIgnoreCase("done")) {
                                break;
                            } else if (cmd.equalsIgnoreCase("time")){
                                System.out.println("Enter course start time that you would like to filter by as 'Hours:Minutes' using Military Time (i.e. 14:00)");
                                String time = searchScan.next();
                                if (time.contains(":") && !time.equalsIgnoreCase("1:00")) {
                                    for (int i = 0; i < courses.size(); i++) {
                                        //Check to see if valid
                                        if (courses.get(i).startTime.toLowerCase().contains(time.toLowerCase())) {
                                            filteredResults.add(courses.get(i));
                                        }
                                    }
                                }
                                else if (time.contains(":") && time.equalsIgnoreCase("1:00")){
                                    for (int i = 0; i < courses.size(); i++) {
                                        //Check to see if valid
                                        if (courses.get(i).startTime.toLowerCase().equals("1:00")) {
                                            filteredResults.add(courses.get(i));
                                        }
                                    }
                                }
                            } else if (cmd.equalsIgnoreCase("days")){
                                //Filter by days
                                System.out.println("Enter days that you would like to filter by (M,T,W,TR,F), enter all days (i.e. MWF)");
                                String days = searchScan.next();
                                for (int i = 0; i < courses.size(); i++) {
                                    //Check to see if valid
                                    if (courses.get(i).meets.equalsIgnoreCase(days.toLowerCase())) {
                                        filteredResults.add(courses.get(i));
                                    }
                                }
                            }
                            else{
                                System.out.println("Invalid filter. Please retry with a different filter");
                                wrong = true;
                            }
                            if (!filteredResults.isEmpty()){
                                s.printResults(filteredResults);
                                System.out.println("Would you like to add a class from the filtered results? (Yes/No)");
                                if (searchScan.next().equalsIgnoreCase("yes")){
                                    String addO = "";
                                    while(!addO.equals("done")) {
                                        System.out.println("Enter course in the format: \"CODE ### A\" or for a lab \"CODE ### A L\", to be added or enter 'done' if finished adding: ");
                                        System.out.print(">");
                                        addO = add.nextLine();
                                        if (!addO.equals("done")) {
                                            if(addO.charAt(addO.length()-1) == 'L' && addO.length() == 12){
                                                char c = addO.charAt(addO.length()-3);
                                                addO = addO.substring(0,addO.length()-3);
                                                addO = addO + " " + c + "    L";
                                            }else{
                                                char c = addO.charAt(addO.length()-1);
                                                addO = addO.substring(0,addO.length()-1);
                                                addO = addO + " " + c;
                                            }
                                            Course a = cl.getCourse(addO);

                                            if (a != null) {
                                                Boolean c = cl.checkConfliction(a,user.schedule);
                                                Boolean d = cl.checkDouble(a, user.schedule);
                                                //cl.addClass(a, user.schedule);
                                                //*****avoid the conflict and duplicate*****
                                                //checks to see if the course being added is a duplicate.
                                                if(cl.checkDouble(a, user.schedule)){
                                                    System.out.println("That course already is on your schedule, cannot be added.");
                                                }else if(cl.checkConfliction(a, user.schedule)){ //checks to see if the course conflicts
                                                    System.out.println("There is a time conflict with your schedule."); //alerts the user there is a conflict
                                                    Scanner scn = new Scanner(System.in);
                                                    String answer = "";
                                                    while (!answer.equals("No")&&!answer.equals("no")&&!answer.equals("yes")&&!answer.equals("Yes")&&!answer.equals("N")&&!answer.equals("n")&&!answer.equals("Y")&&!answer.equals("y")) { //gives the user the ability to add if conflicting.
                                                        System.out.println("Would you like to add anyway? (Y/N");
                                                        answer = scn.next();
                                                        if (answer.equals("Y") || answer.equals("y") || answer.equals("yes") || answer.equals("Yes")) {
                                                            user.schedule.add(a);
                                                            cl.addClass(a,user.schedule);
                                                            System.out.println("Conflicting course added.");
                                                            //cl.updateHistory("add", a);
                                                            break;
                                                        } else if (answer.equals("N") || answer.equals("n") || answer.equals("no") || answer.equals("No")) {
                                                            System.out.println("Conflicting course was not added.");
                                                            break;
                                                        } else {
                                                            System.out.println("Invalid response please select Y or N.");
                                                        }
                                                    }
                                                }else{ //if the course is not a duplicate or a not conflicting course it wil be added to the user's schedule.
                                                    //cl.updateHistory("add", a);
                                                    user.schedule.add(a);
                                                    cl.addClass(a,user.schedule);
                                                    System.out.println("The course has successfully been added to your schedule.");
                                                }
                                                //
                                                if(d){
                                                    lg.logConflict(user.username + " has attempted to add the course: " + a + ", that is a duplicate of a course on their current schedule.");
                                                }else if(c && user.schedule.contains(a)){
                                                    lg.logConflict(user.username + " added the course: " + a + " that conflicts with a course on their schedule.");
                                                }else if(c && !user.schedule.contains(a)){
                                                    lg.logConflict(user.schedule + " has attempted to add the course: " + a + " that conflicts with their schedule but elected not to add it.");
                                                }else{
                                                    lg.Action(user.username + " has added the course: " + a + ".");
                                                }
                                            } else {
                                                if (!addO.equalsIgnoreCase("done")){
                                                    System.out.println("Please enter a valid class.");
                                                    lg.logger.warning(user.username + " added the invalid course: " + addO);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else if (!wrong) System.out.println("No results, try editing filter");
                            filteredResults.clear();
                        }
                    }
                }
            }


            else {
                System.out.println("- Command '" + command + "' not recognized.");
                lg.logger.warning("Invalid Input: " + command);
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

        if (command.equals("logout")){
            //save schedule if logout
            if(!isGuest) {
                ApachePOI.writeSchedule(user.username, user.schedule);
            }
            lg.Action(user.username + " has logged out.");
            main(null);
        }
        lg.Action(user.username + " has quit the application.");
    }
}
