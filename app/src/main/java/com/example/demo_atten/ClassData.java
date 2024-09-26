package com.example.demo_atten;

public class ClassData {
    public String id;
    public String className;
    public String semester;
    public String facultyName;

    public ClassData() {} // Default constructor required for Firebase

    public ClassData(String id, String className, String semester, String facultyName) {
        this.id = id;
        this.className = className;
        this.semester = semester;
        this.facultyName = facultyName;
    }
}
