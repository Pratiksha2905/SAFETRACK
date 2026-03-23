package com.example.safetrack;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class SetupActivity extends AppCompatActivity {

    private EditText etName, etAge, etContact1, etContact2, etContact3;
    private Button btnSave, btnPermissions;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etContact1 = findViewById(R.id.etContact1);
        etContact2 = findViewById(R.id.etContact2);
        etContact3 = findViewById(R.id.etContact3);
        btnSave = findViewById(R.id.btnSave);
        btnPermissions = findViewById(R.id.btnPermissions);

        btnPermissions.setOnClickListener(v -> requestAppPermissions());

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveUserData();
                startActivity(new Intent(SetupActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void requestAppPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.SEND_SMS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.ACTIVITY_RECOGNITION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS);
        }

        ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
    }

    private boolean validateInputs() {
        if (etName.getText().toString().isEmpty() || etAge.getText().toString().isEmpty() ||
                etContact1.getText().toString().isEmpty() || etContact2.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveUserData() {
        SharedPreferences prefs = getSharedPreferences("SafeTrackPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userName", etName.getText().toString());
        editor.putString("userAge", etAge.getText().toString());
        editor.putString("contact1", etContact1.getText().toString());
        editor.putString("contact2", etContact2.getText().toString());
        editor.putString("contact3", etContact3.getText().toString());
        editor.putBoolean("isFirstRun", false);
        editor.apply();
    }
}
