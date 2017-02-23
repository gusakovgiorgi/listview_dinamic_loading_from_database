package net.gusakov.ddatestapp.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import net.gusakov.ddatestapp.MainActivity;
import net.gusakov.ddatestapp.database.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static net.gusakov.ddatestapp.classes.CourseUtil.UNINITIALIZED;

/**
 * Created by hasana on 2/22/2017.
 */

public class JSONParser {

    private static final String JSON_BIRTHDAY = "birthday";
    private static final String JSON_FIRSTNAME = "firstName";
    private static final String JSON_LASTNAME = "lastName";
    private static final String JSON_ID = "id";
    private static final String JSON_COURSES = "courses";
    private static final String JSON_COURSE_NAME = "name";
    private static final String JSON_COURSE_MARK = "mark";


    private static final String TAG = "JSONParser";
    private int mark = UNINITIALIZED;
    private int courseId = UNINITIALIZED;
    private final int ARRAYLIST_SIZE=3100;
    private List<Student> students=new ArrayList<>(ARRAYLIST_SIZE);
    private Context ctx;
    private Database db;

    public JSONParser(Context ctx){
        this.ctx=ctx;
        db=new Database(ctx);
    }

    public void parse(String jsnString) throws JSONException {
        JSONArray mainArray=new JSONArray(jsnString);
        Student student;
        for (int i=0;i<mainArray.length();i++){
            JSONObject jsnObj=mainArray.getJSONObject(i);
            String hashStr=jsnObj.getString(JSON_ID);
            String firstName=jsnObj.getString(JSON_FIRSTNAME);
            String lastName=jsnObj.getString(JSON_LASTNAME);
            long birthday=jsnObj.getLong(JSON_BIRTHDAY);
            int courceId;
            int mark;
            student=new Student(hashStr,firstName,lastName,birthday);
            JSONArray coursesArray=jsnObj.getJSONArray(JSON_COURSES);
            for (int j=0;j<coursesArray.length();j++){
                JSONObject courceJsnObj=coursesArray.getJSONObject(j);
                courceId=CourseUtil.getCourseId(courceJsnObj.getString(JSON_COURSE_NAME));
                mark=courceJsnObj.getInt(JSON_COURSE_MARK);
                student.setMark(courceId,mark);
            }
            printStudent(student);
            students.add(student);
        }

        saveListInDataBase(students);

    }

    private void saveListInDataBase(List<Student> students) {
        db.openSqlConnection();
        db.saveStudentsData(students);
        nextAppLaunchNoNeedFillDatabase();
        db.closeSqlConnection();
    }

    private void nextAppLaunchNoNeedFillDatabase() {
        SharedPreferences sharedPref=ctx.getSharedPreferences(MainActivity.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPref.edit();
        ed.putBoolean(MainActivity.SHARED_PREF_FIRST_TIME_KEY,false);
        ed.commit();
    }

    private void printStudent(Student student) {
        if (student != null) {
            Log.v(TAG, student.toString());
        }
    }

    private void setMarkAndCourse(Student student) {
        if (mark != UNINITIALIZED && courseId != UNINITIALIZED && student != null) {
            student.setMark(courseId, mark);
            mark = UNINITIALIZED;
            courseId = UNINITIALIZED;
        }
    }


}
