package com.example.demo_atten;

import androidx.annotation.NonNull;

public class Student implements Comparable<Student> {
    private String name;
    private String rollNumber;
    private boolean checked;
    private int attendanceCount;
    private int totalCount;

    public Student(String name, String rollNumber) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.checked = false;
        this.attendanceCount = 0;
        this.totalCount = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getAttendanceCount() {
        return attendanceCount;
    }

    public void incrementAttendanceCount() {
        this.attendanceCount++;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void incrementTotalCount() {
        this.totalCount++;
    }

    @Override
    public int compareTo(Student o) {
        return this.rollNumber.compareTo(o.getRollNumber());
    }

    @NonNull
    @Override
    public String toString() {
        return name + " (" + rollNumber + ")";
    }
}
