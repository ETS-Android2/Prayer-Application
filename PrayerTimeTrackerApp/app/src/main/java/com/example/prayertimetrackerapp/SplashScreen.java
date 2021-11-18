package com.example.prayertimetrackerapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    VideoView splash ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);
        // getSupportActionBar().hide();
        splash = (VideoView)findViewById(R.id.videoView);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.splash);
        splash.setVideoURI(video);

        splash.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isFinishing())
                    return;
                startActivity(new Intent(SplashScreen.this,LogIn.class));
                finish();
            }
        });
        splash.start();
    }
}
