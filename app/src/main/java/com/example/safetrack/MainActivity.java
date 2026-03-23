package com.example.safetrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnStartMonitoring, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartMonitoring = findViewById(R.id.btnStartMonitoring);
        btnExit = findViewById(R.id.btnExit);

        btnStartMonitoring.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MonitoringActivity.class);
            startActivity(intent);
        });

        btnExit.setOnClickListener(v -> finishAffinity());
    }
}
