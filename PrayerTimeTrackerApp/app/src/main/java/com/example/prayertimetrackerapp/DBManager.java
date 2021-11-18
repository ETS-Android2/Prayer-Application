package com.example.prayertimetrackerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    } // close connection

    public boolean insertNewUser(String name, String email , String pass , String silent) { // insert new reminder
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.name, name);
        contentValue.put(DatabaseHelper.email, email);
        contentValue.put(DatabaseHelper.password, pass);
       // contentValue.put(DatabaseHelper.silent, silent);

        contentValue.put(DatabaseHelper.Fajr, "null");
        contentValue.put(DatabaseHelper.Dhuhr, "null");
        contentValue.put(DatabaseHelper.Asr, "null");
        contentValue.put(DatabaseHelper.Maghrib, "null");
        contentValue.put(DatabaseHelper.Ishaa, "null");

        contentValue.put(DatabaseHelper.FajrDuration, 40);
        contentValue.put(DatabaseHelper.DhuhrDuration, 40);
        contentValue.put(DatabaseHelper.AsrDuration, 40);
        contentValue.put(DatabaseHelper.MaghribDuration, 40);
        contentValue.put(DatabaseHelper.IshaaDuration, 40);


        contentValue.put(DatabaseHelper.FajrSilent, 0);
        contentValue.put(DatabaseHelper.DhuhrSilent, 0);
        contentValue.put(DatabaseHelper.AsrSilent, 0);
        contentValue.put(DatabaseHelper.MaghribSilent, 0);
        contentValue.put(DatabaseHelper.IshaaSilent, 0);

        contentValue.put(DatabaseHelper.Juristic, "شافعي");
        contentValue.put(DatabaseHelper.CalMethods, "أم القرى، مكة المكرمة");
        contentValue.put(DatabaseHelper.angel, "لا يوجد تعديل");
        contentValue.put(DatabaseHelper.timeFormat, "تنسيق  ٢٤ ساعة");

        Long g =  database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
       if(g == -1 )return false ;
       return true ;
    }

    public int updateUser(String name, String email , String pass , String silent) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.name, name);
        contentValues.put(DatabaseHelper.email, email);
        contentValues.put(DatabaseHelper.password, pass);
      //  contentValues.put(DatabaseHelper.silent, silent);

        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }


    //--------------------- update and get ---------------------------------------------------------
    public int updatePrayers(String Fajr, String Dhuhr, String Asr, String Maghrib, String Ishaa,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.Fajr, Fajr);
        contentValues.put(DatabaseHelper.Dhuhr, Dhuhr);
        contentValues.put(DatabaseHelper.Asr, Asr);
        contentValues.put(DatabaseHelper.Maghrib, Maghrib);
        contentValues.put(DatabaseHelper.Ishaa, Ishaa);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;

    }
    public  int updateFajr(String Fajr,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.Fajr), Fajr);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public  int updateDhuhr(String Dhuhr,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.Dhuhr), Dhuhr);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public  int updateAsr(String Asr,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.Asr), Asr);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public  int updateMaghrib(String Maghrib,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.Maghrib), Maghrib);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public  int updateIshaa(String Ishaa,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.Ishaa), Ishaa);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }

    public String getFajr(String email){
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        String result = cursor.getString(cursor.getColumnIndex("Fajr"));
        return result ;
    }
    public String getDhuhr(String email){
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        String result = cursor.getString(cursor.getColumnIndex("Dhuhr"));
        return result ;
    }
    public String getAsr(String email){
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        String result = cursor.getString(cursor.getColumnIndex("Asr"));
        return result ;
    }
    public String getMaghrib(String email){
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        String result = cursor.getString(cursor.getColumnIndex("Maghrib"));
        return result ;
    }
    public String getIshaa(String email){
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        String result = cursor.getString(cursor.getColumnIndex("Ishaa"));
        return result ;
    }

    public int updateSilence(int Fajr, int Dhuhr, int Asr, int Maghrib, int Ishaa,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.FajrSilent), Fajr);
        contentValues.put(String.valueOf(DatabaseHelper.DhuhrSilent), Dhuhr);
        contentValues.put(String.valueOf(DatabaseHelper.AsrSilent), Asr);
        contentValues.put(String.valueOf(DatabaseHelper.MaghribSilent), Maghrib);
        contentValues.put(String.valueOf(DatabaseHelper.IshaaSilent), Ishaa);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = " + email, null);
        return i;
    }
   public int updateFajrSilent(int Fajr,String email){
    ContentValues contentValues = new ContentValues();
    contentValues.put(String.valueOf(DatabaseHelper.FajrSilent), Fajr);
       int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
    return i;
}
    public int updateDhuhrSilent(int Dhuhr,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.DhuhrSilent), Dhuhr);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public int updateAsrSilent(int Asr,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.AsrSilent), Asr);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public int updateIshaaSilent(int IshaaSilent,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.IshaaSilent), IshaaSilent);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public int updateMaghribSilent(int Maghrib,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.MaghribSilent), Maghrib);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public int getFajrSilent(String email){database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("FajrSilent"));
        return result ; }
    public int getDhuhrSilent(String email){
        database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("DhuhrSilent"));
        return result ;
    }
    public int getAsrSilent(String email){
        database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("AsrSilent"));
        return result ;
        }
    public int getMaghribSilent(String email){
        database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("MaghribSilent"));
        return result ;
    }
    public int getIshaaSilent(String email){
        database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("IshaaSilent"));
        return result ;
    }

    public int updateJuristic (String Juristic,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.Juristic, Juristic);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public int updateCalMethods (String CalMethods,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CalMethods, CalMethods);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public int updateAngle (String angle,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.angel, angle);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public int updateTimeFormat (String timeFormat,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.timeFormat, timeFormat);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }

    public String getJuristic(String email) {
        database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        String result = cursor.getString(cursor.getColumnIndex("Juristic"));
        return result ;
    }
    public String getCalMethods(String email) {
        database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        String result = cursor.getString(cursor.getColumnIndex("CalMethods"));
        return result ;
    }
    public String getAngle(String email) {
        database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        String result = cursor.getString(cursor.getColumnIndex("angel"));
        return result ;
    }
    public String getTimeFormat(String email) {
        database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        String result = cursor.getString(cursor.getColumnIndex("timeFormat"));
        return result ;
    }
    public int updateFajrDuration(int FajrDuration,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.FajrDuration), FajrDuration);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public int updateDhuhrDuration(int DhuhrDuration,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.DhuhrDuration), DhuhrDuration);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public int updateAsrDuration(int AsrDuration,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.AsrDuration), AsrDuration);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public int updateMaghribDuration(int MaghribDuration,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.MaghribDuration), MaghribDuration);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public int updateIshaaDuration(int IshaaDuration,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DatabaseHelper.IshaaDuration), IshaaDuration);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.email + " = '" + email+"'", null);
        return i;
    }
    public int getIshaaDuration(String email){database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("IshaaDuration"));
        return result ; }
    public int getMaghribDuration(String email){database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("MaghribDuration"));
        return result ; }
    public int getAsrDuration(String email){database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("AsrDuration"));
        return result ; }
    public int getDhuhrDuration(String email){database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("DhuhrDuration"));
        return result ; }
    public int getFajrDuration(String email){database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("FajrDuration"));
        return result ; }
    //----------------------------------------------------------------------------------------------

    public void deleteUser(int email) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.email + "=" + email, null);
    }
    public int count(Context context)
    {
        dbHelper = new DatabaseHelper(context);
        dbHelper.onOpen(database);
        int num =0;

        String query = "select count(*) from "+DatabaseHelper.TABLE_NAME;
        database = dbHelper.getWritableDatabase();
        Cursor c = database.rawQuery(query, null);
       // num =c.getCount();
        c.moveToFirst();
        num = c.getInt(0);
        return num ;
    }
    public Cursor getAllUser(){
        database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME,null);
           return cursor ;

    }
    public Cursor getUserByEmail(int email ){
        database = dbHelper.getWritableDatabase();
        Cursor  cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME+" WHERE email = '"+email+"'",null);
        return cursor ;

    }

}
