package com.example.patientinformation.Data.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.patientinformation.Data.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientHelper extends SQLiteOpenHelper {
    public PatientHelper(Context context) {
        super(context, "db_Medical", null, 1);
    }
    private String TABLENAME = "tbl_Patients";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + "(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "firstName TEXT," +
                    "middleName TEXT," +
                    "lastName TEXT," +
                    "age INTEGER," +
                    "sex TEXT," +
                    "archived INTEGER)");
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to create table " + TABLENAME);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try{
            db.execSQL("DROP TABLE " + TABLENAME);
            onCreate(db);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
    }

    public void insert(Patient data){
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.insert(TABLENAME, null, prepareData(data));
            Log.i("DatabaseHelper", "Successfully inserted into the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
    }

    public List<Patient> get(){
        SQLiteDatabase db = getReadableDatabase();
        List<Patient> list = new ArrayList<>();
        try{
            Cursor cursor = db.rawQuery("SELECT ID, firstName, middleName, lastName, age, sex FROM " + TABLENAME + " WHERE archived = 0", null);
            while (cursor.moveToNext())
                list.add(prepareData(cursor));
            Log.i("DatabaseHelper", "Successfully retrieved from the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
        return list;
    }

    public Patient get(int ID){
        SQLiteDatabase db = getReadableDatabase();
        Patient data = null;
        try{
            Cursor cursor = db.rawQuery("SELECT ID, firstName, middleName, lastName, age, sex FROM " + TABLENAME + " WHERE ID = ?", new String[]{String.valueOf(ID)});
            while (cursor.moveToNext())
                data = prepareData(cursor);
            Log.i("DatabaseHelper", "Successfully retrieved from the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
        return data;
    }

    public Patient get(String name){
        SQLiteDatabase db = getReadableDatabase();
        Patient data = null;
        try{
            Cursor cursor = db.rawQuery("SELECT ID, firstName, middleName, lastName, age, sex FROM " + TABLENAME + " WHERE firstName = ?", new String[]{name});
            while (cursor.moveToNext())
                data = prepareData(cursor);
            Log.i("DatabaseHelper", "Successfully retrieved from the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
        return data;
    }

    public void update(Patient data){
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.update(TABLENAME, prepareData(data), "ID = ?", new String[]{String.valueOf(data.getID())});
            Log.i("DatabaseHelper", "Successfully updated a data with ID " + data.getID() + " from the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
    }

    public void remove(Patient data){
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues values = prepareData(data);
            values.put("archived", 1);
            db.update(TABLENAME, values, "ID = ?", new String[]{String.valueOf(data.getID())});
            Log.i("DatabaseHelper", "Successfully removed a data from the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
    }

    public int getID(){
        SQLiteDatabase db = getReadableDatabase();
        int data = 0;
        try{
            Cursor cursor = db.rawQuery("SELECT MAX(ID) FROM " + TABLENAME, null);
            while (cursor.moveToNext())
                data = cursor.getInt(0);
            data++;
            Log.i("DatabaseHelper", "Successfully retrieved from the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
        return data;
    }

    private ContentValues prepareData(Patient data){
        ContentValues content = new ContentValues();
        if(data.getFirstName() != null)
            content.put("firstName", data.getFirstName());
        if(data.getMiddleName() != null)
            content.put("middleName", data.getMiddleName());
        if(data.getLastName() != null)
            content.put("lastName", data.getLastName());
        if(data.getAge() != 0)
            content.put("age", data.getAge());
        if(data.getSex() != null)
            content.put("sex", data.getSex());
        content.put("archived", 0);
        return content;
    }
    private Patient prepareData(Cursor cursor){
        Patient data = new Patient();
        data.setID(cursor.getInt(0));
        data.setFirstName(cursor.getString(1));
        data.setMiddleName(cursor.getString(2));
        data.setLastName(cursor.getString(3));
        data.setAge(cursor.getInt(4));
        data.setSex(cursor.getString(5));
        return data;
    }
}
