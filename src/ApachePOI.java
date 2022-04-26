import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class ApachePOI {
    final int CODE_COL = 0;
    final int STITLE_COL = 1;
    final int LTITLE_COL = 2;
    final int STARTTIME_COL = 3;
    final int ENDTIME_COL = 4;
    final int MEET_COL = 5;
    final int BUILDING_COL = 6;
    final int ROOM_COL = 7;

    static XSSFWorkbook courses;
    static XSSFSheet sheet;

    static DataFormatter formatDate = new DataFormatter();

    /**
     * Initialize the DB Sheet
     */
    public ApachePOI() {
        try{
            FileInputStream courseFile = new FileInputStream("CourseDB.xlsx");
            courses = new XSSFWorkbook(courseFile);
            sheet = courses.getSheetAt(0);
        }
        catch(FileNotFoundException e){
            System.out.println("Did not add this file to the directory (fool)");
        }
        catch(IOException e){
            System.out.println("The file is not in the correct format.");
        }
//        sheet = courses.getSheetAt(0);
    }

    public static void deleteOldSchedule(String username, String fileName) throws IOException {
       try {
           FileInputStream userFile = new FileInputStream(fileName);
           XSSFWorkbook schedules = new XSSFWorkbook(userFile);

           for (int i = schedules.getNumberOfSheets() - 1; i >= 0; i--) {
               XSSFSheet tmpSheet = schedules.getSheetAt(i);
               if (tmpSheet.getSheetName().equals(username)) {
                   schedules.removeSheetAt(i);
               }
           }
           userFile.close();
           //schedules.close();

           FileOutputStream outputStream = new FileOutputStream(fileName);
           System.out.println("final test");
           schedules.write(outputStream);
           schedules.close();
           outputStream.close();

       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public static void writeSchedule(String username, ArrayList<Course> user){
        String fileName = "UserSchedules.xlsx";
        try {
            FileInputStream userFile = new FileInputStream(fileName);
            XSSFWorkbook schedules = new XSSFWorkbook(userFile);
            //Remove old schedule if there is one
            deleteOldSchedule(username, fileName);

            XSSFSheet userSheet = schedules.createSheet(username);
            int rowCount = -1;

            System.out.println("About to write");
            for(Course course : user){
                Row row = userSheet.createRow(++rowCount);
//                Cell cell = row.createCell(0);
//                cell.setCellValue(username);

                Cell cell = row.createCell(0);
                cell.setCellValue(course.getCourseCode());
                cell = row.createCell(1);
                cell.setCellValue(course.shortTitle);
                cell = row.createCell(2);
                cell.setCellValue(course.longTitle);
                cell = row.createCell(3);
                cell.setCellValue(course.startTime);
                cell = row.createCell(4);
                cell.setCellValue(course.endTime);
                cell = row.createCell(5);
                cell.setCellValue(course.meets);
                cell = row.createCell(6);
                cell.setCellValue(course.building);
                cell = row.createCell(7);
                cell.setCellValue(course.roomNum);
            }
            userFile.close();
            System.out.println("writing...");

            FileOutputStream outputStream = new FileOutputStream(fileName);
            System.out.println("final test");
            schedules.write(outputStream);
            schedules.close();
            outputStream.close();
        }
        catch(FileNotFoundException e){
            System.out.println("Did not add this file to the directory (fool)");
        }
        catch(IOException e){
            System.out.println("The file is not in the correct format.");
        }
    }

    public static ArrayList<Course> readSchedule(String username){
        ArrayList<Course> completeList = new ArrayList<>();
        try{
            FileInputStream userFile = new FileInputStream("UserSchedules.xlsx");
            XSSFWorkbook schedules = new XSSFWorkbook(userFile);
            XSSFSheet userSheet = schedules.getSheet(username);
            ArrayList<Row> allRows = new ArrayList<>();
            //skip first row
            //boolean skipHeader = true;
            for(Row row : userSheet){
//                if(skipHeader){
//                    skipHeader = false;
//                    continue;
//                }
                allRows.add(row);
            }

            for(int i = 0; i < allRows.size(); i++) {
                ArrayList<String> cells = new ArrayList<>();
                for (Cell cell : allRows.get(i)) {
                    String cellWSpace = formatDate.formatCellValue(cell);
                    cells.add(cellWSpace);
                }
                //create course to be added
                Course temp = new Course(cells.get(0),cells.get(1), cells.get(2), cells.get(3), cells.get(4), cells.get(5),
                        cells.get(6),cells.get(7));
                completeList.add(temp);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Did not add this file to the directory (fool)");
        } catch (IOException e) {
            System.out.println("The file is not in the correct format.");
        }
        return completeList;
    }

    /**
     * Read the file and output an array list of Courses
     */
    public static ArrayList<Course> courseList(){
        ArrayList<Course> completeList = new ArrayList<>();
        ArrayList<Row> allRows = new ArrayList<>();

        boolean skipHeader = true;
        for(Row row : sheet){
            if(skipHeader){
                skipHeader = false;
                continue;
            }
            allRows.add(row);
        }

        for(int i = 0; i < allRows.size(); i++){
            ArrayList<String> cells = new ArrayList<>();
            for(Cell cell : allRows.get(i)){
                String cellWSpace = formatDate.formatCellValue(cell);
                cells.add(cellWSpace);
            }
            //create course to be added
            Course temp = new Course(cells.get(0), cells.get(1),cells.get(2), cells.get(3), cells.get(4),
                    cells.get(5), cells.get(6), cells.get(7));
            completeList.add(temp);
        }
        return completeList;
    }

    public static Course findByCourseCode(String courseCode){
        boolean skipHeader = true;
        for(Row row : sheet) {
            if (skipHeader) {
                skipHeader = false;
                continue;
            }
            Cell cell = row.getCell(0);
            String toCompare = formatDate.formatCellValue(cell);

            if(courseCode.equals(toCompare)){
                Course temp = new Course(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue(), formatDate.formatCellValue(row.getCell(3)),
                        formatDate.formatCellValue(row.getCell(4)), formatDate.formatCellValue(row.getCell(5)),
                        formatDate.formatCellValue(row.getCell(6)), formatDate.formatCellValue(row.getCell(7)));
                return temp;
            }
        }
        return null;
    }

    /**
     * search by code, title, building etc
     */
    public static ArrayList<Course> searchByColumn(String search, int col){
        //get rid of spaces
        String searchInput = search.replace(" ", "");

        ArrayList<Course> results = new ArrayList<>();
        boolean skipHeader = true;
        for(Row row : sheet){
            if(skipHeader){
                skipHeader = false;
                continue;
            }
            Cell cell = row.getCell(col);
            String toCompare = formatDate.formatCellValue(cell);

            toCompare = toCompare.replace(" ", "");

            //make both capital so I can use .contains();
            toCompare = toCompare.toLowerCase();
            searchInput = searchInput.toLowerCase();

            if(toCompare.contains(searchInput)){
                Course temp = new Course(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue(), formatDate.formatCellValue(row.getCell(3)),
                        formatDate.formatCellValue(row.getCell(4)), formatDate.formatCellValue(row.getCell(5)),
                        formatDate.formatCellValue(row.getCell(6)), formatDate.formatCellValue(row.getCell(7)));
                results.add(temp);
            }
        }
        return results;
    }
    public static void main (String[] args) throws IOException {
//        ArrayList<Course> userCourse = new ArrayList<>();
//        Course test_c = new Course("MATH 101", "Intro Math", "Introduction to Mathematics", "9", "10", "MWF", "SHAL", "101");
//        userCourse.add(test_c);
//        System.out.println(userCourse);
//        String userName = "testing";
//        System.out.println("About to run");
//        writeSchedule(userName, userCourse);
//        System.out.println("Complete");
//
//        ArrayList<Course> completed = readSchedule(userName);
//        System.out.println(completed);

        deleteOldSchedule("Test1","UserSchedules.xlsx");
    }
}
