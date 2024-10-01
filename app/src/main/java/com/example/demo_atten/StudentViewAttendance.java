package com.example.demo_atten;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentViewAttendance extends AppCompatActivity {

    private TextView studentNameTextView, studentRollNumberTextView, attendanceTextView;
    private DatabaseReference databaseReference;
    private String rollNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_attendance);

        studentNameTextView = findViewById(R.id.studentNameTextView);
        studentRollNumberTextView = findViewById(R.id.studentRollNumberTextView);
        attendanceTextView = findViewById(R.id.attendanceTextView); // New TextView for attendance

        // Get the passed data from the Intent
        String studentName = getIntent().getStringExtra("studentName");
        rollNumber = getIntent().getStringExtra("rollNumber");

        // Set the TextViews with the received data
        studentNameTextView.setText("Student Name: " + studentName);
        studentRollNumberTextView.setText("Roll Number: " + rollNumber);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Classes");

        // Fetch attendance data
        fetchAttendanceData();

        // Set ActionBar title
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Student Attendance");
        }
    }

    private void fetchAttendanceData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder attendanceData = new StringBuilder("Attendance:\n");

                for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                    String className = classSnapshot.child("className").getValue(String.class);
                    String facultyName = classSnapshot.child("facultyName").getValue(String.class);
                    DataSnapshot studentsSnapshot = classSnapshot.child("students");

                    int totalAttendanceForClass = 0;
                    int totalClassesForClass = 0;

                    for (DataSnapshot studentSnapshot : studentsSnapshot.getChildren()) {
                        String dbRollNumber = studentSnapshot.child("rollNumber").getValue(String.class);

                        // Check if the roll number matches
                        if (dbRollNumber != null && dbRollNumber.equals(rollNumber)) {
                            // Retrieve attendance data for the student
                            int attendanceCnt = studentSnapshot.child("attendance_cnt").getValue(Integer.class);
                            int totalCnt = studentSnapshot.child("Total_cnt").getValue(Integer.class);

                            // Add faculty-specific attendance information
                            attendanceData.append("Class: ").append(className)
                                    .append("\nFaculty: ").append(facultyName)
                                    .append("\nAttended: ").append(attendanceCnt)
                                    .append(" / Total: ").append(totalCnt).append("\n\n");

                            // Accumulate total attendance and total classes
                            totalAttendanceForClass += attendanceCnt;
                            totalClassesForClass += totalCnt;
                        }
                    }

                    // After looping through all students for a class, add total attendance for the subject
                    if (totalClassesForClass > 0) {
                        attendanceData.append("Total for Class ").append(className).append(": ")
                                .append("Attended: ").append(totalAttendanceForClass)
                                .append(" / Total: ").append(totalClassesForClass).append("\n\n");
                    }
                }

                // Display the attendance data
                attendanceTextView.setText(attendanceData.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentViewAttendance.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
