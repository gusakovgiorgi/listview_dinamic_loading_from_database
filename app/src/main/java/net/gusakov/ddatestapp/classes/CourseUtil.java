package net.gusakov.ddatestapp.classes;

/**
 * Created by gusakov on 2/22/2017.
 */

public class CourseUtil {
    public static final int COURSE0ID = 0;
    public static final int COURSE1ID = 1;
    public static final int COURSE2ID = 2;
    public static final int COURSE3ID = 3;

    public static final String COURSE0TEXT = "Course-0";
    public static final String COURSE1TEXT = "Course-1";
    public static final String COURSE2TEXT = "Course-2";
    public static final String COURSE3TEXT = "Course-3";

    public static final int UNINITIALIZED = -1;

    public static int getCourseId(String courseText) {
        int id;
        switch (courseText) {
            case COURSE0TEXT:
                id = COURSE0ID;
                break;
            case COURSE1TEXT:
                id = COURSE1ID;
                break;
            case COURSE2TEXT:
                id = COURSE2ID;
                break;
            case COURSE3TEXT:
                id = COURSE3ID;
                break;
            default:
                id = UNINITIALIZED;
                break;
        }
        return id;
    }

    public static String getCourseString(int id) {
        String courseString;
        switch (id) {
            case COURSE0ID:
                courseString = COURSE0TEXT;
                break;
            case COURSE1ID:
                courseString = COURSE1TEXT;
                break;
            case COURSE2ID:
                courseString = COURSE2TEXT;
                break;
            case COURSE3ID:
                courseString = COURSE3TEXT;
                break;
            default:
                courseString = "";
        }
        return courseString;
    }

    public static int[] getCourseIds() {
        return new int[]{COURSE0ID, COURSE1ID, COURSE2ID, COURSE3ID};
    }

    public static String[] getCourseStrings() {
        return new String[]{COURSE0TEXT, COURSE1TEXT, COURSE2TEXT, COURSE3TEXT};
    }

    public static boolean isValidId(int id) {
        return !getCourseString(id).isEmpty();
    }
}
