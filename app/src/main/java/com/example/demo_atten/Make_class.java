package com.example.demo_atten;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;





import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Make_class extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "attendance.db";
    private static final int DATABASE_VERSION = 4;

    // Table names
    private static final String TABLE_STUDENT = "student_table";
    private static final String TABLE_CLASS = "class_table";

    // Columns for student table
    private static final String COLUMN_STUDENT_ID = "id";
    private static final String COLUMN_STUDENT_NAME = "student_name";
    private static final String COLUMN_ROLL_NUMBER = "roll_number";
    private static final String COLUMN_CLASS_NAME = "class_name";

    // Columns for class table
    private static final String COLUMN_CLASS_ID = "id";
    private static final String COLUMN_CLASS_NAME_CLASS = "class_name";
    private static final String COLUMN_SEMESTER = "semester";
    private static final String COLUMN_FACULTY_NAME = "faculty_name";

    public Make_class(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the class table
        String CREATE_CLASS_TABLE = "CREATE TABLE " + TABLE_CLASS + " (" +
                COLUMN_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLASS_NAME_CLASS + " TEXT, " +
                COLUMN_SEMESTER + " TEXT, " +
                COLUMN_FACULTY_NAME + " TEXT)";
        db.execSQL(CREATE_CLASS_TABLE);

        // Create the student table
        String CREATE_STUDENT_TABLE = "CREATE TABLE " + TABLE_STUDENT + " (" +
                COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_NAME + " TEXT, " +
                COLUMN_ROLL_NUMBER + " TEXT, " +
                COLUMN_CLASS_NAME + " TEXT)";
        db.execSQL(CREATE_STUDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS);
        onCreate(db);
    }

    // Method to add a new class
    public boolean addClass(String className, String semester, String facultyName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_NAME_CLASS, className);
        values.put(COLUMN_SEMESTER, semester);
        values.put(COLUMN_FACULTY_NAME, facultyName);

        long result = db.insert(TABLE_CLASS, null, values);
        return result != -1; // Return true if insertion is successful
    }

    // Method to add a student to a class
    public boolean addStudent(String studentName, String rollNumber, String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_NAME, studentName);
        values.put(COLUMN_ROLL_NUMBER, rollNumber);
        values.put(COLUMN_CLASS_NAME, className);

        long result = db.insert(TABLE_STUDENT, null, values);
        return result != -1; // Return true if insertion is successful
    }

    // Method to get all classes by faculty
    public Cursor getClassesByFaculty(String facultyName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CLASS, null, COLUMN_FACULTY_NAME + " = ?", new String[]{facultyName}, null, null, null);
    }

    // Method to get all students for a particular class
    public Cursor getStudentsByClass(String className) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Log the query for debugging purposes
        Log.d("Make_class", "Querying students for class: " + className);

        return db.rawQuery("SELECT * FROM student_table WHERE class_name = ?", new String[]{className});
    }

}






