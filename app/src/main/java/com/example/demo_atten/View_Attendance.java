package com.example.demo_atten;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class View_Attendance extends AppCompatActivity {

    ListView classListView;
    ArrayList<String> classList;
    ArrayAdapter<String> classAdapter;
    String facultyName; // Store the faculty name
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        classListView = findViewById(R.id.classListView);

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Classes");

        // Get the logged-in faculty's name from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        facultyName = preferences.getString("facultyName", ""); // Retrieve faculty name

        loadClassList();

        // Handle class selection
        classListView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedClass = classList.get(position);

            // Extract class details from the selected item
            String className = selectedClass.split(",")[0].replace("Class: ", "").trim();

            // Start StudentListActivity and pass the class name
            Intent intent = new Intent(View_Attendance.this, List_Of_Students.class);
            intent.putExtra("className", className); // Pass className
            intent.putExtra("facultyName", facultyName); // Pass faculty name
            startActivity(intent);
        });
    }

    private void loadClassList() {
        classList = new ArrayList<>();

        databaseReference.orderByChild("facultyName").equalTo(facultyName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                                // Try to retrieve className and semester safely
                                Object classNameObj = classSnapshot.child("className").getValue();
                                Object semesterObj = classSnapshot.child("semester").getValue();

                                // Convert to string, handling cases where the data might be stored as a Long
                                String className = (classNameObj instanceof String) ? (String) classNameObj : String.valueOf(classNameObj);
                                String semester = (semesterObj instanceof String) ? (String) semesterObj : String.valueOf(semesterObj);

                                classList.add("Class: " + className + ", Semester: " + semester);
                            }
                            classAdapter = new ArrayAdapter<>(View_Attendance.this, android.R.layout.simple_list_item_1, classList);
                            classListView.setAdapter(classAdapter);
                        } else {
                            Log.d("View_Attendance", "No classes found for this faculty.");
                            Toast.makeText(View_Attendance.this, "No classes found for this faculty.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("View_Attendance", "Error retrieving classes: " + error.getMessage());
                        Toast.makeText(View_Attendance.this, "Error retrieving classes.", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
