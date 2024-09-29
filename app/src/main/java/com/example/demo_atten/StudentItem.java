package com.example.demo_atten;

public class StudentItem {
    private final String name;
    private final String rollNumber;
    private final String attendanceCnt;
    private final String totalCnt;

    public StudentItem(String name, String rollNumber, String attendanceCnt, String totalCnt) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.attendanceCnt = attendanceCnt;
        this.totalCnt = totalCnt;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getAttendanceCnt() {
        return attendanceCnt;
    }

    public String getTotalCnt() {
        return totalCnt;
    }
}

