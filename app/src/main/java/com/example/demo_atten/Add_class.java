package com.example.demo_atten;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Add_class extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setIcon(R.drawable.baseline_arrow_back_24);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setTitle("ATTENDANCE SYSTEM");

    }




    public void CreateClass(View view) {

        Toast.makeText(getApplicationContext(),"Class has been Created",Toast.LENGTH_LONG).show();
    }
}