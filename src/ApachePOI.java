import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
                String cellWSpace = "";
                //remove spaces
                if(cell.getCellType() != CellType.STRING){
                    cellWSpace = formatDate.formatCellValue(cell);
                }
                else {
                    cellWSpace = cell.getStringCellValue();
                }
                String cellNoSpace = cellWSpace.replace(" ", "");
                cells.add(cellNoSpace);
            }
            //create course to be added
            Course temp = new Course(cells.get(0), cells.get(1),cells.get(2), cells.get(3), cells.get(4),
                    cells.get(5), cells.get(6), cells.get(7));
            completeList.add(temp);
        }
        return completeList;
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
            String toCompare = "";
            if(cell.getCellType() != CellType.STRING){
                toCompare = formatDate.formatCellValue(cell);
            }
            else {
                toCompare = cell.getStringCellValue();
            }
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
}
