package com.samcore.leafdisease.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.samcore.leafdisease.R;
import com.samcore.leafdisease.components.AppSession;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    Animation imgAnimation,textAnimation;
    AppSession appSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appSession=new AppSession(getApplicationContext());
        findViewById();
    }

    private void findViewById() {
        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.text);
        imgAnimation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_anim);
        imageView.startAnimation(imgAnimation);
        textAnimation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);

        new Handler().postDelayed(() -> {
            textView.startAnimation(textAnimation);
            if (appSession.getKeyIsLoggedIn()){
                Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
