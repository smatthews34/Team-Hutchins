import java.util.*;
import java.io.*;

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
                //sorts by day they meet
                String day1 = ((Course) c1).meets; //getters code be used here
                String day2 = ((Course) c2).meets;
                int day = day1.compareTo(day2);
                if(day != 0 ){
                    return day;
                }
                //then sorts by time of that day
                String time1 = ((Course) c1).startTime; //getters could be used here
                String time2 = ((Course) c2).startTime;
                return time1.compareTo(time2);
            }

        });
        return ordered;
        //return ordered;
    }

    /**
     * Prints the ordered lists from the search results for the user to view and choose what to add.
     * @param orderList takes in and ordered list from the search results given to the orderList()
     */
    public static void printResults(ArrayList<Course> orderList){
        for(int i = 0; i < orderList.size(); i++){
            System.out.println(orderList.get(i).courseCode + " " + orderList.get(i).shortTitle + " " + orderList.get(i).meets + " " + orderList.get(i).startTime + "-" + orderList.get(i).endTime);
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
    //day filter code

    // HashMap initialized with string as a key and a list of strings
    public static Map<String, List<String>> mapCoursesDays = null;

    // initlaizing map
    static {
        mapCoursesDays = new HashMap<>();
    }

    public static void filterTxtDays() {
        String line;
        BufferedReader br = null;
        boolean header = true;

        try {
            br = new BufferedReader(new FileReader("classFile.txt"));
            // while there is still data left in the file
            while ((line = br.readLine()) != null) {
                // this will make sure the header is not added to the map
                if (header) {
                    header = false;
                    continue;
                }
                String[] split = line.split(","); // separates columns using a comma
                populateMapDays(split[5], split[0]); // adds the columns for coursecode and time
            }
            promptUserDays();
            // error catching
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void populateMapDays(String day, String courseCode) {
        List<String> courses = null;
        if (mapCoursesDays.containsKey(day)) {
            courses = mapCoursesDays.get(day);
            courses.add(courseCode);
            mapCoursesDays.put(day, courses);
        } else {
            courses = new ArrayList<>();
            courses.add(courseCode);
            mapCoursesDays.put(day, courses);
        }
    }

    public static void promptUserDays() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter the days you want to see classes on");
        String daysEntered = scnr.nextLine();
        List<String> courses = mapCoursesDays.get(daysEntered);
        courses.forEach(System.out::println);
        scnr.close();
    }

    // time filter code

    // HashMap initialized with string as a key and a list of strings
    public static Map<String, List<String>> mapCoursesTimes = null;

    // initlaizing map
    static {
        mapCoursesTimes = new HashMap<>();
    }

    public static void filterTxtTimes() {
        String line;
        BufferedReader br = null;
        boolean header = true;

        try {
            br = new BufferedReader(new FileReader("classFile.txt"));
            // while there is still data left in the file
            while ((line = br.readLine()) != null) {
                // this will make sure the header is not added to the map
                if (header) {
                    header = false;
                    continue;
                }
                String[] split = line.split(","); // separates columns using a comma
                populateMapTimes(split[3], split[0]); // adds the columns for coursecode and day
            }
            promptUserTimes();
            // error catching
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void populateMapTimes(String time, String courseCode) {
        List<String> courses = null;
        if (mapCoursesTimes.containsKey(time)) {
            courses = mapCoursesTimes.get(time);
            courses.add(courseCode);
            mapCoursesTimes.put(time, courses);
        } else {
            courses = new ArrayList<>();
            courses.add(courseCode);
            mapCoursesTimes.put(time, courses);
        }
    }

    public static void promptUserTimes() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter the times you want to see classes on");
        String timesEntered = scnr.nextLine();
        List<String> courses = mapCoursesTimes.get(timesEntered);
        courses.forEach(System.out::println);
        scnr.close();
    }

    public static void main(String[] args){ //Temporary main for testing
        ArrayList<Course> daCourses = getResults("comp");
        printResults(orderSearch(daCourses));
        System.out.println("");
        System.out.println("");
        System.out.println("");
        printResults(daCourses);
        for (Course daCours : daCourses) {
            System.out.println(daCours.longTitle);
        }
        filterTxtDays();
        filterTxtTimes();
    }
}