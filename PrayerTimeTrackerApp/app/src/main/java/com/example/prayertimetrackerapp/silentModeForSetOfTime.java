package com.example.prayertimetrackerapp;

// final update muntaha

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class silentModeForSetOfTime extends AppCompatActivity {
    // default values to avoid Exceptions
    int fajerEndDuratio = 40;
    int duharEndDuratio = 40;
    int asureEndDuratio = 40;
    int magrebEndDuratio = 40;
    int eishaEndDuratio = 40;

    int startFajerTimeHours = 4;
    int startDuharTimeHours = 11;
    int startAsureTimeHours = 15;
    int startMagrebTimeHours = 18;
    int starteishaTimeHours = 19;

    int startFajerTimeMinuts = 55 ;
    int startDuharTimeMinuts = 55;
    int startAsureTimeMinuts = 23;
    int startMagrebTimeMinuts = 13;
    int starteishaTimeMinuts = 43;

    TextInputEditText fajerDInput;
    TextInputEditText duharDInput;
    TextInputEditText asureDInput;
    TextInputEditText magrebDInput;
    TextInputEditText eishaDInput;
    TextView errorMessage;

    int fajerEndHour = startFajerTimeHours;
    int duharEndHour = startDuharTimeHours;
    int asureEndDHour = startAsureTimeHours;
    int magrebEndHour = startMagrebTimeHours;
    int eishaEndHour = starteishaTimeHours;

    private PrayTime prayTimeObj = new PrayTime();

    Spinner JuristicSpinner;
    Spinner calMethodSpinner;
    Spinner angleSpinner;
    Spinner timeSpinner;

    String juristicVal;
    String calMethodVal;
    String angleVal;
    String timeVal;

    String currentJuristic = "";
    String currentCalMethod = "";
    String currentAngle = "";
    String currentTime = "";

    int juristicIndex = 0;
    int calMethodIndex = 0;
    int angleIndex = 0;
    int timeIndex = 0;

    String email;
    SharedPreferences userDetails;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    public DBManager dbManager; //  use this to work with database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dbManager = new DBManager(this);  // this line should be in each activity
        dbManager.open(); // open database ---- this line should be in each activity
        userDetails =getSharedPreferences("userdetails", MODE_PRIVATE);// to get information from shared preferences
        email = userDetails.getString("email", "");

        setLists();

        Switch swichButtonFajer = (Switch) findViewById(R.id.switchID);
        Switch swichButtonDuhar = (Switch) findViewById(R.id.switchID2);
        Switch swichButtonAsur = (Switch) findViewById(R.id.switch4);
        Switch swichButtonMagreb = (Switch) findViewById(R.id.switch5);
        Switch swichButtonEisha = (Switch) findViewById(R.id.switch6);
        Button changeSilencDuration = findViewById(R.id.buttonSilenc);
        Button logoutButton = findViewById(R.id.logoutButton);

        fajerDInput = findViewById(R.id.fajerInputD);
        duharDInput = findViewById(R.id.duharInputD);
        asureDInput = findViewById(R.id.asureInputD);
        magrebDInput = findViewById(R.id.magrebInputD);
        eishaDInput = findViewById(R.id.eishInputD);
        errorMessage = findViewById(R.id.errorM);


        // To Split Time TO Hours And Min
        if(dbManager.getFajr(email).equals("null")  || dbManager.getDhuhr(email).equals("null") || dbManager.getAsr(email).equals("null")
                || dbManager.getMaghrib(email).equals("null")  || dbManager.getIshaa(email).equals("null") ){
            // do nothing
            Toast.makeText(getApplicationContext(),"inside if null --> database has null data",Toast.LENGTH_SHORT).show();

        } else {
            splitData(dbManager.getFajr(email) , "fajer");
            splitData(dbManager.getDhuhr(email) , "duhar");
            splitData(dbManager.getAsr(email), "asure");
            splitData(dbManager.getMaghrib(email),"magreb");
            splitData(dbManager.getIshaa(email),"eisha"); }

      // Turn Switch on
        if(dbManager.getFajrSilent(email) == 1){swichButtonFajer.setChecked(true);
            //onSilentMode(0, startFajerTimeHours, startFajerTimeMinuts, 0, fajerEndHour, fajerEndDuratio+startFajerTimeMinuts, 0);
        }
        if( dbManager.getDhuhrSilent(email) == 1){swichButtonDuhar.setChecked(true);
           // onSilentMode(1,startDuharTimeHours,startDuharTimeMinuts,0,duharEndHour,duharEndDuratio+startDuharTimeMinuts,0);
        }
        if(dbManager.getAsrSilent(email) == 1){swichButtonAsur.setChecked(true);
           // onSilentMode(2,startAsureTimeHours,startAsureTimeMinuts,0,asureEndDHour,asureEndDuratio+startAsureTimeMinuts,0);
        }
        if( dbManager.getMaghribSilent(email) == 1){swichButtonMagreb.setChecked(true);
           // onSilentMode(3,startMagrebTimeHours,startMagrebTimeMinuts,0,magrebEndHour,magrebEndDuratio+startMagrebTimeMinuts,0);
        }
        if( dbManager.getIshaaSilent(email) == 1){swichButtonEisha.setChecked(true);
            //onSilentMode(4,starteishaTimeHours,starteishaTimeMinuts,0,eishaEndHour,eishaEndDuratio+starteishaTimeMinuts,0);
        }



        //Boolean swichButtonState = swichButton.isChecked();
        //swichButton.setText("");
        //swichButton.setChecked(true);

//--------------------------------------------------------------------------------ASK PERMISIION TO ACCESS MOBILE SITIINGS
        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !nm.isNotificationPolicyAccessGranted()) {
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)); }

        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        //AudioManager audioManagerDuher = (AudioManager) getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
//--------------------------------------------------------------------------------- EDIT DURATION BUTTON AND LOGOUT BUTTON
        changeSilencDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInformation(); // Duration Input >> ADDED
                saveButtonClicked(); // Save prayers settings
                Intent intent = new Intent(getApplicationContext(),MainActivity.class); // مدري اذا مفروض بطريقه ثانيه
                startActivity(intent);
            }});

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logout
                SharedPreferences.Editor edit = userDetails.edit();
                edit.clear();
                Toast.makeText(getApplicationContext(),"تم تسجيل الخروج بنجاح",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(), LogIn.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

//---------------------------------------------------------------------------------- SWITCH FOR FAJER ALARM WILL REAPET DAILY
        swichButtonFajer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){ // turn on silent mode
                   //>>>> audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    // (ORDER) Uniqe ID - Start Hours - Start Minuts - Start seconds - End Hours - End Minuts - End Seconds - Text appear in Toast
                      onSilentMode(0, startFajerTimeHours, startFajerTimeMinuts, 0, fajerEndHour, fajerEndDuratio+startFajerTimeMinuts, 0);
                    dbManager.updateFajrSilent(1, email);
                } else { // KILL THE ALARM
                    AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), SilenceBroadcastReceiver.class);
                    PendingIntent midnightPI = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                    am.cancel(midnightPI);
                    Intent intent2 = new Intent(getApplicationContext(), UnsilenceBroadcastReceiver.class);
                    PendingIntent sixPI = PendingIntent.getBroadcast(getApplicationContext(), 0, intent2, 0);
                    am.cancel(sixPI);
                    Toast.makeText(getApplicationContext(),"تم الغاء تفعيل وضع الصامت لصلاة الفجر",Toast.LENGTH_SHORT).show();
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    dbManager.updateFajrSilent(0,email);


                }}
        }); // end of setOnCheckedChangeListener for swich
//------------------------------------------------------------------------------------- SWITCH FOR DHUAR ALARM WILL REAPET DAILY
        swichButtonDuhar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){ // turn on silent mode
                    //>>>> audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    onSilentMode(1,startDuharTimeHours,startDuharTimeMinuts,0,duharEndHour,duharEndDuratio+startDuharTimeMinuts,0);
                    dbManager.updateDhuhrSilent(1,email);

                } else {
                    AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), SilenceBroadcastReceiver.class);
                    PendingIntent midnightPI = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
                    am.cancel(midnightPI);
                    Intent intent2 = new Intent(getApplicationContext(), UnsilenceBroadcastReceiver.class);
                    PendingIntent sixPI = PendingIntent.getBroadcast(getApplicationContext(), 1, intent2, 0);
                    am.cancel(sixPI);
                    Toast.makeText(getApplicationContext(),"تم الغاء تفعيل وضع الصامت لصلاة الظهر",Toast.LENGTH_SHORT).show();
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    dbManager.updateDhuhrSilent(0,email);
                }}
        }); // end of setOnCheckedChangeListener for swich

//------------------------------------------------------------------------------------- SWITCH FOR AUSER ALARM WILL REAPET DAILY
        swichButtonAsur.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){ // turn on silent mode
                    //>>>> audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    onSilentMode(2,startAsureTimeHours,startAsureTimeMinuts,0,asureEndDHour,asureEndDuratio+startAsureTimeMinuts,0);
                    dbManager.updateAsrSilent(1,email);

                } else {
                    AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), SilenceBroadcastReceiver.class);
                    PendingIntent midnightPI = PendingIntent.getBroadcast(getApplicationContext(), 2, intent, 0);
                    am.cancel(midnightPI);
                    Intent intent2 = new Intent(getApplicationContext(), UnsilenceBroadcastReceiver.class);
                    PendingIntent sixPI = PendingIntent.getBroadcast(getApplicationContext(), 2, intent2, 0);
                    am.cancel(sixPI);
                    Toast.makeText(getApplicationContext(),"تم الغاء تفعيل وضع الصامت لصلاة العصر",Toast.LENGTH_SHORT).show();
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    dbManager.updateAsrSilent(0,email);

                }}
        }); // end of setOnCheckedChangeListener for swich

//------------------------------------------------------------------------------------- SWITCH FOR MAGREB ALARM WILL REAPET DAILY
        swichButtonMagreb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){ // turn on silent mode
                    //>>>> audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    onSilentMode(3,startMagrebTimeHours,startMagrebTimeMinuts,0,magrebEndHour,magrebEndDuratio+startMagrebTimeMinuts,0);
                    dbManager.updateMaghribSilent(1,email);

                } else {
                    AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), SilenceBroadcastReceiver.class);
                    PendingIntent midnightPI = PendingIntent.getBroadcast(getApplicationContext(), 3, intent, 0);
                    am.cancel(midnightPI);
                    Intent intent2 = new Intent(getApplicationContext(), UnsilenceBroadcastReceiver.class);
                    PendingIntent sixPI = PendingIntent.getBroadcast(getApplicationContext(), 3, intent2, 0);
                    am.cancel(sixPI);
                    Toast.makeText(getApplicationContext(),"تم الغاء تفعيل وضع الصامت لصلاة المغرب",Toast.LENGTH_SHORT).show();
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    dbManager.updateMaghribSilent(0,email);

                }}
        }); // end of setOnCheckedChangeListener for swich

//------------------------------------------------------------------------------------- SWITCH FOR ISHA ALARM WILL REAPET DAILY
        swichButtonEisha.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){ // turn on silent mode
                    //>>>> audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    onSilentMode(4,starteishaTimeHours,starteishaTimeMinuts,0,eishaEndHour,eishaEndDuratio+starteishaTimeMinuts,0);
                    dbManager.updateIshaaSilent(1,email);

                } else {
                    AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), SilenceBroadcastReceiver.class);
                    PendingIntent midnightPI = PendingIntent.getBroadcast(getApplicationContext(), 4, intent, 0);
                    am.cancel(midnightPI);
                    Intent intent2 = new Intent(getApplicationContext(), UnsilenceBroadcastReceiver.class);
                    PendingIntent sixPI = PendingIntent.getBroadcast(getApplicationContext(), 4, intent2, 0);
                    am.cancel(sixPI);
                    Toast.makeText(getApplicationContext(),"تم الغاء تفعيل وضع الصامت لصلاة العشاء",Toast.LENGTH_SHORT).show();
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    dbManager.updateIshaaSilent(0,email);
                }}
        }); // end of setOnCheckedChangeListener for swich


    } // end of oncreate()

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


//------------------------------------------------------------------------------- METHOD CREAT ALARM WITH START AND EAND TIME
    private void onSilentMode(Integer unID , Integer sHours, Integer sMinuts, Integer sSeconds, Integer eHours, Integer eMinuts ,Integer eSeconds){
        AlarmManager mAlarmManger = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), SilenceBroadcastReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), unID, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, sHours);
        calendar.set(Calendar.MINUTE, sMinuts);
        calendar.set(Calendar.SECOND, sSeconds);

        mAlarmManger.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);


        Intent intent2 = new Intent(getApplicationContext(), UnsilenceBroadcastReceiver.class);
        PendingIntent sixPI = PendingIntent.getBroadcast(getApplicationContext(), unID, intent2, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.HOUR_OF_DAY, eHours);
        endCalendar.set(Calendar.MINUTE, eMinuts);
        endCalendar.set(Calendar.SECOND, eSeconds);
        mAlarmManger.setInexactRepeating(AlarmManager.RTC_WAKEUP, endCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sixPI);
        //Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }// END



    private void offSilentMode(Integer unID, String text){
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), SilenceBroadcastReceiver.class);
        PendingIntent midnightPI = PendingIntent.getBroadcast(getApplicationContext(), unID, intent, 0);
        am.cancel(midnightPI);

        Intent intent2 = new Intent(getApplicationContext(), UnsilenceBroadcastReceiver.class);
        PendingIntent sixPI = PendingIntent.getBroadcast(getApplicationContext(), unID, intent2, 0);
        am.cancel(sixPI);
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
      //  audioManagerDuher.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }//END




 private void splitData(String timeOFPray, String pray ){ // SPLIT TIME TO HOURS AND MINUTS
        if(pray.equals("fajer")){
            String[] f = timeOFPray.split(":");
            startFajerTimeHours = Integer.parseInt(f[0]);
            startFajerTimeMinuts = Integer.parseInt(f[1]); }

     if(pray.equals("duhar")){
         String[] d = timeOFPray.split(":");
         startDuharTimeHours = Integer.parseInt(d[0]);
         startDuharTimeMinuts = Integer.parseInt(d[1]);}

     if(pray.equals("asure")){
         String[] a = timeOFPray.split(":");
         startAsureTimeHours = Integer.parseInt(a[0]);
         startAsureTimeMinuts = Integer.parseInt(a[1]);}

     if(pray.equals("magreb")){
         String[] m = timeOFPray.split(":");
         startMagrebTimeHours = Integer.parseInt(m[0]);
         startMagrebTimeMinuts = Integer.parseInt(m[1]);}

     if(pray.equals("eisha")){
         String[] e = timeOFPray.split(":");
         starteishaTimeHours= Integer.parseInt(e[0]);
         starteishaTimeMinuts = Integer.parseInt(e[1]);}


      fajerEndHour = startFajerTimeHours;
      duharEndHour = startDuharTimeHours;
      asureEndDHour = startAsureTimeHours;
      magrebEndHour = startMagrebTimeHours;
      eishaEndHour = starteishaTimeHours;

 }// end

    private void setLists(){

         JuristicSpinner = findViewById(R.id.JuristicSpin);
         calMethodSpinner = findViewById(R.id.calMethodSpin);
         angleSpinner = findViewById(R.id.angleSpin);
         timeSpinner = findViewById(R.id.timeSpin);

        //[1] : "حنفي" , [0] = "شافعي"
        String[] JuristicList = new String[]{"شافعي", "حنفي"};
        String[] calMethodList = new String[]{"جعفري", "جامعة العلوم الإسلامية بكراتشي", "الجمعية الإسلامية لأمريكا الشمالية" , "رابطة العالم الإسلامي" , "أم القرى، مكة المكرمة" , "الهيئة المصرية العامة", "طهران"};
        //String[] calMethodList = new String[]{"أم القرى، مكة المكرمة","رابطة العالم الإسلامي" , "الجمعية الإسلامية لأمريكا الشمالية" , "جامعة العلوم الإسلامية بكراتشي" , "جعفري", "الهيئة المصرية العامة", "طهران"};
        String[] angleList = new String[]{"لا يوجد تعديل", "منتصف الليل", "١/٧ من الليل", "زاويه  ٦٠ من الليل"};
        String[] timeList = new String[]{"تنسيق  ٢٤ ساعة", "تنسيق ١٢ ساعة", "تنسيق ١٢ بدون صباح ومساء", "ارقام عشريه"};

        sortArray();

        ArrayAdapter<String> JuristicAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, JuristicList);
        JuristicSpinner.setAdapter(JuristicAdapter);
        JuristicSpinner.setSelection(juristicIndex);

        ArrayAdapter<String> calMethodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, calMethodList);
        calMethodSpinner.setAdapter(calMethodAdapter);
        calMethodSpinner.setSelection(calMethodIndex);

        ArrayAdapter<String> angleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, angleList);
        angleSpinner.setAdapter(angleAdapter);
        angleSpinner.setSelection(angleIndex);

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeList);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setSelection(timeIndex);
    }

    private void sortArray(){

        if (dbManager.getJuristic(email).isEmpty()){
            currentJuristic = "شافعي";
        } else { currentJuristic = dbManager.getJuristic(email); }

        if (dbManager.getCalMethods(email).isEmpty()){
            currentCalMethod = "جعفري";
        } else { currentCalMethod = dbManager.getCalMethods(email); }

        if (dbManager.getAngle(email).isEmpty()){
            currentAngle = "لا يوجد تعديل";
        } else { currentAngle = dbManager.getAngle(email); }

        if (dbManager.getTimeFormat(email).isEmpty()){
            currentTime = "تنسيق  ٢٤ ساعة";
        } else { currentTime = dbManager.getTimeFormat(email); }

        switch (currentJuristic){
            case "شافعي" : juristicIndex = 0; break;
            case "حنفي": juristicIndex = 1; break;
            default: juristicIndex = 0;
        }


        /*if (currentJuristic.equals("شافعي"))
            juristicIndex = 0;
        else
            juristicIndex = 1;*/

        switch (currentCalMethod){
            case "أم القرى، مكة المكرمة" : calMethodIndex = 4; break;
            case "رابطة العالم الإسلامي": calMethodIndex = 3; break;
            case "الجمعية الإسلامية لأمريكا الشمالية": calMethodIndex = 2; break;
            case "جامعة العلوم الإسلامية بكراتشي": calMethodIndex = 1; break;
            case "جعفري": calMethodIndex = 0; break;
            case "الهيئة المصرية العامة": calMethodIndex = 5; break;
            case "طهران": calMethodIndex = 6; break;
            default: calMethodIndex = 4;
        }

        switch (currentAngle){
            case "لا يوجد تعديل": angleIndex = 0; break;
            case "منتصف الليل": angleIndex = 1; break;
            case "١/٧ من الليل": angleIndex = 2; break;
            case "زاويه  ٦٠ من الليل": angleIndex = 3; break;
            default: angleIndex = 0;
        }

        switch (currentTime){
            case "تنسيق  ٢٤ ساعة": timeIndex = 0; break;
            case "تنسيق ١٢ ساعة": timeIndex = 1;break;
            case "تنسيق ١٢ بدون صباح ومساء": timeIndex = 2; break;
            case "ارقام عشريه": timeIndex = 3; break;
            default: timeIndex = 0;
        }
    }

    private void saveButtonClicked(){

        juristicVal = JuristicSpinner.getSelectedItem().toString();
        calMethodVal = calMethodSpinner.getSelectedItem().toString();
        angleVal = angleSpinner.getSelectedItem().toString();
        timeVal = timeSpinner.getSelectedItem().toString();

        dbManager.updateJuristic(juristicVal, email);
        dbManager.updateCalMethods(calMethodVal, email);
        dbManager.updateAngle(angleVal, email);
        dbManager.updateTimeFormat(timeVal, email);

        prayTimeObj.setAsrJuristic(juristicIndex);
        prayTimeObj.setCalcMethod(calMethodIndex);
        prayTimeObj.setAdjustHighLats(angleIndex);
        prayTimeObj.setTimeFormat(timeIndex);
    }

    private void sendInformation(){

        try {

            if(fajerDInput.getText().toString().equals("")){
                //
            }else {
                fajerEndDuratio = Integer.parseInt(fajerDInput.getText().toString().replace(" ", ""));

            }
            if(duharDInput.getText().toString().equals("")){
              //  duharEndDuratio = 40;
            }else {
                duharEndDuratio = Integer.parseInt(duharDInput.getText().toString().replace(" ", ""));

            }
            if(magrebDInput.getText().toString().equals("")){
               // magrebEndDuratio = 40;
            }else {
                magrebEndDuratio = Integer.parseInt(magrebDInput.getText().toString().replace(" ", ""));

            }
            if(asureDInput.getText().toString().equals("")){
               // asureEndDuratio = 40;
            } else {
                asureEndDuratio = Integer.parseInt(asureDInput.getText().toString().replace(" ", ""));

            }
            if(eishaDInput.getText().toString().equals("")){
             //   eishaEndDuratio = 40;

            }else {
                eishaEndDuratio = Integer.parseInt(eishaDInput.getText().toString().replace(" ", "")); }

        }catch (Exception e){

            errorMessage.setText("الرجاء ادخال ارقام فقط في حقول مدة وضع الصامت");
            return;
        }

        errorMessage.setText("");
        dbManager.updateFajrDuration(fajerEndDuratio,email);
        dbManager.updateDhuhrDuration(duharEndDuratio,email);
        dbManager.updateAsrDuration(asureEndDuratio,email);
        dbManager.updateMaghribDuration(magrebEndDuratio,email);
        dbManager.updateIshaaDuration(eishaEndDuratio,email);

        if(dbManager.getFajrSilent(email) == 1){
            onSilentMode(0, startFajerTimeHours, startFajerTimeMinuts, 0, fajerEndHour, fajerEndDuratio+startFajerTimeMinuts, 0);
        }
        if( dbManager.getDhuhrSilent(email) == 1){
            onSilentMode(1,startDuharTimeHours,startDuharTimeMinuts,0,duharEndHour,duharEndDuratio+startDuharTimeMinuts,0);
        }
        if(dbManager.getAsrSilent(email) == 1){
            onSilentMode(2,startAsureTimeHours,startAsureTimeMinuts,0,asureEndDHour,asureEndDuratio+startAsureTimeMinuts,0);
        }
        if( dbManager.getMaghribSilent(email) == 1){
            onSilentMode(3,startMagrebTimeHours,startMagrebTimeMinuts,0,magrebEndHour,magrebEndDuratio+startMagrebTimeMinuts,0);
            //Toast.makeText(getApplicationContext()," inside if magreb == 1",Toast.LENGTH_SHORT).show();

        }
        if( dbManager.getIshaaSilent(email) == 1){
            onSilentMode(4,starteishaTimeHours,starteishaTimeMinuts,0,eishaEndHour,eishaEndDuratio+starteishaTimeMinuts,0);
        }

        //TEST >>> remove
//                Toast.makeText(getApplicationContext(),fajerEndDuratio +" time fajer class 1",Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),duharEndDuratio +" time du class 1",Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),asureEndDuratio +" time au class 1",Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),magrebEndDuratio +" time me class 1",Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),eishaEndDuratio +" time ei class 1",Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "تم الحفظ",Toast.LENGTH_SHORT).show();

    }




} // END OF CLASS
