import java.text.SimpleDateFormat;
import java.util.*;


public class CourseList {
    Stack<Course> courseHist = new Stack<>();
    Stack<String> commandHist = new Stack<>();

    Stack<Course> undoCourseHist = new Stack<>();
    Stack<String> undoCommandHist = new Stack<>();


    private void updateHistory(String command, Course course){
        commandHist.push(command);
        courseHist.push(course);
    }


    public void undo(ArrayList<Course> schedule){
        if (!courseHist.isEmpty() && !commandHist.isEmpty()) {
            String lastCommand = commandHist.pop();
            Course lastCourse = courseHist.pop();

            //do opposite of last command to applicable course
            if (lastCommand.equals("add")) {
                schedule.remove(lastCourse);
            }

            else if (lastCommand.equals("remove")) {
                schedule.add(lastCourse);
            }

            undoCommandHist.push(lastCommand);
            undoCourseHist.push(lastCourse);
        }
        else {
            System.out.println("No actions to undo.");
        }
    }

    public void redo(ArrayList<Course> schedule) {

        if (!undoCommandHist.isEmpty() && !undoCourseHist.isEmpty()){
            String lastCommand = undoCommandHist.pop();
            Course lastCourse = undoCourseHist.pop();

            if (lastCommand.equals("add")) {
                schedule.add(lastCourse);
            }
            else if (lastCommand.equals("remove")) {
                schedule.remove(lastCourse);
            }
        }

        else {
            System.out.println("No actions to redo.");
        }
    }

    public void removeClass(Course course, ArrayList<Course> Schedule){
        if (checkDouble(course, Schedule)) {
            Schedule.remove(course);
            System.out.println("Course removed.");
        }

        else {
            System.out.println("Course not in schedule, cannot be removed.");
        }

        updateHistory("remove", course);
    }

    /**
     *
     * @param course the course that is being attempted to be added to user schedule
     * @param Schedule
     */
    public void addClass(Course course, ArrayList<Course> Schedule){
        //checks for time confliction
        if(checkDouble(course, Schedule)){
            System.out.println("That course already is on your schedule, cannot be added.");
        }else if(checkConfliction(course, Schedule)){
            System.out.println("There is a time conflict with your schedule.");
            Scanner scn = new Scanner(System.in);
            while (true) {
                System.out.println("Would you like to add anyway? (Y/N");
                String answer = scn.next();
                if (answer.equals("Y") || answer.equals("y") || answer.equals("yes") || answer.equals("Yes")) {
                    Schedule.add(course);
                    System.out.println("Conflicting course added.");
                    break;
                } else if (answer.equals("N") || answer.equals("n") || answer.equals("no") || answer.equals("No")) {
                    System.out.println("Conflicting course was not added.");
                    break;
                } else {
                    System.out.println("Invalid response please select Y or N.");
                }
            }
        }else{
            Schedule.add(course);
            System.out.println("The course has successfully been added to your schedule.");
        }
        //possibly a remove from grand course list so you can add double of a course
    }

    /**
     * @param C course being checked for duplication
     * @param S current schedule being checked
     * @return True if there is a conflict
     * @return False if there is no conflict
     */
    public static boolean checkDouble(Course C, ArrayList<Course> S){
        boolean check = false;
        for(int i = 0; i < S.size(); i++){
            if(S.get(i).courseCode.equals(C.courseCode)){
                check = true;
            }
        }
        return check;
    }

    /**
     * @param C course being checked for duplication
     * @param S current schedule being checked
     * @return True if there is a conflict
     * @return False if there is no conflict
     */
    public static boolean checkConfliction(Course C, ArrayList<Course> S){
        boolean check = false;
        for(int i = 0; i < S.size(); i++){
            if(S.get(i).startTime == C.startTime && S.get(i).meets.equals(C.meets)){
                check = true;
            }
        }
        return check;
    }

    /**
     * Count how many courses have a conflict
     * @param S Student schedule
     * @return number of conflicts
     */
    public static int countConflicts(ArrayList<Course> S){
        int count = 0;
        for(int i = 0; i < S.size(); i++){
            Course c = S.get(i);
            if(checkConfliction(c, S)){
                count++;
            }
        }
        return count;
    }

    /**
     * Returns a calendar, will modify to list classes
     * @param S Current classes in user schedule
     */
    public static void printCalendar(ArrayList<Course> S){
        Calendar calendar = new GregorianCalendar(2022, 2,1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        System.out.println(new SimpleDateFormat("MMMM yyyy").format(calendar.getTime()));
        System.out.println("  S   M   T   W   T   F   S");

        //print initial spaces
        String initialSpace = "";
        for (int i = 0; i < dayOfWeek - 1; i++) {
            initialSpace += "    ";
        }
        System.out.print(initialSpace);

        //print the days of the month starting from 1
        for (int i = 0, dayOfMonth = 1; dayOfMonth <= daysInMonth; i++) {
            for (int j = ((i == 0) ? dayOfWeek - 1 : 0); j < 7 && (dayOfMonth <= daysInMonth); j++) {
                System.out.printf("%3d ", dayOfMonth);
                dayOfMonth++;
            }
            System.out.println();
        }
    }

    public static void confirmS(ArrayList<Course> S) {
        //check for number of conflicts
        Scanner scn = new Scanner(System.in);
        int conflicts = countConflicts(S);
        if (conflicts > 0) {
            System.out.println(conflicts + " classes conflict, would you still like to" +
                    " confirm? (Y/N)");
            String answer = scn.next();
            boolean confirmed = false;
            while (!confirmed) {
                if (answer.equalsIgnoreCase("YES") || answer.equalsIgnoreCase("Y")) {
                    printCalendar(S);
                    System.out.println(conflicts + " classes conflict.");
                    confirmed = true;

                    //Let the user edit schedule and print their status sheet here? or outside loop

                } else if (answer.equalsIgnoreCase("NO") || answer.equalsIgnoreCase("N")) {
                    System.out.println("Proceed back to course list.");
                    confirmed = true;
                } else {
                    System.out.println("Invalid input. Type Yes or No.");
                    answer = scn.next();
                }
            }
        }
        //if no conflicts, then proceed with confirming schedule
        else{
            System.out.println(conflicts + " conflicts exist. Confirming schedule now.");
            printCalendar(S);

            //Let user get status sheet or continue to edit schedule
        }
    }

    public static void main(String[] args) {

        CourseList cList = new CourseList();
        //Test 1
        System.out.println("Test 1:");
        ArrayList<Course> test = new ArrayList<>();
        Course test_c = new Course("MATH 101", "Intro Math", "Introduction to Mathematics", "9", "10", "MWF", "SHAL", "101");
        System.out.println(test);
        cList.addClass(test_c,test);
        System.out.println(test);

        //Test 2
        System.out.println("Test 2:");
        System.out.println(test);
        cList.addClass(test_c, test);
        System.out.println(test);

        //Test 3
        System.out.println("Test 3:");
        Course test_c2 = new Course("PHIL 101", "Intro Phil", "Introduction to Philosophy", "9", "10", "MWF", "SHAL", "102");
        System.out.println(test);
        cList.addClass(test_c2, test);
        System.out.println(test);

        //Test 4, test for confirm schedule
        System.out.println("Test 4:");
        System.out.println(test);
        confirmS(test);

        //Test 5
        System.out.println("Test 5:");
        cList.removeClass(test_c, test);
        System.out.println(test);

    }
}

