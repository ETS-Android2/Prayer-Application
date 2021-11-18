package com.example.prayertimetrackerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "Prayer_Channel";
    private GpsTracker gpsTracker;
    private PrayTime obj = new PrayTime();
    private PrayTime obj24h = new PrayTime();
    double latitude = 0;
    double longitude = 0;
    String Fajr24h;
    String Dhuhr24h;
    String Asr24h;
    String Maghrib24h;
    String Isha24h;

    int millisToGo;

    TextView Fajr;
    TextView Dhuhr;
    TextView Asr;
    TextView Maghrib;
    TextView Isha;

    int juristicIndex = 0;
    int calMethodIndex = 0;
    int angleIndex = 0;
    int timeIndex = 0;

    CountDownTimer countDownTimer;
    TextView nextPrayerText;
    TextView timer;
    String currentPrayer;
    String email;
    public  DBManager dbManager; //  use this to work with database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(this);  // this line should be in each activity
        dbManager.open(); // open database ---- this line should be in each activity

        SharedPreferences userDetails =getSharedPreferences("userdetails", MODE_PRIVATE);// to get information from shered preferences
        email = userDetails.getString("email", "");

        //-------------------------------------------------------->> ADDED by Muntaha :)
        ImageView settingIcon = findViewById(R.id.settingID);
        settingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),silentModeForSetOfTime.class);
                startActivity(intent);
            }}); // END

        GPS();
        calculatePrayerTimes();
        setTimer();
        startTimer();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void GPS(){

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                new AlertDialog.Builder(this)
                        .setTitle("تحديث")
                        .setMessage("حدث اوقات الصلوات بناءً على موقعي")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("حسنًا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(getIntent());
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            //Toast.makeText(getApplicationContext(),"latitude "+ latitude +" longitude" +longitude , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"Can't get location" , Toast.LENGTH_SHORT).show();
        }
    }


    // ----------------------------------------------------------------calculatePrayerTimes method
    public void calculatePrayerTimes(){

        Fajr = findViewById(R.id.Fajr);
        Dhuhr = findViewById(R.id.Dhuhr);
        Asr = findViewById(R.id.Asr);
        Maghrib = findViewById(R.id.Maghrib);
        Isha = findViewById(R.id.Isha);


        Calendar cal = Calendar.getInstance();

        TimeZone tz = TimeZone.getDefault();
        String gmt = TimeZone.getTimeZone(tz.getID()).getDisplayName(false,
                TimeZone.SHORT);
        String z1 = gmt.substring(4);

        String z = z1.replaceAll(":", ".");
        double zo = Double.parseDouble(z);

        getPrayersSettings();

        //Toast.makeText(getApplicationContext(),"You choose: "+ juristicIndex , Toast.LENGTH_LONG).show();

        obj.setAsrJuristic(juristicIndex);
        obj.setCalcMethod(calMethodIndex);
        obj.setAdjustHighLats(angleIndex);
        obj.setTimeFormat(timeIndex);

        obj24h.setAsrJuristic(juristicIndex);
        obj24h.setCalcMethod(calMethodIndex);
        obj24h.setAdjustHighLats(angleIndex);
        obj24h.setTimeFormat(0);

        //obj.setTimeFormat(1);// 0 -> 24 h , 1 -> AM/PM
        //obj24h.setTimeFormat(0);// 0 -> 24 h , 1 -> AM/PM

        ArrayList<String> array = obj.getPrayerTimes(cal,latitude,longitude, zo);
        ArrayList<String> array24h = obj24h.getPrayerTimes(cal,latitude,longitude, zo);

        // for the beautiful Ghada
        Fajr.setText(array.get(0));
        String Sunrise = array.get(1);
        Dhuhr.setText(array.get(2));
        Asr.setText(array.get(3));
        String Sunset = array.get(4);
        Maghrib.setText(array.get(5));
        Isha.setText(array.get(6));

        Fajr24h = array24h.get(0); //Fajr time
        //array.get(1);//Sunrise
        Dhuhr24h = array24h.get(2);//Dhuhr
        Asr24h = array24h.get(3);//Asr
        //array.get(4);//Sunset
        Maghrib24h = array24h.get(5);//Maghrib
        Isha24h = array24h.get(6);//Isha

        // For Muntaha
        dbManager.updatePrayers(Fajr24h, Dhuhr24h, Asr24h, Maghrib24h, Isha24h, email);

    }

    private void getPrayersSettings(){

        if(dbManager.getJuristic(email).isEmpty()){
            juristicIndex = 0;
        } else {
            switch (dbManager.getJuristic(email)){
                case "شافعي" : juristicIndex = 0; break;
                case "حنفي": juristicIndex = 1; break;
                default: juristicIndex = 0;
            }
        }


        if (dbManager.getCalMethods(email).isEmpty()){
            calMethodIndex = 0;
        } else {
            switch (dbManager.getCalMethods(email)){
                case "أم القرى، مكة المكرمة" : calMethodIndex = 4; break;
                case "رابطة العالم الإسلامي": calMethodIndex = 3; break;
                case "الجمعية الإسلامية لأمريكا الشمالية": calMethodIndex = 2; break;
                case "جامعة العلوم الإسلامية بكراتشي": calMethodIndex = 1; break;
                case "جعفري": calMethodIndex = 0; break;
                case "الهيئة المصرية العامة": calMethodIndex = 5; break;
                case "طهران": calMethodIndex = 6; break;
                default: calMethodIndex = 4;
            }
        }


        if (dbManager.getAngle(email).isEmpty()){
            angleIndex = 0;
        } else {
            switch (dbManager.getAngle(email)){
                case "لا يوجد تعديل": angleIndex = 0; break;
                case "منتصف الليل": angleIndex = 1; break;
                case "١/٧ من الليل": angleIndex = 2; break;
                case "زاويه  ٦٠ من الليل": angleIndex = 3; break;
                default: angleIndex = 0;
            }
        }


        if (dbManager.getTimeFormat(email).isEmpty()){
            timeIndex = 0;
        } else {
            switch (dbManager.getTimeFormat(email)){
                case "تنسيق  ٢٤ ساعة": timeIndex = 0; break;
                case "تنسيق ١٢ ساعة": timeIndex = 1;break;
                case "تنسيق ١٢ بدون صباح ومساء": timeIndex = 2; break;
                case "ارقام عشريه": timeIndex = 3; break;
                default: timeIndex = 0;
            }
        }


    }

    // this method just to test if the address right or not  ()
    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Toast.makeText(this, add, Toast.LENGTH_SHORT).show();

            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // ----------------------------------------------------------------------------setTimer method
    public void setTimer() {
        nextPrayerText = findViewById(R.id.nextPrayer);
        timer = findViewById(R.id.countdownTimer);

        //Current time
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()); //"HH:mm:ss"
        int index = currentTime.indexOf(":");
        int index2 = currentTime.indexOf(":", index+1);
        int currentHour = Integer.parseInt(currentTime.substring(0, index));
        int currentMin = Integer.parseInt(currentTime.substring(index+1, index2));
        int currentSec = Integer.parseInt(currentTime.substring(index2+1));
        //Toast.makeText(getApplicationContext(),"Current sec is: "+ currentSec , Toast.LENGTH_SHORT).show();


        int FajrHour = Integer.parseInt(Fajr24h.substring(0, index));
        int FajrMin = Integer.parseInt(Fajr24h.substring(index+1, index+3));
        //
        int DhuhrHour = Integer.parseInt(Dhuhr24h.substring(0, index));
        int DhuhrMin = Integer.parseInt(Dhuhr24h.substring(index+1, index+3));
        //
        int AsrHour = Integer.parseInt(Asr24h.substring(0, index));
        int AsrMin = Integer.parseInt(Asr24h.substring(index+1, index+3));
        //
        int MaghribHour = Integer.parseInt(Maghrib24h.substring(0, index));
        int MaghribMin = Integer.parseInt(Maghrib24h.substring(index+1, index+3));
        //
        int IshaHour = Integer.parseInt(Isha24h.substring(0, index));
        int IshaMin = Integer.parseInt(Isha24h.substring(index+1, index+3));

        int hoursToGo = 0;
        int minutesToGo = 0;
        int secondsToGo = 30;

        if (currentHour <= FajrHour){
            if (currentHour == FajrHour){
                if (currentMin >= FajrMin){
                    // ?? hours left to next prayer
                    nextPrayerText.setText("المتبقي على أذان الظهر:");
                    currentPrayer = "الفجر";
                    hoursToGo = DhuhrHour - currentHour;
                    minutesToGo = DhuhrMin - currentMin;
                    secondsToGo = 0 - currentSec;
                } else {
                    // ?? min left to this prayer
                    nextPrayerText.setText("المتبقي على أذان الفجر:");
                    currentPrayer = "العشاء";
                    hoursToGo = 0;
                    minutesToGo = FajrMin - currentMin;
                    secondsToGo = 0 - currentSec;
                }
            } else {

                //?? hours left to Fajr prayer
                nextPrayerText.setText("المتبقي على أذان الفجر:");
                currentPrayer = "العشاء";
                hoursToGo = FajrHour - currentHour;
                minutesToGo = FajrMin - currentMin;
                secondsToGo = 0 - currentSec;
            }

        }else if (currentHour <= DhuhrHour){
            if (currentHour == DhuhrHour){
                if (currentMin >= DhuhrMin){
                    // ?? hours left to next prayer
                    nextPrayerText.setText("المتبقي على أذان العصر:");
                    currentPrayer = "الظهر";
                    hoursToGo = AsrHour - currentHour;
                    minutesToGo = AsrMin - currentMin;
                    secondsToGo = 0 - currentSec;
                } else {
                    // ?? min left to this prayer
                    nextPrayerText.setText("المتبقي على أذان الظهر:");
                    currentPrayer = "الفجر";
                    hoursToGo = 0;
                    minutesToGo = DhuhrMin - currentMin;
                    secondsToGo = 0 - currentSec;
                }
            }else {

                //?? hours left to Dhuhr prayer
                nextPrayerText.setText("المتبقي على أذان الظهر:");
                currentPrayer = "الفجر";
                hoursToGo = DhuhrHour - currentHour;
                minutesToGo = DhuhrMin - currentMin;
                secondsToGo = 0 - currentSec;
            }

        }else if (currentHour <= AsrHour){
            if (currentHour == AsrHour){
                if (currentMin >= AsrMin){
                    // ?? hours left to next prayer
                    nextPrayerText.setText("المتبقي على أذان المغرب:");
                    currentPrayer = "العصر";
                    hoursToGo = MaghribHour - currentHour;
                    minutesToGo = MaghribMin - currentMin;
                    secondsToGo = 0 - currentSec;
                } else {
                    // ?? min left to this prayer
                    nextPrayerText.setText("المتبقي على أذان العصر:");
                    currentPrayer = "الظهر";
                    hoursToGo = 0;
                    minutesToGo = AsrMin - currentMin;
                    secondsToGo = 0 - currentSec;
                }

            }else{

                //?? hours left to asr prayer
                nextPrayerText.setText("المتبقي على أذان العصر:");
                currentPrayer = "الظهر";
                hoursToGo = AsrHour - currentHour;
                minutesToGo = AsrMin - currentMin;
                secondsToGo = 0 - currentSec;
            }

        }else if (currentHour <= MaghribHour){
            if (currentHour == MaghribHour){
                if (currentMin >= MaghribMin){
                    // ?? hours left to next prayer
                    nextPrayerText.setText("المتبقي على أذان العشاء:");
                    currentPrayer = "المغرب";
                    hoursToGo = IshaHour - currentHour;
                    minutesToGo = IshaMin - currentMin;
                    secondsToGo = 0 - currentSec;
                } else {
                    // ?? min left to this prayer
                    nextPrayerText.setText("المتبقي على أذان المغرب:");
                    currentPrayer = "العصر";
                    hoursToGo = 0;
                    minutesToGo = MaghribMin - currentMin;
                    secondsToGo = 0 - currentSec;
                }

            }else{

                //?? hours left to maghrib prayer
                nextPrayerText.setText("المتبقي على أذان المغرب:");
                currentPrayer = "العصر";
                hoursToGo = MaghribHour - currentHour;
                minutesToGo = MaghribMin - currentMin;
                secondsToGo = 0 - currentSec;
            }

        }else if (currentHour <= IshaHour){
            if (currentHour == IshaHour){
                if (currentMin >= IshaMin){
                    // ?? hours left to next prayer
                    nextPrayerText.setText("المتبقي على أذان الفجر:");
                    currentPrayer = "العشاء";
                    hoursToGo = (24 - currentHour) + (FajrHour);
                    minutesToGo = (currentMin) + (FajrMin);
                    secondsToGo = 0 - currentSec;
                } else {
                    // ?? min left to this prayer
                    nextPrayerText.setText("المتبقي على أذان العشاء:");
                    currentPrayer = "المغرب";
                    hoursToGo = 0;
                    minutesToGo = IshaMin - currentMin;
                    secondsToGo = 0 - currentSec;
                }

            }else{

                //?? hours left to Ishaa prayer
                nextPrayerText.setText("المتبقي على أذان العشاء:");
                currentPrayer = "المغرب";
                hoursToGo = IshaHour - currentHour;
                minutesToGo = IshaMin - currentMin;
                secondsToGo = 0 - currentSec;
            }

        } else {
            //currentHour > IshaHour AND currentHour < AjrHour
            //?? hours left to Fajr prayer
            nextPrayerText.setText("المتبقي على أذان الفجر:");
            currentPrayer = "العشاء";
            hoursToGo = (24 - currentHour) + (FajrHour);
            minutesToGo = (currentMin) + (FajrMin);
            secondsToGo = 0 - currentSec;
        }

        millisToGo = secondsToGo*1000+minutesToGo*1000*60+hoursToGo*1000*60*60;
    }

    public void startTimer(){

        if(countDownTimer!=null){
            countDownTimer.cancel();
            calculatePrayerTimes();
            setTimer();
        }

        //countdown timer
        countDownTimer = new CountDownTimer(millisToGo, 1000) {
            @Override
            public void onTick(long millis) {

                int seconds = (int) (millis / 1000) % 60 ;
                int minutes = (int) ((millis / (1000*60)) % 60);
                int hours   = (int) ((millis / (1000*60*60)) % 24);
                String text = String.format("%02d ساعة, %02d دقيقة, %02d ثانية",hours,minutes,seconds);

                timer.setText(text);

            }

            @Override
            public void onFinish() {

                //When finish
                setTimer();
                if(countDownTimer!=null){
                    countDownTimer.cancel();
                    startTimer();
                }
                Toast.makeText(getApplicationContext(), "حان الآن وقت أذان " + currentPrayer, Toast.LENGTH_LONG).show();
                createNotification(currentPrayer);
            }
        }.start();
    }

    private void createNotificationChannel() {
        /* Create the NotificationChannel, but only on API 26+ because
         the NotificationChannel class is new and not in the support library */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Prayer Notification";
            String description = "This notification is provided by Salaty Hayaty APP";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // create Notification (prayer)
    public void createNotification( final String prayer){

        createNotificationChannel();
        Intent intent1 = new Intent (MainActivity.this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, (int) (Math.random()*100) ,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Prayer_Channel")
                .setSmallIcon(R.drawable.musqu_icon)
                .setContentTitle("حان الآن وقت آذان "+ prayer)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        notificationManager.notify(200,builder.build());
    }

}