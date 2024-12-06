package com.example.projetmobilediitwm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        TextView subtitle = findViewById(R.id.subtitle);
        EditText emailEditText = findViewById(R.id.email); // Changer ici
        EditText passwordEditText = findViewById(R.id.password);
        ImageView eyeIcon = findViewById(R.id.eye_icon);

        boolean[] isPasswordVisible = {false};

        eyeIcon.setOnClickListener(v -> {
            if (isPasswordVisible[0]) {
                passwordEditText.setInputType(129);
                isPasswordVisible[0] = false;
            } else {
                passwordEditText.setInputType(1);
                isPasswordVisible[0] = true;
            }
            passwordEditText.setSelection(passwordEditText.getText().length());
        });

        // Stylisation et clic sur "Sign up here"
        String text = "Don't have an account yet ? Sign up here";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), text.indexOf("Sign up here"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), text.indexOf("Sign up here"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        subtitle.setText(spannableString);
        subtitle.setClickable(true);
        subtitle.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_login).setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                return;
            }


            boolean isValidUser = db.validateUserByEmail(email, password);

            if (isValidUser) {
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", email);
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, BooksActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
