package com.example.patientinformation.Data.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.patientinformation.Data.History;
import com.example.patientinformation.Data.Patient;

import java.util.ArrayList;
import java.util.List;

public class HistoryHelper extends SQLiteOpenHelper {
    public HistoryHelper(Context context) {
        super(context, "db_Medical", null, 1);
    }
    private String TABLENAME = "tbl_Histories";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME + "(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "smoker INTEGER," +
                    "heartCondition INTEGER," +
                    "asthma INTEGER," +
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

    public void insert(History data){
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.insert(TABLENAME, null, prepareData(data));
            Log.i("DatabaseHelper", "Successfully inserted into the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
    }

    public List<History> get(){
        SQLiteDatabase db = getReadableDatabase();
        List<History> list = new ArrayList<>();
        try{
            Cursor cursor = db.rawQuery("SELECT ID, smoker, heartCondition, asthma FROM " + TABLENAME + " WHERE archived = 0", null);
            while (cursor.moveToNext())
                list.add(prepareData(cursor));
            Log.i("DatabaseHelper", "Successfully retrieved from the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
        return list;
    }

    public History get(int ID){
        SQLiteDatabase db = getReadableDatabase();
        History data = null;
        try{
            Cursor cursor = db.rawQuery("SELECT ID, smoker, heartCondition, asthma FROM " + TABLENAME + " WHERE ID = ?", new String[]{String.valueOf(ID)});
            while (cursor.moveToNext())
                data = prepareData(cursor);
            Log.i("DatabaseHelper", "Successfully retrieved from the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
        return data;
    }

    public void update(History data){
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.update(TABLENAME, prepareData(data), "ID = ?", new String[]{String.valueOf(data.getID())});
            Log.i("DatabaseHelper", "Successfully updated a data from the " + TABLENAME);
        }catch (SQLException e){
            Log.e("DatabaseHelper", "Unable to upgrade table " + TABLENAME);
        }
        db.close();
    }

    public void remove(History data){
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues values = prepareData(data);
            values.put("archived", 1);
            db.update(TABLENAME, values, "ID = ?", new String[]{String.valueOf(data.getID())});
            Log.i("DatabaseHelper", "Successfully updated a data with ID " + data.getID() + "from the " + TABLENAME);
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

    private ContentValues prepareData(History data){
        ContentValues content = new ContentValues();
        if(data.isSmoker())
            content.put("smoker", 1);
        else
            content.put("smoker", 0);
        if(data.hasHeartCondition())
            content.put("heartCondition", 1);
        else
            content.put("heartCondition", 0);
        if(data.hasAsthma())
            content.put("asthma", 1);
        else
            content.put("asthma", 0);
        content.put("archived", 0);
        return content;
    }
    private History prepareData(Cursor cursor){
        History data = new History();
        data.setID(cursor.getInt(0));
        if(cursor.getInt(1) == 1)
            data.setSmoker(true);
        else
            data.setSmoker(false);
        if(cursor.getInt(2) == 1)
            data.setHeartCondition(true);
        else
            data.setHeartCondition(false);
        if(cursor.getInt(3) == 1)
            data.setAsthma(true);
        else
            data.setAsthma(false);
        return data;
    }
}

