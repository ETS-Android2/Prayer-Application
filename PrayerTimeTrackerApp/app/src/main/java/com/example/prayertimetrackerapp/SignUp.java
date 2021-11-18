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

public class SignUp extends AppCompatActivity {
    public  DBManager dbManager; //  use this to work with database
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbManager = new DBManager(this);  // this line should be in each activity
        dbManager.open(); // open database ---- this line should be in each activity

        Button signUp = findViewById(R.id.SignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(!checkEmpty()){
                 if(!checkPassAndEmail()){
                     if(!checkUserExistBefor()) {
                         addUser();
                     }
                }
             }
            }
        });

        TextView goLogin = findViewById(R.id.goLogin);
        goLogin.setPaintFlags(goLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getApplicationContext(),LogIn.class);
               // startActivity(i);
                finish();
            }
        });
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


    public boolean checkEmpty(){
        boolean empty = false;

        EditText name = findViewById(R.id.nameSignUp);
        EditText email = findViewById(R.id.emailSignUp);
        EditText pass = findViewById(R.id.passSignUp);
        EditText pass2 = findViewById(R.id.pass2SignUp);

        if(name.getText().length()==0){
            name.setError("الاسم مطلوب");
            empty = true ;
        }
        if(email.getText().length()==0){
            email.setError("الايميل مطلوب");
            empty = true ;
        }
        if(pass.getText().length()==0){
            pass.setError("كلمة المرور مطلوبة");
            empty = true ;
        }
        if(pass2.getText().length()==0){
            pass2.setError("تأكيد كلمة المرور مطلوبة");
            empty = true ;
        }
       return empty;
    }
    public boolean checkPassAndEmail() {
        boolean error = false;

        EditText email = findViewById(R.id.emailSignUp);
        EditText pass = findViewById(R.id.passSignUp);
        EditText pass2 = findViewById(R.id.pass2SignUp);
        if(!pass.getText().toString().equals(pass2.getText().toString())){
            pass2.setError("تأكيد كلمة المرور لا تطابق كلمة المرور");
            error = true ;
        }

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
    public void addUser(){
        EditText name = findViewById(R.id.nameSignUp);
        EditText email = findViewById(R.id.emailSignUp);
        EditText pass = findViewById(R.id.passSignUp);

        dbManager.insertNewUser(name.getText().toString(),email.getText().toString(),pass.getText().toString(),"false");
        Toast.makeText(getApplicationContext(),"تم انشاء الحساب بنجاح",Toast.LENGTH_LONG).show();
        SharedPreferences userDetails =getSharedPreferences("userdetails", MODE_PRIVATE);// to share information between activity
        SharedPreferences.Editor edit = userDetails.edit();
        edit.clear();
        edit.putString("email", email.getText().toString());
        edit.commit();// add the information
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
    public boolean checkUserExistBefor(){
        Cursor users = dbManager.getAllUser();
        int countt = users.getCount();
        boolean isExist = false;
        EditText email = findViewById(R.id.emailSignUp);
        if(countt != 0) {
            if (users.moveToFirst()) {
                while (!users.isAfterLast()) {
                    String emailU = users.getString(users.getColumnIndex("email"));

                    if(email.getText().toString().equals(emailU)){
                        isExist = true;
                        email.setError("البريد الالكتروني مستخدم");

                    }

                    users.moveToNext();
                }
            }
        }
        return isExist;
    }
    }