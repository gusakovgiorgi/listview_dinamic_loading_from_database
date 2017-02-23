package net.gusakov.ddatestapp.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by hasana on 2/22/2017.
 */

public class Student {
    private int id;
    private String hashId;
    private String firstName;
    private String lastName;
    private long birthdayTimeStamp;
    private Map<Integer,Integer> marks=new HashMap<>(4,1.25F);

    public Student(){
    }

    public Student(int id, String hashId, String firstName, String lastName, long birthdayTimeStamp) {
        this.id = id;
        this.hashId = hashId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdayTimeStamp = birthdayTimeStamp;
    }
    public Student(String hashId, String firstName, String lastName, long birthdayTimeStamp) {
        this.id = CourseUtil.UNINITIALIZED;
        this.hashId = hashId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdayTimeStamp = birthdayTimeStamp;
    }
    public void setMark(int courseId,int mark){
        if(CourseUtil.isValidId(courseId)){
            marks.put(courseId,mark);
        }
    }

    public int getMark(int courseId){
        if(CourseUtil.isValidId(courseId)){
            return marks.get(courseId);
        }
        return -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getBirthdayTimeStamp() {
        return birthdayTimeStamp;
    }

    public void setBirthdayTimeStamp(long birthdayTimeStamp) {
        this.birthdayTimeStamp = birthdayTimeStamp;
    }

    public Map<Integer, Integer> getMarks() {
        return marks;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder("[");


        for (Integer key:marks.keySet()) {
            sb.append(CourseUtil.getCourseString(key)+"-"+(marks.get(key))+",");
        }
        return "Student{" +
                "id=" + id +
                ", hashId='" + hashId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthdayTimeStamp=" + birthdayTimeStamp +
                ", marks=" + sb.toString() +
                '}';
    }
}
