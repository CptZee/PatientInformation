package com.example.patientinformation.Data.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.patientinformation.Data.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordHelper extends SQLiteOpenHelper {
    public RecordHelper(Context context) {
        super(context, "db_Medical", null, 1);
    }
    private String TABLENAME = "tbl_Records";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + "(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "patientID INTEGER," +
                    "historyID INTEGER," +
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

    public void insert(Record data){
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.insert(TABLENAME, null, prepareData(data));
            Log.i("DatabaseHelper", "Successfully inserted into the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
    }

    public List<Record> get(){
        SQLiteDatabase db = getReadableDatabase();
        List<Record> list = new ArrayList<>();
        try{
            Cursor cursor = db.rawQuery("SELECT ID, patientID, historyID FROM " + TABLENAME + " WHERE archived = 0", null);
            while (cursor.moveToNext())
                list.add(prepareData(cursor));
            Log.i("DatabaseHelper", "Successfully retrieved from the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
        return list;
    }

    public Record get(int ID){
        SQLiteDatabase db = getReadableDatabase();
        Record data = null;
        try{
            Cursor cursor = db.rawQuery("SELECT ID, patientID, historyID FROM " + TABLENAME + " WHERE ID = ?", new String[]{String.valueOf(ID)});
            while (cursor.moveToNext())
                data = prepareData(cursor);
            Log.i("DatabaseHelper", "Successfully retrieved from the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
        return data;
    }

    public void update(Record data){
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues values = prepareData(data);
            db.update(TABLENAME, values, "ID = ?", new String[]{String.valueOf(data.getID())});
            Log.i("DatabaseHelper", "Successfully updated a data with ID " + data.getID() + " from the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
    }

    public void remove(Record data){
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

    private ContentValues prepareData(Record data){
        ContentValues content = new ContentValues();
        if(data.getPatientID() != 0)
            content.put("patientID", data.getPatientID());
        if(data.getHistoryID() != 0)
            content.put("historyID", data.getHistoryID());
        content.put("archived", 0);
        return content;
    }
    private Record prepareData(Cursor cursor){
        Record data = new Record();
        data.setID(cursor.getInt(0));
        data.setPatientID(cursor.getInt(1));
        data.setHistoryID(cursor.getInt(2));
        return data;
    }
}