package com.example.demo_atten;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentAttendanceAdapter extends ArrayAdapter<StudentAttendanceItem> {

    public StudentAttendanceAdapter(Context context, ArrayList<StudentAttendanceItem> students) {
        super(context, 0, students);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StudentAttendanceItem studentItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView studentNameTextView = convertView.findViewById(android.R.id.text1);
        TextView attendanceTextView = convertView.findViewById(android.R.id.text2);

        studentNameTextView.setText(studentItem.getStudentName() + " (Roll: " + studentItem.getRollNumber() + ")");
        attendanceTextView.setText(studentItem.getAttendanceRatio());

        return convertView;
    }
}
