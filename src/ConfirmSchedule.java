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
        ArrayList<String> startTimesInOrder = new ArrayList<>();
        startTimesInOrder.add("8:00:00");
        startTimesInOrder.add("9:00:00");
        startTimesInOrder.add("9:15:00");
        startTimesInOrder.add("10:00:00");
        startTimesInOrder.add("10:05:00");
        startTimesInOrder.add("11:00:00");
        startTimesInOrder.add("11:30:00");
        startTimesInOrder.add("12:00:00");
        startTimesInOrder.add("13:00:00");
        startTimesInOrder.add("14:00:00");
        startTimesInOrder.add("14:30:00");
        startTimesInOrder.add("15:00:00");
        startTimesInOrder.add("16:00:00");
        startTimesInOrder.add("18:30:00");
        startTimesInOrder.add("19:00:00");

        pw.printf("%-20s%-50s%-50s%-50s%-50s%-50s\n","        ","Monday","Tuesday","Wednesday","Thursday","Friday");
        for(int i = 0; i < startTimesInOrder.size(); i++){
            ArrayList<Course> temp = classesByTime(S,startTimesInOrder.get(i));
            ArrayList<Course> mon = classesPerDay(temp, 2,false);
            ArrayList<Course> tue = classesPerDay(temp,3,false);
            ArrayList<Course> wed = classesPerDay(temp,4,false);
            ArrayList<Course> thu = classesPerDay(temp,5,false);
            ArrayList<Course> fri = classesPerDay(temp,6,false);

            while(!mon.isEmpty() || !tue.isEmpty() || !wed.isEmpty() || !thu.isEmpty() || !fri.isEmpty()){
                //System.out.println();
                String monString = "";
                String tueString = "";
                String wedString = "";
                String thuString = "";
                String friString = "";
                if(!mon.isEmpty()){
                    monString = mon.get(0).toString();
                    mon.remove(0);
                }
                if(!tue.isEmpty()){
                    tueString = tue.get(0).toString();
                    tue.remove(0);
                }
                if(!wed.isEmpty()){
                    wedString = wed.get(0).toString();
                    wed.remove(0);
                }
                if(!thu.isEmpty()){
                    thuString = thu.get(0).toString();
                    thu.remove(0);
                }
                if(!fri.isEmpty()){
                    friString = fri.get(0).toString();
                    fri.remove(0);
                }
                pw.printf("%-20s%-50s%-50s%-50s%-50s%-50s\n",startTimesInOrder.get(i),monString,tueString,wedString,thuString,friString);
            }
        }
        //three lines
        pw.println("\n\n\n");

        //print calendar too
        Calendar currentDate = Calendar.getInstance();
        Calendar calendar = new GregorianCalendar(currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        pw.println("          "+new SimpleDateFormat("MMMM yyyy").format(calendar.getTime()));
        pw.println("          SUNDAY                MONDAY                " +
                "TUESDAY               WEDNESDAY             THURSDAY              " +
                "FRIDAY                SATURDAY");

        //print initial spaces
        String initialSpace = "";
        for (int i = 0; i < dayOfWeek - 1; i++) {
            initialSpace += "                      ";
        }
        pw.print(initialSpace);

        //print the days of the month starting from 1
        for (int i = 0, dayOfMonth = 1; dayOfMonth <= daysInMonth; i++) {
            for (int j = ((i == 0) ? dayOfWeek - 1 : 0); j < 7 && (dayOfMonth <= daysInMonth); j++) {
                pw.printf("%10d ", dayOfMonth);
                if(j == 1) {
                    pw.print(classesPerDay(S, j+1, false).size() + " Class(es)");
                }
                if(j==0 || j==6) {
                    pw.print("            ");
                }
                if(j == 2) {
                    pw.print(classesPerDay(S, j+1, false).size() + " Class(es)");
                }
                if(j == 3) {
                    pw.print(classesPerDay(S, j+1, false).size() + " Class(es)");
                }
                if(j == 4) {
                    pw.print(classesPerDay(S, j+1, false).size() + " Class(es)");
                }
                if(j == 5) {
                    pw.print(classesPerDay(S, j+1, false).size() + " Class(es)");
                }
                dayOfMonth++;
            }
            pw.println("\n");
        }

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
                    System.out.print(classesPerDay(S, j+1, false).size() + " Class(es)");
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

    /**
     *
     * @param S
     * @param time, time in military time
     * @return
     */
    public static ArrayList<Course> classesByTime(ArrayList<Course> S, String time){
        ArrayList<Course> results = new ArrayList<>();

        for(int i = 0; i < S.size(); i++) {
            if (S.get(i).startTime != null && S.get(i).startTime.equals(time)) {
                results.add(S.get(i));
            }
        }
        return results;
    }

    public static void weeklyView(ArrayList<Course> S){
        ArrayList<String> startTimesInOrder = new ArrayList<>();
        startTimesInOrder.add("8:00:00");
        startTimesInOrder.add("9:00:00");
        startTimesInOrder.add("9:15:00");
        startTimesInOrder.add("10:00:00");
        startTimesInOrder.add("10:05:00");
        startTimesInOrder.add("11:00:00");
        startTimesInOrder.add("11:30:00");
        startTimesInOrder.add("12:00:00");
        startTimesInOrder.add("13:00:00");
        startTimesInOrder.add("14:00:00");
        startTimesInOrder.add("14:30:00");
        startTimesInOrder.add("15:00:00");
        startTimesInOrder.add("16:00:00");
        startTimesInOrder.add("18:30:00");
        startTimesInOrder.add("19:00:00");

        System.out.printf("%-50s%-50s%-50s%-50s%-50s%-50s\n","        ","Monday","Tuesday","Wednesday","Thursday","Friday");
        for(int i = 0; i < startTimesInOrder.size(); i++){
            ArrayList<Course> temp = classesByTime(S,startTimesInOrder.get(i));
            ArrayList<Course> mon = classesPerDay(temp, 2,false);
            ArrayList<Course> tue = classesPerDay(temp,3,false);
            ArrayList<Course> wed = classesPerDay(temp,4,false);
            ArrayList<Course> thu = classesPerDay(temp,5,false);
            ArrayList<Course> fri = classesPerDay(temp,6,false);

            while(!mon.isEmpty() || !tue.isEmpty() || !wed.isEmpty() || !thu.isEmpty() || !fri.isEmpty()){
                //System.out.println();
                String monString = "";
                String tueString = "";
                String wedString = "";
                String thuString = "";
                String friString = "";
                if(!mon.isEmpty()){
                    monString = mon.get(0).toString();
                    mon.remove(0);
                }
                if(!tue.isEmpty()){
                    tueString = tue.get(0).toString();
                    tue.remove(0);
                }
                if(!wed.isEmpty()){
                    wedString = wed.get(0).toString();
                    wed.remove(0);
                }
                if(!thu.isEmpty()){
                    thuString = thu.get(0).toString();
                    thu.remove(0);
                }
                if(!fri.isEmpty()){
                    friString = fri.get(0).toString();
                    fri.remove(0);
                }
                System.out.printf("%-50s%-50s%-50s%-50s%-50s%-50s\n",startTimesInOrder.get(i),monString,tueString,wedString,thuString,friString);
            }
        }
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
