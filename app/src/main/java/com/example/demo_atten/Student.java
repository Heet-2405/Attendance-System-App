package com.example.demo_atten;

import androidx.annotation.NonNull;

public class Student implements Comparable<Student> {
    private String name;
    private String rollNumber;
    private boolean checked;  // Indicates whether the student is marked as present

    // Constructor
    public Student(String name, String rollNumber) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.checked = false;  // Default value: not present
    }

    // Getter for the name
    public String getName() {
        return name;
    }

    // Setter for the name (in case you want to modify it)
    public void setName(String name) {
        this.name = name;
    }

    // Getter for the roll number
    public String getRollNumber() {
        return rollNumber;
    }

    // Setter for the roll number (in case you want to modify it)
    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    // Getter for the "checked" status (whether the student is marked as present)
    public boolean isChecked() {
        return checked;
    }

    // Setter for the "checked" status (to mark the student as present or not)
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    // Compare students by roll number
    @Override
    public int compareTo(Student other) {
        return this.rollNumber.compareTo(other.rollNumber);  // Sort by roll number
    }

    // Custom toString method for displaying the student's name and roll number in a list
    @NonNull
    @Override
    public String toString() {
        return "Name: " + name + ", Roll No: " + rollNumber;
    }
}
