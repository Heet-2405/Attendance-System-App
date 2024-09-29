package com.example.demo_atten;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class List_Of_Students extends AppCompatActivity {

    ListView studentListView;
    ArrayList<String> studentList;
    ArrayAdapter<String> studentAdapter;
    String className, facultyName; // Get the class and faculty name
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_students);

        studentListView = findViewById(R.id.studentListView);

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Classes");

        // Get the class name and faculty name from the intent
        className = getIntent().getStringExtra("className");
        facultyName = getIntent().getStringExtra("facultyName");

        loadStudentList();
    }

    private void loadStudentList() {
        studentList = new ArrayList<>();

        // Query the students for the selected class
        databaseReference.orderByChild("className").equalTo(className)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                                DataSnapshot studentsSnapshot = classSnapshot.child("students");
                                for (DataSnapshot studentSnapshot : studentsSnapshot.getChildren()) {
                                    // Handle name, rollNumber, attendanceCount, and totalCount safely
                                    Object nameObj = studentSnapshot.child("name").getValue();
                                    Object rollNumberObj = studentSnapshot.child("rollNumber").getValue();
                                    Object attendanceCountObj = studentSnapshot.child("attendance_cnt").getValue();
                                    Object totalCountObj = studentSnapshot.child("Total_cnt").getValue();

                                    // Convert the values to String, handling cases where they might be Long
                                    String name = (nameObj instanceof String) ? (String) nameObj : String.valueOf(nameObj);
                                    String rollNumber = (rollNumberObj instanceof String) ? (String) rollNumberObj : String.valueOf(rollNumberObj);
                                    String attendanceCount = (attendanceCountObj instanceof String) ? (String) attendanceCountObj : String.valueOf(attendanceCountObj);
                                    String totalCount = (totalCountObj instanceof String) ? (String) totalCountObj : String.valueOf(totalCountObj);

                                    // Add student details to the list
                                    studentList.add( rollNumber + " " + name +
                                            ", Attendance: " + attendanceCount + "/" + totalCount);
                                }
                            }
                            studentAdapter = new ArrayAdapter<>(List_Of_Students.this, android.R.layout.simple_list_item_1, studentList);
                            studentListView.setAdapter(studentAdapter);
                        } else {
                            Log.d("List_Of_Students", "No students found for this class.");
                            Toast.makeText(List_Of_Students.this, "No students found for this class.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("List_Of_Students", "Error retrieving students: " + error.getMessage());
                        Toast.makeText(List_Of_Students.this, "Error retrieving students.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
