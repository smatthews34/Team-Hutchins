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
            if(S.get(i).startTime != null && S.get(i).startTime == C.startTime && S.get(i).meets.equals(C.meets)){
                check = true;
            }
        }
        return check;
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
        //confirmS(test);

        //Test 5
        System.out.println("Test 5:");
        cList.removeClass(test_c, test);
        System.out.println(test);

    }
}

