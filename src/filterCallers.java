import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class filterCallers {
    public static void promptUserDays() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter the days you want to see classes on:");
        System.out.print(">");
        while(scnr.hasNextLine()){
            String daysEntered = scnr.nextLine();
            List<String> courses = Search.mapCoursesDays.get(daysEntered);

            if(courses == null){
                System.out.println("Please enter valid input");
            }
            else{
                ArrayList<Course> toPrint = new ArrayList<>();
                for(String string : courses){
                    if(ApachePOI.findByCourseCode(string) == null){
                        System.out.println("That class does not exist.");
                    }
                    else {
                        toPrint.add(ApachePOI.findByCourseCode(string));
                    }
                }
                toPrint.forEach(System.out::println); // prints out the list of appropriate courses
            }
            break;
        }
    }

    public static void promptUserTimes() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter the times you want to see classes on:");
        System.out.print(">");
        while(scnr.hasNextLine()) {
            String timesEntered = scnr.nextLine();
            List<String> courses = Search.mapCoursesTimes.get(timesEntered);
            if(courses == null){
                System.out.println("Please enter a valid time");
            }else{
                ArrayList<Course> toPrint = new ArrayList<>();
                for(String string : courses){
                    if(ApachePOI.findByCourseCode(string) == null){
                        System.out.println("That class does not exist.");
                    }
                    else {
                        toPrint.add(ApachePOI.findByCourseCode(string));
                    }
                }
                toPrint.forEach(System.out::println); // prints out the list of appropriate courses
            }
            break;
        }
        // scnr.close();
    }

    public static void promptUserDepts() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter the depts you want to see classes in:");
        System.out.print(">");
        while(scnr.hasNextLine()){
            String deptsEntered = scnr.nextLine();
            List<String> courses = Search.mapCoursesDepts.get(deptsEntered);
            if(courses == null){
                System.out.println("Please enter a valid department");
            }else{
                ArrayList<Course> toPrint = new ArrayList<>();
                for(String string : courses){
                    if(ApachePOI.findByCourseCode(string) == null){
                        System.out.println("That class does not exist.");
                    }
                    else {
                        toPrint.add(ApachePOI.findByCourseCode(string));
                    }
                }
                toPrint.forEach(System.out::println); // prints out the list of appropriate courses
            }
            break;
        }
        //scnr.close();

    }

    public static void promptUserBuildings() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter the building you want to see classes in:");
        System.out.print(">");
        while(scnr.hasNextLine()){
            String buildingEntered = scnr.nextLine();
            List<String> courses = Search.mapCoursesBuildings.get(buildingEntered);
            if(courses == null){
                System.out.println("Please enter valid input");
            }
            else{
                ArrayList<Course> toPrint = new ArrayList<>();
                for(String string : courses){
                    if(ApachePOI.findByCourseCode(string) == null){
                        System.out.println("That class does not exist.");
                    }
                    else {
                        toPrint.add(ApachePOI.findByCourseCode(string));
                    }
                }
                toPrint.forEach(System.out::println); // prints out the list of appropriate courses
            }
            break;
        }
    }
}
