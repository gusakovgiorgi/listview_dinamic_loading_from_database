package net.gusakov.ddatestapp.classes;

import static net.gusakov.ddatestapp.classes.CourseUtil.UNINITIALIZED;

/**
 * Created by hasana on 2/23/2017.
 */

public class Filter {
    private int courseId;
    private int mark;
    private int readNumber;

    public Filter(int courseId, int mark, int readNumber) {
        this.courseId = courseId;
        this.mark = mark;
        this.readNumber = readNumber;
    }

    public Filter(int readNumber) {
        this.readNumber = readNumber;
        courseId=UNINITIALIZED;
        mark=UNINITIALIZED;
    }

    public Filter(){
        courseId=UNINITIALIZED;
        mark=UNINITIALIZED;
        readNumber=UNINITIALIZED;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public long getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(int readNumber) {
        this.readNumber = readNumber;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "courseId=" + courseId +
                ", mark=" + mark +
                ", readNumber=" + readNumber +
                '}';
    }
}
