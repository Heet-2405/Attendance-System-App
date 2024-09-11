package com.example.demo_atten;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Take_attendance extends AppCompatActivity {

    ListView classListView, studentListView;
    Make_class dbHelper;
    ArrayList<String> classList, studentList;
    ArrayAdapter<String> classAdapter, studentAdapter;
    String facultyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance); // Ensure the layout has two ListViews

        classListView = findViewById(R.id.classListView);
        studentListView = findViewById(R.id.studentListView); // Second ListView for students
        dbHelper = new Make_class(this);

        // Get the logged-in faculty's name
        SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        facultyName = preferences.getString("facultyName", "");

        loadClassList();

        // Handle class selection
        classListView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedClass = classList.get(position); // Get selected class name
            loadStudentList(selectedClass); // Load students for the selected class
        });
    }

    // Method to load the class list from the database
    private void loadClassList() {
        classList = new ArrayList<>();
        Cursor cursor = dbHelper.getClassesByFaculty(facultyName); // Get classes by faculty

        if (cursor.moveToFirst()) {
            do {
                String className = cursor.getString(cursor.getColumnIndexOrThrow("class_name"));
                String semester = cursor.getString(cursor.getColumnIndexOrThrow("semester"));
                classList.add("Class: " + className + ", Semester: " + semester);
            } while (cursor.moveToNext());
        }
        cursor.close();

        classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, classList);
        classListView.setAdapter(classAdapter);
    }

    // Method to load the student list for the selected class
    private void loadStudentList(String classString) {
        studentList = new ArrayList<>();

        // Extract the actual class name from "Class: html, Semester: 3"
        String className = classString.split(",")[0].replace("Class: ", "").trim();

        // Log the extracted class name to verify
        Log.d("Take_attendance", "Extracted class name: " + className);

        Cursor cursor = dbHelper.getStudentsByClass(className); // Get students by class

        if (cursor != null && cursor.getCount() > 0) {
            Log.d("Take_attendance", "Number of students found: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    String studentName = cursor.getString(cursor.getColumnIndexOrThrow("student_name"));
                    String rollNumber = cursor.getString(cursor.getColumnIndexOrThrow("roll_number"));
                    studentList.add("Name: " + studentName + ", Roll No: " + rollNumber);
                } while (cursor.moveToNext());
            }
        } else {
            Log.d("Take_attendance", "No students found for class: " + className);
        }

        if (cursor != null) {
            cursor.close();
        }

        studentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        studentListView.setAdapter(studentAdapter);
    }


}

