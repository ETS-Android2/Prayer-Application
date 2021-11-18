package com.example.prayertimetrackerapp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "UserInfo";

    // Table columns
    public static final String name = "name";
    public static final String email = "email";
    public static final String password = "password";
    public static final String silent = "silent";
    public static final String Fajr = "Fajr";
    public static final String Dhuhr = "Dhuhr";
    public static final String Asr = "Asr";
    public static final String Maghrib = "Maghrib";
    public static final String Ishaa = "Ishaa";
    public static final String FajrSilent = "FajrSilent";
    public static final String DhuhrSilent = "DhuhrSilent";
    public static final String AsrSilent = "AsrSilent";
    public static final String MaghribSilent = "MaghribSilent";
    public static final String IshaaSilent = "IshaaSilent";
    public static final String Juristic = "Juristic";
    public static final String CalMethods = "CalMethods";
    public static final String angel = "angel";
    public static final String timeFormat = "timeFormat";

    public static final String FajrDuration = "FajrDuration";
    public static final String DhuhrDuration = "DhuhrDuration";
    public static final String AsrDuration = "AsrDuration";
    public static final String MaghribDuration = "MaghribDuration";
    public static final String IshaaDuration = "IshaaDuration";

    // Database Information
    static final String DB_NAME = "PrayerTime.db";

    // database version
    static final int DB_VERSION = 8;

    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +name+" TEXT NOT NULL ," + email + " TEXT PRIMARY KEY, " + password + " TEXT NOT NULL, "
            + Fajr + " TEXT, " + Dhuhr + " TEXT, " + Asr + " TEXT, " + Maghrib + " TEXT, " + Ishaa + " TEXT, "
            + FajrDuration + " INTEGER, " + DhuhrDuration + " INTEGER, " + AsrDuration + " INTEGER, " + MaghribDuration + " INTEGER, " + IshaaDuration + " INTEGER, "
            + FajrSilent + " INTEGER, " + DhuhrSilent + " INTEGER, " + AsrSilent + " INTEGER, " + MaghribSilent + " INTEGER, " + IshaaSilent + " INTEGER, "
            + Juristic + " TEXT, " + CalMethods + " TEXT, " + angel + " TEXT, " + timeFormat + " TEXT);";
            //+ silent + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);


    }
}
