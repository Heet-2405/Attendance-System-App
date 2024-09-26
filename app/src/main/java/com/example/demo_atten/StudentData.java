package com.example.demo_atten;

public class StudentData {
    public String id;
    public String studentName;
    public String rollNumber;
    public String className;

    public StudentData() {} // Default constructor required for Firebase

    public StudentData(String id, String studentName, String rollNumber, String className) {
        this.id = id;
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.className = className;
    }
}
