package com.example.demo_atten;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class Make_class extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AttendanceSystem.db";
    private static final int DATABASE_VERSION = 2;

    // Table for classes
    private static final String TABLE_CLASS = "classes";
    private static final String COLUMN_CLASS_ID = "class_id";
    private static final String COLUMN_CLASS_NAME = "class_name";
    private static final String COLUMN_SEMESTER = "semester";

    public Make_class(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Log the table creation process
        Log.d("Database", "onCreate called - Creating table...");
        String CREATE_CLASS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CLASS + " ("
                + COLUMN_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CLASS_NAME + " TEXT, "
                + COLUMN_SEMESTER + " TEXT)";
        db.execSQL(CREATE_CLASS_TABLE);
        Log.d("Database", "Table created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Log the upgrade process
        Log.d("Database", "onUpgrade called from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS);
        onCreate(db); // Recreate table
        Log.d("Database", "Table recreated successfully");
    }

    // Method to add a new class
    public boolean addClass(String className, String semester) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_NAME, className);
        values.put(COLUMN_SEMESTER, semester);

        try {
            long result = db.insert(TABLE_CLASS, null, values);
            if (result == -1) {
                Log.e("Database Error", "Failed to insert class");
            } else {
                Log.d("Database", "Class inserted successfully with ID: " + result);
            }
            return result != -1;
        } catch (Exception e) {
            Log.e("Database Error", "Error inserting data", e);
            return false;
        } finally {
            db.close();
        }
    }

    // Method to get all classes
    public Cursor getAllClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_CLASS, null);
            Log.d("Database", "Query successful. Retrieved " + cursor.getCount() + " rows.");
        } catch (Exception e) {
            Log.e("Database Error", "Error retrieving classes", e);
        }
        return cursor;
    }
}


