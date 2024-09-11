package com.example.demo_atten;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Add_student extends AppCompatActivity {

    ListView classListView;
    Make_class dbHelper;
    ArrayList<String> classList;
    ArrayAdapter<String> adapter;
    String facultyName;
    EditText inputName;
    EditText inputRoll;
    String className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        classListView = findViewById(R.id.classListView);
        dbHelper = new Make_class(this);

        // Get the logged-in faculty's name
        SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        facultyName = preferences.getString("facultyName", "");

        loadClassList();

        // Handle class selection
        classListView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedClass = classList.get(position); // Get selected class name
            showAddStudentDialog(selectedClass);
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

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, classList);
        classListView.setAdapter(adapter);
    }

    // Show dialog to add a student
    // Show dialog to add a student with roll number
    private void showAddStudentDialog(String selectedClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Student");

        // Create a layout for the input fields
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Input field for student name
        inputName = new EditText(this);
        inputName.setHint("Enter student name");
        layout.addView(inputName);

        // Input field for roll number
        inputRoll = new EditText(this);
        inputRoll.setHint("Enter roll number");
        layout.addView(inputRoll);

        builder.setView(layout);

        // Extract class name from selected class
        String[] parts = selectedClass.split(",");
        className = parts[0].replace("Class: ", "").trim();

        // Set up the buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String studentName = inputName.getText().toString().trim();
            String rollNumber = inputRoll.getText().toString().trim();

            Log.d("AddStudent", "Class Name: " + className);
            Log.d("AddStudent", "Student Name: " + studentName);
            Log.d("AddStudent", "Roll Number: " + rollNumber);

            if (!studentName.isEmpty() && !rollNumber.isEmpty()) {
                if (dbHelper.addStudent(studentName, rollNumber, className)) {
                    Toast.makeText(getApplicationContext(), "Student added to " + className, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add student", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please enter both student name and roll number", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


}


