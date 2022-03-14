import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


public class CourseList {
    Stack<course> courseHist = new Stack<>();
    Stack<String> commandHist = new Stack<>();

    Stack<course> undoCourseHist = new Stack<>();
    Stack<String> undoCommandHist = new Stack<>();

    /**
     * Place holder for the course class
     */
    public class course {
        String code = "TEST 123A";
        String time = "9-9:50";
        String day = "MWF";
        //place holder

    }

    public void updateHistory(String command, course course){
        commandHist.push(command);
        courseHist.push(course);
    }


    public void undo(ArrayList<course> schedule){
        if (!undoCommandHist.isEmpty() && !undoCourseHist.isEmpty()) {
            String lastCommand = commandHist.pop();
            course lastCourse = courseHist.pop();

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

    public void redo(ArrayList<course> schedule) {

        if (!undoCommandHist.isEmpty() && !undoCourseHist.isEmpty()){
            String lastCommand = undoCommandHist.pop();
            course lastCourse = undoCourseHist.pop();

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

    public static void removeClass(course course, ArrayList<course> Schedule){
        if (checkDouble(course, Schedule)) {
            Schedule.remove(course);
            System.out.println("Course removed.");
        }

        else {
            System.out.println("Course not in schedule, cannot be removed.");
        }
    }

    /**
     *
     * @param course the course that is being attempted to be added to user schedule
     * @param Schedule
     */
    public static void addClass(course course, ArrayList<course> Schedule){
        //checks for time confliction
        if(checkConfliction(course, Schedule)){
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

        }else if(checkDouble(course, Schedule)){
            System.out.println("That course already is on your schedule, cannot be added.");
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
    public static boolean checkDouble(course C, ArrayList<course> S){
        boolean check = false;
        for(int i = 0; i < S.size(); i++){
            if(S.get(i).code.equals(C.code)){
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
    public static boolean checkConfliction(course C, ArrayList<course> S){
        boolean check = false;
        for(int i = 0; i < S.size(); i++){
            if(S.get(i).time.equals(C.time) && S.get(i).day.equals(C.day)){
                check = true;
            }
        }
        return check;
    }



}

