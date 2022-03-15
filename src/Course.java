public class Course {
    String courseCode;
    protected String time;
    String longTitle;
    String shortTitle;
    String meets;
    String building;
    String roomNum;

    String startTime;
    String endTime;

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
    public Course (String courseCode, String shortTitle, String longTitle, String startTime, String endTime, String meets,
                   String building, String roomNum){
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
        //this.startTime = 0;
    }

    /*public String getTime(){
        if(startTime == 0){
            return "na";
        }
        else {
            time = startTime + "-" + endTime;
            return time;
        }
    }*/

}