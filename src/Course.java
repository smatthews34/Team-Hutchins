

public class Course {
    String courseCode;
    protected String time;
    String longTitle;
    String shortTitle;
    String meets;
    String building;
    int roomNum;

    int startTime;
    int endTime;

    /**
     * This constructor has it all baby
     * @param courseCode
     * @param shortTitle
     * @param longTitle
     * @param startTime
     * @param endTime
     * @param meets
     * @param roomNum
     */
    public Course (String courseCode, String shortTitle, String longTitle, int startTime, int endTime, String meets,
                   String building, int roomNum){
        this.courseCode = courseCode;
        this.shortTitle = shortTitle;
        this.longTitle = longTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meets = meets;
        this.building = building;
        this.roomNum = roomNum;
    }

    /**
     * Constructor for independent study and internships
     * @param courseCode
     * @param shortTitle
     * @param longTitle
     */
    public Course (String courseCode, String shortTitle, String longTitle){
        this.courseCode = courseCode;
        this.shortTitle = shortTitle;
        this.longTitle = longTitle;
        this.startTime = 0;
    }

    public String getTime(){
        if(startTime == 0){
            return "na";
        }
        else {
            time = startTime + "-" + endTime;
            return time;
        }
    }

    /**
     * For full length courses
     * @return the formatted String of a Course.
     */
    @Override
    public String toString(){
        return String.format(courseCode + " " + shortTitle + " " + startTime + "-" + endTime + " " + meets + " " + longTitle);
    }


}