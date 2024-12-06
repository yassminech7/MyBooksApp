package com.example.projetmobilediitwm;

import android.content.Intent;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView logo = findViewById(R.id.logo);
        TextView appName = findViewById(R.id.app_name);
        TextView subtitle = findViewById(R.id.subtitle);
        Button btnCreateAccount = findViewById(R.id.btn_create_account);
        Button btnLogin = findViewById(R.id.btn_login);

        Handler handler = new Handler();

        handler.postDelayed(() -> {
            logo.setVisibility(View.VISIBLE);
            ObjectAnimator fadeInLogo = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
            fadeInLogo.setDuration(1000);
            fadeInLogo.start();
        }, 1000);

        handler.postDelayed(() -> {
            ObjectAnimator moveLogoUp = ObjectAnimator.ofFloat(logo, "translationY", 0f, -150f);
            moveLogoUp.setDuration(1000);
            moveLogoUp.start();
        }, 3000);

        handler.postDelayed(() -> {
            appName.setVisibility(View.VISIBLE);
            ObjectAnimator fadeInAppName = ObjectAnimator.ofFloat(appName, "alpha", 0f, 1f);
            fadeInAppName.setDuration(1000);
            fadeInAppName.start();
        }, 5000);

        handler.postDelayed(() -> {
            subtitle.setVisibility(View.VISIBLE);
            ObjectAnimator fadeInSubtitle = ObjectAnimator.ofFloat(subtitle, "alpha", 0f, 1f);
            fadeInSubtitle.setDuration(1000);
            fadeInSubtitle.start();
        }, 7000);

        handler.postDelayed(() -> {
            btnCreateAccount.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);

            ObjectAnimator fadeInCreateAccount = ObjectAnimator.ofFloat(btnCreateAccount, "alpha", 0f, 1f);
            ObjectAnimator fadeInLogin = ObjectAnimator.ofFloat(btnLogin, "alpha", 0f, 1f);
            fadeInCreateAccount.setDuration(1000);
            fadeInLogin.setDuration(1000);

            fadeInCreateAccount.start();
            fadeInLogin.start();
        }, 9000);
        btnCreateAccount.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
