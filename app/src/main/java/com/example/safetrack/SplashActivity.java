package com.example.safetrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Using Looper.getMainLooper() ensures the task runs correctly on the main thread
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("SafeTrackPrefs", MODE_PRIVATE);
            boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

            Intent intent;
            if (isFirstRun) {
                intent = new Intent(SplashActivity.this, SetupActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000); // Reduced to 2 seconds for a smoother feel
    }
}
