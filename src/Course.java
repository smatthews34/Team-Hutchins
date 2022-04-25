

public class Course {
    String courseCode;
    String longTitle;
    String shortTitle;
    String meets;
    String building;
    String roomNum;

    String startTime;
    String endTime;

    /**
     * This constructor has it all
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
     * Constructor for Extracurricular Activities
     * @param shortTitle
     * @param startTime
     * @param endTime
     * @param meets
     */
    public Course (String shortTitle, String startTime, String endTime, String meets){
        this.courseCode = "EXTRA";
        this.shortTitle = shortTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meets = meets;
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
    }

    /**
     * For full length courses
     * @return the formatted String of a Course.
     */
    @Override
    public String toString(){
        return String.format(courseCode + " " + shortTitle + " " + startTime + "-" + endTime + " " + meets);
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}