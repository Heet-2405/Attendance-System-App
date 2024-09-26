package com.example.demo_atten;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class StudentCheckboxAdapter extends ArrayAdapter<Student> {
    Student student;
    private final Context context;
    private final ArrayList<Student> students;

    public StudentCheckboxAdapter(@NonNull Context context, ArrayList<Student> students) {
        super(context, R.layout.student_list_item, students);
        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.student_list_item, parent, false);
        }

        student = students.get(position);

        // Get references to the views in the layout
        CheckBox studentCheckBox = convertView.findViewById(R.id.studentCheckBox);
        TextView studentNameRollTextView = convertView.findViewById(R.id.studentNameRollTextView);

        // Set the student details in the TextView
        studentNameRollTextView.setText(student.toString());

        // Handle checkbox selection
        studentCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            student.setChecked(isChecked);
        });

        return convertView;
    }
}

