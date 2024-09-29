package com.example.demo_atten;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassListAdapter extends ArrayAdapter<ClassItem> {

    public ClassListAdapter(Context context, ArrayList<ClassItem> classes) {
        super(context, 0, classes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ClassItem classItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Lookup view for data population
        TextView classNameTextView = convertView.findViewById(android.R.id.text1);
        // Populate the data into the template view using the data object
        classNameTextView.setText(classItem.getClassName());

        // Return the completed view to render on screen
        return convertView;
    }
}
