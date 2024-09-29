package com.example.demo_atten;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentAttendanceActivity extends AppCompatActivity {

    ListView studentListView;
    ArrayList<StudentAttendanceItem> studentAttendanceList; // Create this model class
    StudentAttendanceAdapter studentAttendanceAdapter; // Create this adapter to display students
    DatabaseReference databaseReference;
    String className, facultyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setIcon(R.drawable.baseline_arrow_back_24);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setTitle("Student Attendance");

        // Initialize variables
        studentListView = findViewById(R.id.studentListView);
        studentAttendanceList = new ArrayList<>();

        // Get the class name and faculty name from the intent
        className = getIntent().getStringExtra("className");
        facultyName = getIntent().getStringExtra("facultyName");

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Classes");

        // Load students for the selected class
        loadStudents(className);
    }

    private void loadStudents(String classString) {
        databaseReference.orderByChild("className").equalTo(classString)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                                DataSnapshot studentsSnapshot = classSnapshot.child("students");

                                if (studentsSnapshot.exists()) {
                                    for (DataSnapshot studentSnapshot : studentsSnapshot.getChildren()) {
                                        String studentName = studentSnapshot.child("name").getValue(String.class);
                                        String rollNumber = studentSnapshot.child("rollNumber").getValue(String.class);
                                        int attendanceCount = studentSnapshot.child("attendance_cnt").getValue(Integer.class);
                                        int totalCount = studentSnapshot.child("Total_cnt").getValue(Integer.class);
                                        studentAttendanceList.add(new StudentAttendanceItem(studentName, rollNumber, attendanceCount, totalCount));
                                    }

                                    // Set the custom adapter to display student attendance
                                    studentAttendanceAdapter = new StudentAttendanceAdapter(StudentAttendanceActivity.this, studentAttendanceList);
                                    studentListView.setAdapter(studentAttendanceAdapter);
                                } else {
                                    Toast.makeText(StudentAttendanceActivity.this, "No students found for this class.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(StudentAttendanceActivity.this, "Class not found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StudentAttendanceActivity.this, "Error loading students: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

