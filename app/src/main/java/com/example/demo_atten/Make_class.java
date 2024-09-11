package com.example.demo_atten;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class Make_class extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4; // Updated version to 4
    private static final String DATABASE_NAME = "AttendanceDB.db";

    // Table for classes
    private static final String TABLE_CLASS = "classes";
    private static final String COLUMN_CLASS_ID = "class_id";
    private static final String COLUMN_CLASS_NAME = "class_name";
    private static final String COLUMN_SEMESTER = "semester";
    private static final String COLUMN_FACULTY_NAME = "faculty_name"; // New column to store faculty name

    public Make_class(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CLASS_TABLE = "CREATE TABLE " + TABLE_CLASS + " ("
                + COLUMN_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CLASS_NAME + " TEXT, "
                + COLUMN_SEMESTER + " TEXT, "
                + COLUMN_FACULTY_NAME + " TEXT)"; // New column
        db.execSQL(CREATE_CLASS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS);
        onCreate(db); // Recreate table
    }

    // Method to add a new class with faculty name
    public boolean addClass(String className, String semester, String facultyName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_NAME, className);
        values.put(COLUMN_SEMESTER, semester);
        values.put(COLUMN_FACULTY_NAME, facultyName); // Save faculty name

        long result = db.insert(TABLE_CLASS, null, values);
        return result != -1; // Return true if inserted successfully
    }

    // Method to get all classes created by the logged-in faculty
    public Cursor getClassesByFaculty(String facultyName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CLASS + " WHERE " + COLUMN_FACULTY_NAME + " = ?";
        return db.rawQuery(query, new String[]{facultyName});
    }
}



