import java.util.*;
import java.io.*;

public class Search {
    static String searchInput; //Change from static later

    public Search(String searchInput){
        this.searchInput = searchInput;
        //getResults();
    }

    /**
     * Takes in the results of the search and orginizes it to be prepared for the user to see
     * @param searchResults from the getResults method
     */
    public static ArrayList<Course> orderSearch(ArrayList<Course> searchResults){
        ArrayList<Course> ordered = searchResults;
        //sorts an arraylist by days then sorts the groups of day by chronological order
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
    }

    /**
     * Prints the ordered lists from the search results for the user to view and choose what to add.
     * @param orderList takes in and ordered list from the search results given to the orderList()
     */
    public void printResults(ArrayList<Course> orderList){
        for(int i = 0; i < orderList.size(); i++){
            System.out.println(orderList.get(i).courseCode + " " + orderList.get(i).shortTitle + " " + orderList.get(i).meets + " " + orderList.get(i).startTime + "-" + orderList.get(i).endTime);
        }

    }
    public ArrayList<Course> getResults(String searchInputWithSpace){ //Get rid of static afterwards and return string, get rid of parameter
        //Search by course code and then course title
        if(!ApachePOI.searchByColumn(searchInputWithSpace,0).isEmpty()){
            return ApachePOI.searchByColumn(searchInputWithSpace,0);
        }
        if(!ApachePOI.searchByColumn(searchInputWithSpace,2).isEmpty()){
            return ApachePOI.searchByColumn(searchInputWithSpace,2);
        }
        ArrayList<Course> empty = new ArrayList<>();
        return empty;

//        try {
//            File classFile = new File("classFile.txt");
//            Scanner classScan = new Scanner(classFile);
//            String course;
//            int index = 0; //Keeps track of what section of the file we're at
//            String searchInput = searchInputWithSpace.replace(" ", "");
//            ArrayList<Course> results = new ArrayList<>(); //Finished results
//            ArrayList<String> theStrings = new ArrayList<>(); //Stores the strings that will be used to make a Course
//            int potentialIndex = 0;
//            Course potentialCourse;
//            classScan.nextLine();
//            while (classScan.hasNextLine()) {
//                course = classScan.nextLine(); //grabs the line of code (the course info)
//                Scanner courseScan = new Scanner(course); //Creates a new scanner to read the line
//                courseScan.useDelimiter(",");
//                Scanner potentialScan = new Scanner(course);
//                potentialScan.useDelimiter(",");
//                while (potentialScan.hasNext()){
//                    String potentialData = potentialScan.next();
//                    theStrings.add(potentialData);
//                    potentialIndex++;
//                }
//                potentialCourse = new Course(theStrings.get(0), theStrings.get(1), theStrings.get(2),
//                        theStrings.get(3), theStrings.get(4),
//                        theStrings.get(5), theStrings.get(6), theStrings.get(7)); //Creates potential course for results
//                while (courseScan.hasNext()){ //Only search by course code and full course name
//                    String data = courseScan.next().replace(" ", "");
//                    if(data.equalsIgnoreCase("CourseCode")){ //Ensures that first line of file (info) is not used to create a new course
//                        break;
//                    }
//                    if (data.equalsIgnoreCase(searchInput) && index == 0 ||
//                            data.toLowerCase().contains(searchInput.toLowerCase()) && index == 0){ //User is searching by course code
//                        results.add(potentialCourse);//zack
//                        break;
//                    }
//                    else if (data.equalsIgnoreCase(searchInput) && index == 2 ||
//                            data.toLowerCase().contains(searchInput.toLowerCase()) && index == 2){ //User is searching by course name
//                        results.add(potentialCourse);
//                        break;
//                    }
//                    index++;
//                }
//                index = 0;
//                courseScan.close();
//                potentialScan.close();
//                theStrings.clear();
//            }
//            classScan.close();
//            return orderSearch(results);
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//            return null;
//        }
    }
    //day filter code

    public static ArrayList<Course> getKeystroke(String searchInputWithSpace){ //Returns an ArrayList based on keystrokes
        try {
            File classFile = new File("classFile.txt");
            Scanner classScan = new Scanner(classFile);
            String course;
            int index = 0; //Keeps track of what section of the file we're at
            String searchInput = searchInputWithSpace.replace(" ", "");
            ArrayList<Course> results = new ArrayList<>(); //Finished results
            ArrayList<String> theStrings = new ArrayList<>(); //Stores the strings that will be used to make a Course
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
                        theStrings.get(5), theStrings.get(6), theStrings.get(7)); //Creates potential course for results
                while (courseScan.hasNext()){ //Only search by course code and full course name
                    String data = courseScan.next().replace(" ", "");
                    if(data.equalsIgnoreCase("CourseCode")){ //Ensures that first line of file (info) is not used to create a new course
                        break;
                    }
                    else if (searchInput.equalsIgnoreCase("")){
                        break;
                    }
                    else if (data.toLowerCase().contains(searchInput.toLowerCase()) && index == 2){ //User is searching by course name
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
            return orderSearch(results);
        }  catch (FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

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
                populateMapDays(split[5], split[0]); // adds the columns for coursecode and days
            }
            ArrayList<Course> toPrint = filterCallers.promptUserDays(); //calls the prompt user method after
            toPrint.forEach(System.out::println);
            // error catching
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void populateMapDays(String day, String courseCode) {
        List<String> courses = null;
        // if the map contains the key, in this case the day that the class takes place on, it is added to the list
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
        System.out.println("Enter the days you want to see classes on:");
        System.out.print(">");
        while(scnr.hasNextLine()){
            String daysEntered = scnr.nextLine();
            List<String> courses = mapCoursesDays.get(daysEntered);
            if(courses == null){
                System.out.println("Please enter valid input");
            }
            else{
                courses.forEach(System.out::println); // prints out the list of appropriate courses
            }
            break;
            }
        }

        //scnr.close();

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
                populateMapTimes(split[3], split[0]); // adds the columns for coursecode and start time
            }
            ArrayList<Course> toPrint = filterCallers.promptUserTimes(); // then calls the prompt user method
            toPrint.forEach(System.out::println);
            // error catching
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void populateMapTimes(String time, String courseCode) {
        List<String> courses = null;
        // if the map contains the key, in this case the time that the class takes place on, it is added to the list
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
        System.out.println("Enter the times you want to see classes on:");
        System.out.print(">");
        while(scnr.hasNextLine()) {
            String timesEntered = scnr.nextLine();
            List<String> courses = mapCoursesTimes.get(timesEntered);
            if(courses == null){
                System.out.println("Please enter a valid time");
            }else{
                courses.forEach(System.out::println); // prints list of appropriate courses
            }
            break;
        }
       // scnr.close();
    }

    // dept filter code

    // HashMap initialized with string as a key and a list of strings
    public static Map<String, List<String>> mapCoursesDepts = null;

    // initlaizing map
    static {
        mapCoursesDepts = new HashMap<>();
    }

    public static void filterTxtDepts() {
        String line;
        BufferedReader br = null;
        boolean header = true;

        try {
            br = new BufferedReader(new FileReader("classFileDeptFilter.txt"));
            // while there is still data left in the file
            while ((line = br.readLine()) != null) {
                // this will make sure the header is not added to the map
                if (header) {
                    header = false;
                    continue;
                }
                String[] split = line.split(","); // separates columns using a comma
                populateMapDepts(split[10], split[0]); // adds the columns for coursecode and department
            }
            ArrayList<Course> toPrint = filterCallers.promptUserDepts(); //calls the method to prompt the user
            toPrint.forEach(System.out::println);
            // error catching
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Please enter valid input");
            //e.printStackTrace();
        }

    }

    public static void populateMapDepts(String dept, String courseCode) {
        List<String> courses = null;
        // if the map contains the key, in this case the department, it is added to the list
        if (mapCoursesDepts.containsKey(dept)) {
            courses = mapCoursesDepts.get(dept);
            courses.add(courseCode);
            mapCoursesDepts.put(dept, courses);
        } else {
            courses = new ArrayList<>();
            courses.add(courseCode);
            mapCoursesDepts.put(dept, courses);
        }
    }

    public static void promptUserDepts() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter the depts you want to see classes in:");
        System.out.print(">");
        while(scnr.hasNextLine()){
            String deptsEntered = scnr.nextLine();
            List<String> courses = mapCoursesDepts.get(deptsEntered);
            if(courses == null){
                System.out.println("Please enter a valid department");
            }else{
                courses.forEach(System.out::println); // prints the array of course codes
            }
            break;
        }
       //scnr.close();

    }

    public static Map<String, List<String>> mapCoursesBuildings = null;

    // initlaizing map
    static {
        mapCoursesBuildings = new HashMap<>();
    }

    public static void filterTxtBuildings() {
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
                populateMapBuildings(split[6], split[0]); // adds the columns for coursecode and building
            }
            ArrayList<Course> toPrint = filterCallers.promptUserBuildings(); //calls the prompt user method after
            toPrint.forEach(System.out::println);
            // error catching
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void populateMapBuildings(String building, String courseCode) {
        List<String> courses = null;
        // if the map contains the key, in this case the day that the class takes place on, it is added to the list
        if (mapCoursesBuildings.containsKey(building)) {
            courses = mapCoursesBuildings.get(building);
            courses.add(courseCode);
            mapCoursesBuildings.put(building, courses);
        } else {
            courses = new ArrayList<>();
            courses.add(courseCode);
            mapCoursesBuildings.put(building, courses);
        }
    }

    public static void main(String[] args) {
        ArrayList<Course> c = getKeystroke("SP");

        for (Course course : c) {
            System.out.println(c.toString());
        }
    }

}
