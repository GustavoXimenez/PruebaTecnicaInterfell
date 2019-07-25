package com.androidavanzado.pruebatecnicainterfell.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.androidavanzado.pruebatecnicainterfell.Models.Contraversia;

import java.util.ArrayList;
import java.util.List;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Contraversion.db";
    public static final String TABLE_NAME = "Contraversion_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "PLATE";
    public static final String COL_3 = "DATE";
    public static final String COL_4 = "HOUR";

    public AdminSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creamos la tabla contraversiones
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, PLATE TEXT, DATE TEXT, HOUR TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String plate, String date, String hour){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues registry = new ContentValues();
        registry.put(COL_2, plate);
        registry.put(COL_3, date);
        registry.put(COL_4, hour);
        long result = db.insert(TABLE_NAME, null, registry);
        if(result == -1)
            return false;
        else
            return true;
    }

    public List<Contraversia> selectData(String date, String hour){
        List<Contraversia> contraversias = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where DATE = '" + date + "' AND HOUR = '" + hour + "'", null);
        if(cursor.moveToFirst()){
            do {
                Contraversia contraversia = new Contraversia();
                contraversia.Id = cursor.getInt(0);
                contraversia.Plate = cursor.getString(1);
                contraversia.Date = cursor.getString(2);
                contraversia.Hour = cursor.getString(3);
                contraversias.add(contraversia);
            } while(cursor.moveToNext());
        }
        return contraversias;
    }

    public int countData(String plate){
        int numeroIncidencias = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where PLATE = '" + plate + "'", null);
        if(cursor.moveToFirst()){
            do {
                numeroIncidencias++;
            } while(cursor.moveToNext());
            numeroIncidencias++;
        }
        return numeroIncidencias;
    }

    public int countTotalData(){
        int numeroIncidencias = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME , null);
        if(cursor.moveToFirst()){
            do {
                numeroIncidencias++;
            } while(cursor.moveToNext());
            numeroIncidencias++;
        }
        return numeroIncidencias;
    }
}
