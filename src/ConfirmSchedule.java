import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConfirmSchedule {

    /**
     * Prints the schedule to a file the user can download
     * @param S User schedule
     * @throws FileNotFoundException
     */
    public static void scheduleFile(ArrayList<Course> S) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("FinishedSchedule.txt");
        //pw.printf("%-50s%-50s%-50s%-50s%-50s\n","Monday","Tuesday","Wednesday","Thursday","Friday");
        ArrayList<Course> mon = classesPerDay(S,2,true);
        ArrayList<Course> tue = classesPerDay(S,3,false);
        ArrayList<Course> wed = classesPerDay(S,4,false);
        ArrayList<Course> thu = classesPerDay(S,5,false);
        ArrayList<Course> fri = classesPerDay(S,6,false);

        mon = orderList(mon);
        tue = orderList(tue);
        wed = orderList(wed);
        thu = orderList(thu);
        fri = orderList(fri);

        String monString = courseListString(mon);
        String tueString = courseListString(tue);
        String wedString = courseListString(wed);
        String thuString = courseListString(thu);
        String friString = courseListString(fri);

        pw.println("Monday\n" + monString + "\nTuesday\n" + tueString + "\nWednesday\n" + wedString
        + "\nThursday\n" + thuString + "\nFriday\n" + friString);
        if(countConflicts(S) > 0) {
            pw.println("Conflicts Still Exist");
        }
        pw.close();
    }

    /**
     * Formats multiple courses into one string
     * @param S, user schedule
     * @return, string courses
     */
    public static String courseListString(ArrayList<Course> S){
        String list = "";
        for(int i = 0; i < S.size(); i++){
           list += S.get(i).toString() + "\n";
        }
        return list;
    }

    /**
     * Count how many conflicts are in the schedule
     * @param S, user schedule
     * @return number of conflicts
     */
    public static int countConflicts(ArrayList<Course> S){
        ArrayList<Course> SCopy = (ArrayList<Course>) S.clone();
        int count = 0;
        for(int i = 0; i < SCopy.size(); i++){
            Course c = SCopy.get(i);
            SCopy.remove(c);
            if(CourseList.checkConfliction(c, SCopy)){
                count++;
            }
        }
        return count;
    }

    /**
     * Returns a calendar for the current month
     */
    public static void printCalendar(ArrayList<Course> S){
        Calendar currentDate = Calendar.getInstance();
        Calendar calendar = new GregorianCalendar(currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        System.out.println("             "+new SimpleDateFormat("MMMM yyyy").format(calendar.getTime()));
        System.out.println("             SUNDAY                   MONDAY                   " +
                "TUESDAY                  WEDNESDAY                THURSDAY                 " +
                "FRIDAY                   SATURDAY");

        //print initial spaces
        String initialSpace = "";
        for (int i = 0; i < dayOfWeek - 1; i++) {
            initialSpace += "                         ";
        }
        System.out.print(initialSpace);

        //print the days of the month starting from 1
        for (int i = 0, dayOfMonth = 1; dayOfMonth <= daysInMonth; i++) {
            for (int j = ((i == 0) ? dayOfWeek - 1 : 0); j < 7 && (dayOfMonth <= daysInMonth); j++) {
                System.out.printf("%13d ", dayOfMonth);
                if(j == 1) {
                    System.out.print(classesPerDay(S, j+1, false).size() + " Class(es)");
                }
                if(j==0 || j==6) {
                    System.out.print("            ");
                }
                if(j == 2) {
                    System.out.print(classesPerDay(S, j+1, false).size() + " Class(es)");
                }
                if(j == 3) {
                    System.out.print(classesPerDay(S, j+1, false).size() + " Class(es)");
                }
                if(j == 4) {
                    System.out.print(classesPerDay(S, j+1, false).size() + " Class(es)");
                }
                if(j == 5) {
                    System.out.print(classesPerDay(S, j+6, false).size() + " Class(es)");
                }
                dayOfMonth++;
            }
            System.out.println("\n\n\n");
        }
    }

    /**
     * Takes a day of the month and finds what weekday it is
     * @param dayOfMonth, user input
     * @return day of the week in integer format
     */
    public static int findDayOfWeek(int dayOfMonth){
        Calendar c = Calendar.getInstance();
        GregorianCalendar date = new GregorianCalendar(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), dayOfMonth);
        //days of the week starts with Sunday = 1
        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    /**
     * Returns all classes on a particular day
     * @param S List of current courses
     * @param dayOfWeek
     * @return ArrayList of courses
     */
    public static ArrayList<Course> classesPerDay(ArrayList<Course> S, int dayOfWeek, boolean indStudy){
        ArrayList<Course> classesForDay = new ArrayList<>();

        if(dayOfWeek == 1 || dayOfWeek == 7){
            return classesForDay;
        }

        //just independent study
        if(dayOfWeek != 1 && dayOfWeek != 2 && dayOfWeek != 3 && dayOfWeek != 4
                && dayOfWeek != 5 && dayOfWeek != 6 && dayOfWeek != 7) {
            for(int i = 0; i < S.size(); i++) {
                if (S.get(i).endTime == null) {
                    classesForDay.add(S.get(i));
                }
            }
            return classesForDay;
        }

        for(int i = 0; i < S.size(); i++){
            if(S.get(i).endTime != null) {
                if (dayOfWeek == 2 && (S.get(i).meets.equals("MWF") || S.get(i).meets.equals("M"))) {
                    classesForDay.add(S.get(i));
                } else if (dayOfWeek == 3 && (S.get(i).meets.equals("TR") || S.get(i).meets.equals("T"))) {
                    classesForDay.add(S.get(i));
                } else if (dayOfWeek == 4 && (S.get(i).meets.equals("MWF") || S.get(i).meets.equals("W"))) {
                    classesForDay.add(S.get(i));
                } else if (dayOfWeek == 5 && (S.get(i).meets.equals("TR") || S.get(i).meets.equals("R"))) {
                    classesForDay.add(S.get(i));
                } else if (dayOfWeek == 6 && (S.get(i).meets.equals("MWF") || S.get(i).meets.equals("F"))) {
                    classesForDay.add(S.get(i));
                }
            }
            else if (indStudy){
                classesForDay.add(S.get(i));
            }
        }
        return classesForDay;
    }

    public static void weeklyView(ArrayList<Course> S){

    }

    /**
     * Should sort classes by time
     * @param courseList, list of classes to be sorted
     * @return Sorted list of courses
     */
    public static ArrayList<Course> orderList(ArrayList<Course> courseList){
        ArrayList<Course> ordered = (ArrayList<Course>) courseList.clone();
        ArrayList<Course> indStudy = new ArrayList<>();
        for(int i = 0; i<ordered.size(); i++){
            if(ordered.get(i).meets == null){
                Course c = ordered.get(i);
                indStudy.add(c);
                ordered.remove(c);
            }
        }
        Collections.sort(ordered, new Comparator<Course>() {
            @Override
            public int compare(Course c1, Course c2) {
                String time1 = c1.startTime; //getters could be used here
                String time2 = c2.startTime;
                return time1.compareTo(time2);
            }
        });

        for(int i = 0; i < indStudy.size(); i++){
            ordered.add(indStudy.get(i));
        }

        return ordered;
    }
}
