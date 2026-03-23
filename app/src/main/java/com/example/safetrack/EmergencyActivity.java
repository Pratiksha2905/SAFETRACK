package com.example.safetrack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EmergencyActivity extends AppCompatActivity {

    private TextView tvTimer, tvEmergencyTitle;
    private Button btnImSafe, btnNeedHelp;
    private CountDownTimer countDownTimer;
    private boolean isEmergencyTriggered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        tvTimer = findViewById(R.id.tvTimer);
        tvEmergencyTitle = findViewById(R.id.tvEmergencyTitle);
        btnImSafe = findViewById(R.id.btnImSafe);
        btnNeedHelp = findViewById(R.id.btnNeedHelp);

        String triggerType = getIntent().getStringExtra("trigger_type");
        if ("manual".equals(triggerType)) {
            triggerEmergencyAlert();
        } else {
            startCountdown();
        }

        btnImSafe.setOnClickListener(v -> {
            if (countDownTimer != null) countDownTimer.cancel();
            finish();
        });

        btnNeedHelp.setOnClickListener(v -> {
            if (countDownTimer != null) countDownTimer.cancel();
            triggerEmergencyAlert();
        });
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (!isEmergencyTriggered) {
                    triggerEmergencyAlert();
                }
            }
        }.start();
    }

    private void triggerEmergencyAlert() {
        isEmergencyTriggered = true;
        tvTimer.setText("ALERT SENT");
        tvEmergencyTitle.setText("Emergency Alert Activated!");
        
        sendEmergencySMS();
    }

    private void sendEmergencySMS() {
        SharedPreferences prefs = getSharedPreferences("SafeTrackPrefs", MODE_PRIVATE);
        String contact1 = prefs.getString("contact1", "");
        String contact2 = prefs.getString("contact2", "");
        String contact3 = prefs.getString("contact3", "");
        String lat = prefs.getString("last_lat", "Unknown");
        String lng = prefs.getString("last_lng", "Unknown");

        String message = "I may be in danger. This is my current location: " +
                "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lng;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            if (!contact1.isEmpty()) smsManager.sendTextMessage(contact1, null, message, null, null);
            if (!contact2.isEmpty()) smsManager.sendTextMessage(contact2, null, message, null, null);
            if (!contact3.isEmpty()) smsManager.sendTextMessage(contact3, null, message, null, null);
            
            Toast.makeText(this, "Emergency SMS sent to contacts!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
