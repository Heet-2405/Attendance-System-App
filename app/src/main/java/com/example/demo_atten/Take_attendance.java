package com.example.demo_atten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Take_attendance extends AppCompatActivity {

    ListView classListView;
    ArrayList<String> classList;
    ArrayAdapter<String> classAdapter;
    String facultyName; // Store the faculty name
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        classListView = findViewById(R.id.classListView);
        db = FirebaseFirestore.getInstance();

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

    // Method to load the class list from Firestore
    private void loadClassList() {
        classList = new ArrayList<>();
        db.collection("Classes")
                .whereEqualTo("facultyName", facultyName) // Use facultyName for filtering
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (result != null && !result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                    String className = document.getString("className");
                                    String semester = document.getString("semester"); // Optional, if you have semester
                                    classList.add("Class: " + className + ", Semester: " + semester);
                                }
                                classAdapter = new ArrayAdapter<>(Take_attendance.this, android.R.layout.simple_list_item_1, classList);
                                classListView.setAdapter(classAdapter);
                            } else {
                                Log.d("Take_attendance", "No classes found for this faculty.");
                            }
                        } else {
                            Log.d("Take_attendance", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
