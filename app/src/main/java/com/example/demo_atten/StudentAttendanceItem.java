package com.example.demo_atten;

public class StudentAttendanceItem {
    private final String studentName;
    private final String rollNumber;
    private final int attendanceCount;
    private final int totalCount;

    public StudentAttendanceItem(String studentName, String rollNumber, int attendanceCount, int totalCount) {
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.attendanceCount = attendanceCount;
        this.totalCount = totalCount;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getAttendanceRatio() {
        return attendanceCount + " / " + totalCount;
    }
}

