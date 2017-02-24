package net.gusakov.ddatestapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.gusakov.ddatestapp.classes.CourseUtil;
import net.gusakov.ddatestapp.classes.Filter;
import net.gusakov.ddatestapp.classes.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static net.gusakov.ddatestapp.classes.CourseUtil.UNINITIALIZED;

/**
 * Created by gusakov on 2/22/2017.
 * Database class
 */

public class Database extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public Database(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    private static final String TAG = "Database";
    private static int DB_VERSION = 9;

    public static final String SQLITE_REQUIRED_ID = "_id";
    //db name
    public static final String DB_NAME = "ddap.db";
    //table names
    public static final String TB_STUDENT = "students";
    public static final String TB_COURSE = "courses";
    public static final String TB_MARK = "marks";
    //student table columns
    public static final String ST_TB_ID = SQLITE_REQUIRED_ID;
    public static final String ST_TB_HASH_ID = "hash_id";
    public static final String ST_TB_FIRST_NAME = "first_name";
    public static final String ST_TB_LAST_NAME = "last_name";
    public static final String ST_TB_BIRTHDAY = "birthday";
    //courses table columns
    public static final String COUR_TB_ID = SQLITE_REQUIRED_ID;
    public static final String COUR_TB_NAME = "name";
    //students and courses table
    public static final String MARK_TB_STUDENTS_ID = SQLITE_REQUIRED_ID;
    public static final String MARK_TB_COURSE_ID = "course_id";
    public static final String MATK_TB_MARK = "mark";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "--- onCreate database ---");
        db.execSQL("create table " + TB_STUDENT + " ("
                + ST_TB_ID + " integer primary key autoincrement, "
                + ST_TB_HASH_ID + " text not null unique, "
                + ST_TB_FIRST_NAME + " text, "
                + ST_TB_LAST_NAME + " text, "
                + ST_TB_BIRTHDAY + " integer"
                + ");");

        db.execSQL("create table " + TB_COURSE + " ("
                + COUR_TB_ID + " integer primary key, "
                + COUR_TB_NAME + " text"
                + ");");

        fillCourseTable(db);

        db.execSQL("create table " + TB_MARK + " ("
                + MARK_TB_STUDENTS_ID + " integer, "
                + MARK_TB_COURSE_ID + " integer, "
                + MATK_TB_MARK + " ineger, "
                + "primary key(" + MARK_TB_STUDENTS_ID + "," + MARK_TB_COURSE_ID + "));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_COURSE);
        db.execSQL("DROP TABLE IF EXISTS " + TB_MARK);
        db.execSQL("DROP TABLE IF EXISTS " + TB_STUDENT);
        onCreate(db);
    }

    private void fillCourseTable(SQLiteDatabase db) {
        int[] courseIds = CourseUtil.getCourseIds();
        ContentValues cv = new ContentValues(courseIds.length);
        for (int i = 0; i < courseIds.length; i++) {
            cv.put(COUR_TB_ID, courseIds[i]);
            cv.put(COUR_TB_NAME, CourseUtil.getCourseString(courseIds[i]));
            db.insert(TB_COURSE, null, cv);
        }

    }

    public void saveStudentData(Student student) {
        ContentValues cv = new ContentValues();
        cv.put(ST_TB_HASH_ID, student.getHashId());
        cv.put(ST_TB_FIRST_NAME, student.getFirstName());
        cv.put(ST_TB_LAST_NAME, student.getLastName());
        cv.put(ST_TB_BIRTHDAY, student.getBirthdayTimeStamp());
        long studentId = db.insert(TB_STUDENT, null, cv);
        if (studentId != -1) {
            cv = new ContentValues();
            Map<Integer, Integer> marks = student.getMarks();
            for (Integer courseIdkey : marks.keySet()) {
                cv.put(MARK_TB_COURSE_ID, courseIdkey);
                cv.put(MARK_TB_STUDENTS_ID, studentId);
                cv.put(MATK_TB_MARK, marks.get(courseIdkey));
                db.insert(TB_MARK, null, cv);
            }
        }
    }

    public boolean saveStudentsData(List<Student> students) {
        //need transaction for quick work
        boolean result = false;
        db.beginTransaction();
        try {
            for (Student st : students) {
                saveStudentData(st);
            }
            db.setTransactionSuccessful();
            result = true;
        } finally {
            db.endTransaction();
        }
        return result;
    }

    public Cursor getStudentsInfo(Filter filter) {
        if (filter.getCourseId() == UNINITIALIZED) {
            return db.rawQuery("SELECT  " + ST_TB_ID + "," + ST_TB_FIRST_NAME + "," + ST_TB_LAST_NAME + "," + ST_TB_BIRTHDAY +
                    " FROM " + TB_STUDENT + " LIMIT " + filter.getReadNumber(), null);
        }else {
            String[] studentIds = getStudentIdsByCourseAndMark(filter.getCourseId() + "", filter.getMark() + "", filter.getReadNumber() + "");

            StringBuilder whereSb = new StringBuilder("(");
            if(studentIds.length>0) {
                for (int i = 0; i < studentIds.length - 1; i++) {
                    whereSb.append(ST_TB_ID).append("=").append(studentIds[i]).append(" OR ");
                }
                whereSb.append(ST_TB_ID).append("=").append(studentIds[studentIds.length - 1]).append(")");
            }else{
                //for returning empty cursor
                whereSb.append(ST_TB_ID).append("=").append(UNINITIALIZED).append(")");
            }

            return db.rawQuery("SELECT  " + ST_TB_ID + "," + ST_TB_FIRST_NAME + "," + ST_TB_LAST_NAME + "," + ST_TB_BIRTHDAY +
                    " FROM " + TB_STUDENT + " WHERE " + whereSb.toString() + " LIMIT " + filter.getReadNumber(), null);
        }
    }

    private String[] getStudentIdsByCourseAndMark(String courseId, String mark, String limit) {
        Cursor studentIdsCursor = db.rawQuery("SELECT  " + MARK_TB_STUDENTS_ID +
                        " FROM " + TB_MARK + " WHERE(" + MARK_TB_COURSE_ID + "=? and " + MATK_TB_MARK + "=?) LIMIT ?",
                new String[]{courseId, mark, limit});
        String[] studentIds = new String[studentIdsCursor.getCount()];
        int i = 0;
        while (studentIdsCursor.moveToNext()) {
            studentIds[i++] = studentIdsCursor.getString(0);
        }
        return studentIds;
    }

    public Map<Integer,Integer> getStudentMarks(Integer studentId) {
        Cursor courseAndMarkCursor=db.rawQuery("SELECT  " + MARK_TB_COURSE_ID + "," + MATK_TB_MARK +
                " FROM " + TB_MARK + " WHERE " +MARK_TB_STUDENTS_ID+"="+studentId, null);
        Map<Integer,Integer> marks=new HashMap<>(4,1.25f);
        while (courseAndMarkCursor.moveToNext()){
            marks.put(courseAndMarkCursor.getInt(courseAndMarkCursor.getColumnIndex(MARK_TB_COURSE_ID)),courseAndMarkCursor.getInt(courseAndMarkCursor.getColumnIndex(MATK_TB_MARK)));
        }
        return marks;
    }

    public boolean openSqlConnection() {
        try {
            db = this.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void closeSqlConnection() {
        if (db != null) {
            db.close();
            db = null;
        }
    }


}
