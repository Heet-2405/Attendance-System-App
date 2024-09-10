package com.example.demo_atten;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AttendanceSystem.db";
    private static final int DATABASE_VERSION = 3;

    // Table Name
    private static final String TABLE_FACULTY = "faculty";

    // Table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating table
        String CREATE_FACULTY_TABLE = "CREATE TABLE " + TABLE_FACULTY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_FACULTY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACULTY);
        onCreate(db);
    }

    public boolean registerFaculty(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_FACULTY, null, values);
        return result != -1; // Return true if inserted successfully
    }

    // Method to check if faculty exists
    public boolean checkFaculty(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_FACULTY + " WHERE " + COLUMN_NAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name, password});

        boolean exists = cursor.getCount() > 0;  // Check if a matching entry exists
        cursor.close();
        return exists;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle the downgrade logic here. For now, we can drop the table and recreate it.
        db.execSQL("DROP TABLE IF EXISTS faculty");
        onCreate(db);
    }







}
