package com.example.demo_atten;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentListActivity extends AppCompatActivity {

    ListView studentListView;
    ArrayList<Student> studentList;
    StudentCheckboxAdapter studentAdapter;
    FirebaseFirestore db;
    Button submitAttendanceButton;
    String facultyName, className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        studentListView = findViewById(R.id.studentListView);
        submitAttendanceButton = findViewById(R.id.submitAttendanceButton);
        db = FirebaseFirestore.getInstance();

        // Get the selected class and faculty name from the intent
        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        facultyName = intent.getStringExtra("facultyName");

        if (className != null) {
            loadStudentList(className);
        } else {
            Toast.makeText(this, "Class name is missing.", Toast.LENGTH_SHORT).show();
        }

        // Handle the submit attendance button click
        submitAttendanceButton.setOnClickListener(v -> submitAttendance());
    }

    // Method to load the student list for the selected class
    private void loadStudentList(String classString) {
        studentList = new ArrayList<>();

        // Query Firestore to find the students in the selected class
        db.collection("Classes")
                .whereEqualTo("className", classString)
                .whereEqualTo("facultyName", facultyName) // Match faculty name as well
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            String documentId = task.getResult().getDocuments().get(0).getId();

                            // Use the document ID to access the "students" subcollection
                            db.collection("Classes").document(documentId).collection("students")
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            QuerySnapshot result = task1.getResult();
                                            if (result != null && !result.isEmpty()) {
                                                for (DocumentSnapshot document : result) {
                                                    String studentName = document.getString("name");
                                                    String rollNumber = document.getString("rollNumber");
                                                    studentList.add(new Student(studentName, rollNumber));
                                                }

                                                // Set the custom adapter
                                                studentAdapter = new StudentCheckboxAdapter(StudentListActivity.this, studentList);
                                                studentListView.setAdapter(studentAdapter);
                                            } else {
                                                Log.d("StudentListActivity", "No students found for class: " + classString);
                                                Toast.makeText(StudentListActivity.this, "No students found for this class.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Log.d("StudentListActivity", "Error getting students: ", task1.getException());
                                            Toast.makeText(StudentListActivity.this, "Error retrieving students.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Log.d("StudentListActivity", "Class not found: " + classString);
                        }
                    }
                });
    }

    // Method to submit attendance to Firebase
    // Method to submit attendance to Firebase
    // Method to submit attendance to Firebase
    // Method to submit attendance to Firebase
    private void submitAttendance() {
        for (Student student : studentList) {
            if (student.isChecked()) {
                // Reference to the attendance subcollection for the specific student
                DocumentReference studentRef = db.collection("Classes")
                        .document(className) // Using className as the document ID
                        .collection("students")
                        .document(student.getRollNumber()) // Using roll number as the document ID for students
                        .collection("attendance")
                        .document(className); // Using className (same as subject name) as the document ID for attendance

                // Get the existing attendance document for the subject
                studentRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Map<String, Object> attendanceData = new HashMap<>();
                        attendanceData.put("status", "Present");
                        attendanceData.put("count", 1); // Attendance count for this instance

                        if (document.exists()) {
                            // If the attendance document exists, increment the total count
                            Long currentTotalCount = document.getLong("totalCount");
                            Long newTotalCount = (currentTotalCount != null ? currentTotalCount : 0) + 1;

                            // Update the existing attendance document with new counts
                            attendanceData.put("totalCount", newTotalCount); // Incremented total count
                            studentRef.update(attendanceData)
                                    .addOnSuccessListener(aVoid -> Log.d("StudentListActivity", "Attendance updated for: " + student.getRollNumber()))
                                    .addOnFailureListener(e -> Log.d("StudentListActivity", "Error updating attendance", e));
                        } else {
                            // If the document doesn't exist, create a new record
                            attendanceData.put("totalCount", 1); // Initialize total count
                            studentRef.set(attendanceData)
                                    .addOnSuccessListener(aVoid -> Log.d("StudentListActivity", "Attendance created for: " + student.getRollNumber()))
                                    .addOnFailureListener(e -> Log.d("StudentListActivity", "Error creating attendance", e));
                        }
                    } else {
                        Log.d("StudentListActivity", "Error getting attendance document: ", task.getException());
                    }
                });
            }
        }

        Toast.makeText(this, "Attendance submitted for class: " + className, Toast.LENGTH_SHORT).show();
    }


}




