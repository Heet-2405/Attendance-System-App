package com.example.demo_atten;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
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



    ListView classListView;
    Make_class dbHelper;
    ArrayList<String> classList;
    ArrayAdapter<String> adapter;
    String facultyName;

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
    }

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


}