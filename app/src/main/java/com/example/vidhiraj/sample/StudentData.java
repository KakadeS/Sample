package com.example.vidhiraj.sample;

/**
 * Created by lenovo on 22/08/2016.
 */
public class StudentData {
    public String stud_name;
    public boolean stud_hostel;
public StudentData()
{

}
    public StudentData(String stud_name, boolean stud_hostel, String stud_class_name) {
        this.stud_name = stud_name;
        this.stud_hostel = stud_hostel;
        this.stud_class_name = stud_class_name;
    }

    public String getStud_name() {
        return stud_name;
    }

    public void setStud_name(String stud_name) {
        this.stud_name = stud_name;
    }

    public boolean isStud_hostel() {
        return stud_hostel;
    }

    public void setStud_hostel(boolean stud_hostel) {
        this.stud_hostel = stud_hostel;
    }

    public String getStud_class_name() {
        return stud_class_name;
    }

    public void setStud_class_name(String stud_class_name) {
        this.stud_class_name = stud_class_name;
    }

    public String stud_class_name;
}
