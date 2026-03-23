package com.example.safetrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MonitoringActivity extends AppCompatActivity {

    private Button btnStopMonitoring, btnSOSLarge, btnFakeCall;
    private TextView tvStatus, tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        tvStatus = findViewById(R.id.tvStatus);
        tvLocation = findViewById(R.id.tvLocation);
        btnSOSLarge = findViewById(R.id.btnSOSLarge);
        btnFakeCall = findViewById(R.id.btnFakeCall);
        btnStopMonitoring = findViewById(R.id.btnStopMonitoring);

        // Start the background monitoring service
        Intent serviceIntent = new Intent(this, MonitoringService.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

        btnSOSLarge.setOnClickListener(v -> {
            triggerEmergency();
        });

        btnFakeCall.setOnClickListener(v -> {
            Intent intent = new Intent(MonitoringActivity.this, FakeCallActivity.class);
            startActivity(intent);
        });

        btnStopMonitoring.setOnClickListener(v -> {
            stopMonitoring();
        });
    }

    private void triggerEmergency() {
        Intent intent = new Intent(this, EmergencyActivity.class);
        intent.putExtra("trigger_type", "manual");
        startActivity(intent);
    }

    private void stopMonitoring() {
        Intent serviceIntent = new Intent(this, MonitoringService.class);
        stopService(serviceIntent);
        finish();
    }
}
