package com.example.demo_atten;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StudentListActivity extends AppCompatActivity {

    ListView studentListView;
    ArrayList<Student> studentList;
    StudentCheckboxAdapter studentAdapter;
    DatabaseReference databaseReference;
    Button submitAttendanceButton;
    CheckBox allPresentCheckBox, allAbsentCheckBox;
    String facultyName, className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // Initialize views
        studentListView = findViewById(R.id.studentListView);
        submitAttendanceButton = findViewById(R.id.submitAttendanceButton);
        allPresentCheckBox = findViewById(R.id.allPresentCheckBox);
        allAbsentCheckBox = findViewById(R.id.allAbsentCheckBox);

        // Retrieve faculty name and class name from intent
        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        facultyName = intent.getStringExtra("facultyName");

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Classes");

        if (className != null && facultyName != null) {
            loadStudentList(className);
        } else {
            Toast.makeText(this, "Class name or Faculty name is missing.", Toast.LENGTH_SHORT).show();
        }

        // Handle the submit attendance button click
        submitAttendanceButton.setOnClickListener(v -> submitAttendance());

        // Handle "All Present" checkbox
        allPresentCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                allAbsentCheckBox.setChecked(false); // Uncheck "All Absent"
                markAllStudentsPresent(true);
            }
        });

        // Handle "All Absent" checkbox
        allAbsentCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                allPresentCheckBox.setChecked(false); // Uncheck "All Present"
                markAllStudentsPresent(false);
            }
        });
    }

    // Load student list from Firebase
    // Load student list from Firebase
    private void loadStudentList(String classString) {
        studentList = new ArrayList<>();

        // Query Firebase to get students for the selected class
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
                                        studentList.add(new Student(studentName, rollNumber));
                                    }

                                    // Sort students by roll number (converted to integer for proper sorting)
                                    Collections.sort(studentList, (student1, student2) -> {
                                        int rollNumber1 = Integer.parseInt(student1.getRollNumber());
                                        int rollNumber2 = Integer.parseInt(student2.getRollNumber());
                                        return Integer.compare(rollNumber1, rollNumber2);
                                    });

                                    // Set the adapter to ListView
                                    studentAdapter = new StudentCheckboxAdapter(StudentListActivity.this, studentList);
                                    studentListView.setAdapter(studentAdapter);
                                } else {
                                    Toast.makeText(StudentListActivity.this, "No students found for this class.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(StudentListActivity.this, "Class not found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StudentListActivity.this, "Error retrieving students.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Mark all students as present or absent
    private void markAllStudentsPresent(boolean isPresent) {
        for (Student student : studentList) {
            student.setChecked(isPresent);
        }
        studentAdapter.notifyDataSetChanged();
    }

    // Submit attendance to Firebase
    private void submitAttendance() {
        databaseReference.orderByChild("className").equalTo(className)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                                DataSnapshot studentsSnapshot = classSnapshot.child("students");

                                for (Student student : studentList) {
                                    DataSnapshot studentSnapshot = studentsSnapshot.child(student.getRollNumber());
                                    DatabaseReference studentRef = studentSnapshot.getRef();

                                    // Check if "Total_cnt" and "attendance_cnt" exist
                                    int currentTotalCount = studentSnapshot.hasChild("Total_cnt") ? studentSnapshot.child("Total_cnt").getValue(Integer.class) : 0;
                                    int currentAttendanceCount = studentSnapshot.hasChild("attendance_cnt") ? studentSnapshot.child("attendance_cnt").getValue(Integer.class) : 0;

                                    // Increment total count
                                    studentRef.child("Total_cnt").setValue(currentTotalCount + 1);

                                    // Increment attendance count if student is marked as present
                                    if (student.isChecked()) {
                                        studentRef.child("attendance_cnt").setValue(currentAttendanceCount + 1);
                                    }
                                }
                            }
                            Toast.makeText(StudentListActivity.this, "Attendance submitted for class: " + className, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StudentListActivity.this, "Error updating counts.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
