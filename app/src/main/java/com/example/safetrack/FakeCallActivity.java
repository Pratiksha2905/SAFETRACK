package com.example.safetrack;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FakeCallActivity extends AppCompatActivity {

    private Ringtone ringtone;
    private Vibrator vibrator;
    private FloatingActionButton fabDecline, fabAccept;
    private TextView tvCallStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_call);

        fabDecline = findViewById(R.id.fabDecline);
        fabAccept = findViewById(R.id.fabAccept);
        tvCallStatus = findViewById(R.id.tvCallStatus);

        // Prepare Ringtone
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        
        // Prepare Vibration
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 1000}; // vibrate for 1s, pause for 1s

        if (ringtone != null) ringtone.play();
        if (vibrator != null) vibrator.vibrate(pattern, 0);

        fabDecline.setOnClickListener(v -> {
            stopCallEffects();
            finish();
        });

        fabAccept.setOnClickListener(v -> {
            stopCallEffects();
            tvCallStatus.setText("00:01");
            tvCallStatus.setTextColor(getResources().getColor(android.R.color.holo_green_light));
            fabAccept.hide();
            // In a real app, we could show a "hang up" button here
        });
    }

    private void stopCallEffects() {
        if (ringtone != null && ringtone.isPlaying()) ringtone.stop();
        if (vibrator != null) vibrator.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCallEffects();
    }
}
