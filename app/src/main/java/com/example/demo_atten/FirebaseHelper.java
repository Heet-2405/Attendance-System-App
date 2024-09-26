package com.example.demo_atten;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {

    private final DatabaseReference databaseReference;

    public FirebaseHelper() {
        // Get a reference to the Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("faculty");
    }

    // Register new faculty in Firebase
    public void registerFaculty(String name, String password) {
        String facultyId = databaseReference.push().getKey();  // Unique key for each faculty

        Faculty faculty = new Faculty(name, password); // Create a Faculty object

        if (facultyId != null) {
            databaseReference.child(facultyId).setValue(faculty)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseHelper", "Faculty registered successfully.");
                        } else {
                            Log.d("FirebaseHelper", "Failed to register faculty.");
                        }
                    });
        }
    }

    // Check if faculty exists in Firebase
    public void checkFaculty(String name, String password, FirebaseCallback callback) {
        databaseReference.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean exists = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Faculty faculty = snapshot.getValue(Faculty.class);
                    if (faculty != null && faculty.getPassword().equals(password)) {
                        exists = true;
                        break;
                    }
                }
                callback.onFacultyFound(exists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFacultyFound(false);
            }
        });
    }
}

class Faculty {
    public String name;
    public String password;

    public Faculty() {
        // Default constructor required for calls to DataSnapshot.getValue(Faculty.class)
    }

    public Faculty(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
