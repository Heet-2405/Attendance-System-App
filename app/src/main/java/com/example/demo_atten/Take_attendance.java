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

public class Take_attendance extends AppCompatActivity {

    ListView classListView;
    ArrayList<String> classList;
    ArrayAdapter<String> classAdapter;
    String facultyName; // Store the faculty name
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

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
            Intent intent = new Intent(Take_attendance.this, StudentListActivity.class);
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
                                String className = classSnapshot.child("className").getValue(String.class);
                                String semester = classSnapshot.child("semester").getValue(String.class);
                                classList.add("Class: " + className + ", Semester: " + semester);
                            }
                            classAdapter = new ArrayAdapter<>(Take_attendance.this, android.R.layout.simple_list_item_1, classList);
                            classListView.setAdapter(classAdapter);
                        } else {
                            Log.d("Take_attendance", "No classes found for this faculty.");
                            Toast.makeText(Take_attendance.this, "No classes found for this faculty.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Take_attendance", "Error retrieving classes: " + error.getMessage());
                        Toast.makeText(Take_attendance.this, "Error retrieving classes.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
