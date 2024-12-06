package com.example.projetmobilediitwm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = new DatabaseHelper(this);

        TextView subtitle = findViewById(R.id.subtitle);

        String text = "Already have an account ? Login here";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), text.indexOf("Login here"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), text.indexOf("Login here"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        subtitle.setText(spannableString);

        subtitle.setClickable(true);
        subtitle.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        EditText usernameEditText = findViewById(R.id.username);
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);
        RadioGroup sexRadioGroup = findViewById(R.id.radio_group_sex);
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

        findViewById(R.id.btn_signup).setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            int selectedSexId = sexRadioGroup.getCheckedRadioButtonId();

            if (username.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Username is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(SignupActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 8) {
                Toast.makeText(SignupActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedSexId == -1) {
                Toast.makeText(SignupActivity.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                return;
            }

            String gender = (selectedSexId == R.id.radio_male) ? "Male" : "Female";

            if (db.checkUserExists(email)) {
                Toast.makeText(SignupActivity.this, "Email already exists. Please use another email.", Toast.LENGTH_SHORT).show();
                return;
            }


            boolean isInserted = db.insertUser(username, email, password, gender);
            if (isInserted) {
                Toast.makeText(SignupActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignupActivity.this, "Failed to create account. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
