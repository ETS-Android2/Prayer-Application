package com.example.prayertimetrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class LogIn extends AppCompatActivity {
    public  DBManager dbManager; //  use this to work with database
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        TextView goSignUp = findViewById(R.id.gosignup);
        Button LogIn = findViewById(R.id.login);

        dbManager = new DBManager(this);  // this line should be in each activity
        dbManager.open(); // open database ---- this line should be in each activity

        goSignUp.setPaintFlags(goSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        goSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SignUp.class);
                startActivity(i);
            }
        });

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(!checkEmpty()){
                if(!checkEmail()) {
                checkUser();
                }
             }
            }
        }); }

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

        public boolean checkEmpty(){

            boolean empty = false;

            EditText email = findViewById(R.id.emailLogIn);
            EditText pass = findViewById(R.id.passLogIn);


            if(email.getText().length()==0){
                email.setError("الايميل مطلوب");
                empty = true ;
            }

            if(pass.getText().length()==0){
                pass.setError("كلمة المرور مطلوبة");
                empty = true ;
            }

            return empty;

        }
public boolean checkEmail(){
            boolean error = false;

            EditText email = findViewById(R.id.emailLogIn);

            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);
            if(!pat.matcher(email.getText().toString()).matches()){
                email.setError("صيغة البريد الالكتروني غير صحيحة");
                error = true ;
            }

            return error;
        }
public void checkUser(){
        boolean foundUser = false;
        boolean correctEmail = false;

    EditText email = findViewById(R.id.emailLogIn);
    EditText pass = findViewById(R.id.passLogIn);

    String emailString = email.getText().toString();
    String passString = pass.getText().toString();

    Cursor users = dbManager.getAllUser();
    int countt = users.getCount();

    if(countt != 0) {
        if (users.moveToFirst()) {
            while (!users.isAfterLast()) {
                String emailU = users.getString(users.getColumnIndex("email"));
                String passU = users.getString(users.getColumnIndex("password"));

                if(emailString.equals(emailU)){
                    correctEmail = true;
                    if(passString.equals(passU)){
                        foundUser = true ;
                    }
                }

                users.moveToNext();
            }
        }
    }

    if(correctEmail){
        if(foundUser){
            Toast.makeText(getApplicationContext(),"تم تسجيل الدخول بنجاح",Toast.LENGTH_LONG).show();
            SharedPreferences userDetails =getSharedPreferences("userdetails", MODE_PRIVATE);// to share information between activity
            SharedPreferences.Editor edit = userDetails.edit();
            edit.clear();
            edit.putString("email", emailString);
            edit.commit();// add the information
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }else{
            Toast.makeText(getApplicationContext(),"كلمة المرور خاطئة !",Toast.LENGTH_LONG).show();
        }

    }else{
        Toast.makeText(getApplicationContext(),"البريد الالكتروني غير موجود",Toast.LENGTH_LONG).show();

    }


}
}