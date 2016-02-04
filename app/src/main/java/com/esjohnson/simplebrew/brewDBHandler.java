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

    public static final String[] ALL_KEY = {COLUMN_NAME,COLUMN_ID,COLUMN_GRIND,COLUMN_AMOUNT,COLUMN_BLOOM_TIME,COLUMN_STIR_TIME,COLUMN_BREW_TIME};
    public brewDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_BREW + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_GRIND + " TEXT, " +
                COLUMN_AMOUNT + " INTEGER, " +
                COLUMN_BLOOM_TIME + " INTEGER, " +
                COLUMN_STIR_TIME + " INTEGER, " +
                COLUMN_BREW_TIME + " INTEGER" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_BREW);
        onCreate(db);
    }
    //add a new row to the database
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
    //deletes product from the database
    public void deleteBrew(String brewName){
        SQLiteDatabase db  = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BREW + " WHERE " + COLUMN_NAME + "=\"" + brewName + "\";");
    }
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db  = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BREW + " WHERE 1";

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //move to the first row in your results
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("name")) != null){
                dbString += c.getString(c.getColumnIndex("name"));
                dbString += "/n";
            }
        }
        c.close();
        db.close();
        return dbString;
    }
    public brew getBrewObjById(Long id){
        SQLiteDatabase db  = getWritableDatabase();
        brew brewObj = new brew();
        String query = "SELECT * FROM " + TABLE_BREW + " WHERE " + COLUMN_ID + "='" + id + "'";
        Cursor c =  db.rawQuery(query,null);
        c.moveToFirst();
        if (c.getString(c.getColumnIndex("_id")) != null) {
            brewObj.setId(c.getLong(c.getColumnIndex("_id")));
        }
        if (c.getString(c.getColumnIndex("name")) != null) {
            brewObj.setName(c.getString(c.getColumnIndex("name")));
        }
        if (c.getString(c.getColumnIndex("grind")) != null) {
            brewObj.setGrind(c.getString(c.getColumnIndex("grind")));
        }
        if (c.getString(c.getColumnIndex("amount")) != null) {
            brewObj.setAmount(c.getInt(c.getColumnIndex("amount")));
        }
        if (c.getString(c.getColumnIndex("bloomtime")) != null) {
            brewObj.setBloomTimeMillis(c.getLong(c.getColumnIndex("bloomtime")));
        }
        if (c.getString(c.getColumnIndex("brewtime")) != null) {
            brewObj.setBrewTimeMillis(c.getLong(c.getColumnIndex("brewtime")));
        }
        if (c.getString(c.getColumnIndex("stirtime")) != null) {
            brewObj.setStirTimeMillis(c.getLong(c.getColumnIndex("stirtime")));
        }
        c.close();
        return brewObj;
    }
    //tester for cursor adapter
    public Cursor getAllSpecs(Long id){
        SQLiteDatabase db  = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BREW + " WHERE " + COLUMN_ID + "='" + id + "'";
        Cursor c =  db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }

    public Cursor getAllNames(){
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
}
