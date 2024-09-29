package com.example.demo_atten;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class StudentCheckboxAdapter extends ArrayAdapter<Student> {

    private StudentListActivity context;
    private List<Student> studentList;

    public StudentCheckboxAdapter(StudentListActivity context, List<Student> students) {
        super(context, 0, students);
        this.context = context;
        this.studentList = students;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate custom student list item layout if not already done
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.student_list_item, parent, false);
        }

        // Get current student
        Student student = studentList.get(position);

        // Find views in the custom layout
        CheckBox studentCheckBox = convertView.findViewById(R.id.studentCheckBox);
        TextView studentNameTextView = convertView.findViewById(R.id.studentNameTextView);
        TextView studentRollNumberTextView = convertView.findViewById(R.id.studentRollNumberTextView);

        // Set student name and roll number
        studentNameTextView.setText(student.getName());
        studentRollNumberTextView.setText(student.getRollNumber());

        // Set checkbox state based on whether student is marked present
        studentCheckBox.setChecked(student.isChecked());

        // Set listener for when checkbox changes
        studentCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            student.setChecked(isChecked);
        });

        return convertView;
    }
}
