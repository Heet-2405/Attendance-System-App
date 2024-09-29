package com.example.demo_atten;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;

public class StudentListAdapter extends ArrayAdapter<StudentItem> {
    private final Context mContext;
    private final List<StudentItem> studentList;

    public StudentListAdapter(@NonNull Context context, ArrayList<StudentItem> list) {
        super(context, 0, list);
        mContext = context;
        studentList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.student_list_item, parent, false);

        StudentItem currentStudent = studentList.get(position);

        TextView name = listItem.findViewById(R.id.studentName);
        name.setText(currentStudent.getName());

        TextView rollNumber = listItem.findViewById(R.id.studentRollNumber);
        rollNumber.setText(currentStudent.getRollNumber());

        TextView attendance = listItem.findViewById(R.id.studentAttendance);
        attendance.setText(currentStudent.getAttendanceCnt() + " / " + currentStudent.getTotalCnt());

        return listItem;
    }
}

