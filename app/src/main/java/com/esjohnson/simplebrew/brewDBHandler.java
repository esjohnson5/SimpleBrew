package com.esjohnson.simplebrew;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by esjoh on 1/29/2016.
 * database handler for brewDB
 */
public class brewDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "brewDB.db";
    public static final String TABLE_BREW = "brew";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GRIND = "grind";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_BLOOM_TIME = "bloomtime";
    public static final String COLUMN_BREW_TIME = "brewtime";
    public static final String COLUMN_STIR_TIME = "stirtime";
    //apparatus database materials
    public static final String TABLE_APPARATUS = "apparatus";
    public static final String COLUMN_STYLE = "style";

    public brewDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryBrew = "CREATE TABLE " + TABLE_BREW + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_GRIND + " TEXT, " +
                COLUMN_AMOUNT + " INTEGER, " +
                COLUMN_BLOOM_TIME + " INTEGER, " +
                COLUMN_STIR_TIME + " INTEGER, " +
                COLUMN_BREW_TIME + " INTEGER" +
                ");";
        db.execSQL(queryBrew);

        String queryApparatus = "CREATE TABLE " + TABLE_APPARATUS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_STYLE + " TEXT, " +
                ");";
        db.execSQL(queryApparatus);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_BREW);
        onCreate(db);
        //should include table name in the constructor
    }
    //add a new brew row to the DB
    public void addBrew(brew brew){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, brew.getName());
        values.put(COLUMN_GRIND, brew.getGrind());
        values.put(COLUMN_AMOUNT, brew.getAmount());
        values.put(COLUMN_BLOOM_TIME, brew.getBloomTime());
        values.put(COLUMN_STIR_TIME, brew.getStirTime());
        values.put(COLUMN_BREW_TIME, brew.getBrewTime());
        SQLiteDatabase db  = getWritableDatabase();
        db.insert(TABLE_BREW, null, values);
        db.close();
    }
    //adds new apparatus row to DB
    public void addApparatus(brewApparatus apparatus){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, apparatus.getName());
        values.put(COLUMN_STYLE, apparatus.getName());
        SQLiteDatabase db  = getWritableDatabase();
        db.insert(TABLE_APPARATUS, null, values);
        db.close();
    }

    //deletes product from the database
    public void deleteBrewById(int _id){
        SQLiteDatabase db  = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BREW + " WHERE " + COLUMN_ID + "=\"" + _id + "\";");
    }

    public void deleteApparatusById(int _id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_APPARATUS + " WHERE " + COLUMN_ID + "=\"" + _id + "\";");
    }

    //tester for cursor adapter
    public Cursor getBrewRowById(Long id){
        SQLiteDatabase db  = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BREW + " WHERE " + COLUMN_ID + "='" + id + "'";
        Cursor c =  db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }
    public Cursor getApparatusRowById(Long id){
        SQLiteDatabase db  = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_APPARATUS + " WHERE " + COLUMN_ID + "='" + id + "'";
        Cursor c =  db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }

    public Cursor getAllBrewNames(){
        SQLiteDatabase db  = getWritableDatabase();
        return db.query(TABLE_BREW, new String[] {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_GRIND,
                COLUMN_AMOUNT,
                COLUMN_BLOOM_TIME,
                COLUMN_STIR_TIME,
                COLUMN_BREW_TIME},
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor getAllApparatusNames(){
        SQLiteDatabase db  = getWritableDatabase();
        return db.query(TABLE_BREW, new String[] {
                        COLUMN_ID,
                        COLUMN_NAME,
                        COLUMN_STYLE},
                null,
                null,
                null,
                null,
                null);
    }
}
