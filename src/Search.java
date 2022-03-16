import java.util.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class Search {
    static String searchInput; //Change from static later

    public Search(String searchInput){
        this.searchInput = searchInput;
        //getResults();
    }

    /**
     * Takes in the results of the search and orginzes it to be prepared for the user to see
     * @param searchResults from the getResults method
     */
    public static ArrayList<Course> orderSearch(ArrayList<Course> searchResults){
        ArrayList<Course> ordered = searchResults;
        Collections.sort(ordered, new Comparator<Course>() {
            @Override
            public int compare(Course c1, Course c2) {
                String time1 = ((Course) c1).time; //getters could be used here
                String time2 = ((Course) c2).time;
                int time_compare = time1.compareTo(time2);
                if (time_compare != 0) {
                    return time_compare;
                }
                String day1 = ((Course) c1).meets; //getters code be used here
                String day2 = ((Course) c2).meets;
                return day1.compareTo(day2);
            }
        });
        return ordered;
    }

    /**
     * Prints the ordered lists from the search results for the user to view and choose what to add.
     * @param orderList takes in and ordered list from the search results given to the orderList()
     */
    public static void printResults(ArrayList<Course> orderList){
        for(int i = 0; i < orderList.size(); i++){
            String course = "";
            course = orderList.get(i).meets + " " + orderList.get(i).time + " " + orderList.get(i).courseCode + " " + orderList.get(i).shortTitle;
            System.out.println(course);
        }

    }
    public static ArrayList<Course> getResults(String searchInputWithSpace){ //Get rid of static afterwards and return string, get rid of parameter
        try {
            File classFile = new File("classFile.txt");
            Scanner classScan = new Scanner(classFile);
            String course;
            int index = 0;
            String searchInput = searchInputWithSpace.replace(" ", "");
            ArrayList<Course> results = new ArrayList<>(); //Finish
            ArrayList<String> theStrings = new ArrayList<>();
            int potentialIndex = 0;
            Course potentialCourse;
            classScan.nextLine();
            while (classScan.hasNextLine()) {
                course = classScan.nextLine(); //grabs the line of code (the course info)
                Scanner courseScan = new Scanner(course); //Creates a new scanner to read the line
                courseScan.useDelimiter(",");
                Scanner potentialScan = new Scanner(course);
                potentialScan.useDelimiter(",");
                while (potentialScan.hasNext()){
                    String potentialData = potentialScan.next();
                    theStrings.add(potentialData);
                    potentialIndex++;
                }
                potentialCourse = new Course(theStrings.get(0), theStrings.get(1), theStrings.get(2),
                        theStrings.get(3), theStrings.get(4),
                        theStrings.get(5), theStrings.get(6), theStrings.get(7));
                while (courseScan.hasNext()){ //Only search by course code and full course name
                    String data = courseScan.next().replace(" ", "");
                    if(data.equalsIgnoreCase("CourseCode")){
                        break;
                    }
                    if (data.equalsIgnoreCase(searchInput) && index == 0 ||
                            data.toLowerCase().contains(searchInput.toLowerCase()) && index == 0){ //User is searching by course code
                        results.add(potentialCourse);//zack
                        break;
                    }
                    else if (data.equalsIgnoreCase(searchInput) && index == 2){ //User is searching by course name
                        results.add(potentialCourse);
                        break;
                    }
                    index++;
                }
                index = 0;
                courseScan.close();
                potentialScan.close();
                theStrings.clear();
            }
            classScan.close();
            return results;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }



        public static void dayFilter(String days) {
            Connection conn = null;
            if (days.equals("MWF")) {
                try {
                    conn = DriverManager.getConnection("jdbc:sqlite:courseList.db");
                    Statement statement = conn.createStatement();
                    statement.executeUpdate("DROP table if exists courseList");
                    String select = "SELECT * from CourseList";
                    ResultSet result = statement.executeQuery(select);

                    while (result.next()) {
                        String code = result.getString("CourseCode");
                        String description = result.getString("LongTitle");
                        System.out.println(code + " " + description);
                    }
                } catch (SQLException e) {
                    System.out.println("Error connecting to the SQLite database");
                    e.printStackTrace();
                }
            }
            if (days.equals("TR")) {
                try {
                    conn = DriverManager.getConnection("jdbc:sqlite:courseList.db");
                    Statement statement = conn.createStatement();
                    statement.executeUpdate("DROP table if exists courseList");
                    String select = "SELECT CourseCode, LongTitle FROM courseList where Meets = 'TR'";
                    ResultSet result = statement.executeQuery(select);

                    while (result.next()) {
                        String code = result.getString("CourseCode");
                        String description = result.getString("LongTitle");
                        System.out.println(code + " " + description);
                    }
                } catch (SQLException e) {
                    System.out.println("Error connecting to the SQLite database");
                    e.printStackTrace();
                }
            }
        }




    public static void main(String[] args){ //Temporary main for testing
        ArrayList<Course> daCourses = getResults("comp");
        for (Course daCours : daCourses) {
            System.out.println(daCours.longTitle);
        }
        String days1 = "MWF";
        String days2 = "TR";

        dayFilter(days1);
        dayFilter(days2);
    }
}