package com.example.safetrack;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MonitoringService extends Service implements SensorEventListener {

    private static final String CHANNEL_ID = "SafeTrackServiceChannel";
    private LocationManager locationManager;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    
    private float lastX, lastY, lastZ;
    private long lastUpdate;
    private static final int SHAKE_THRESHOLD = 800;

    private Handler safetyCheckHandler = new Handler();
    private Runnable safetyCheckRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        startLocationUpdates();
        
        startSafetyCheckTimer();
    }

    private void startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Update current location in SharedPreferences or a static variable
                    getSharedPreferences("SafeTrackPrefs", MODE_PRIVATE).edit()
                            .putString("last_lat", String.valueOf(location.getLatitude()))
                            .putString("last_lng", String.valueOf(location.getLongitude()))
                            .apply();
                }
                @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override public void onProviderEnabled(String provider) {}
                @Override public void onProviderDisabled(String provider) {}
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void startSafetyCheckTimer() {
        safetyCheckRunnable = new Runnable() {
            @Override
            public void run() {
                // Trigger Safety Check every 10 minutes (600,000 ms)
                // For testing, you might want to reduce this to 1 minute
                Intent intent = new Intent(MonitoringService.this, EmergencyActivity.class);
                intent.putExtra("trigger_type", "safety_check");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                
                safetyCheckHandler.postDelayed(this, 60000);
            }
        };
        safetyCheckHandler.postDelayed(safetyCheckRunnable, 60000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MonitoringActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SafeTrack Monitoring")
                .setContentText("We are monitoring your journey...")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Ensure this exists or use a default
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        safetyCheckHandler.removeCallbacks(safetyCheckRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                // Unusual movement detected
                Intent intent = new Intent(this, EmergencyActivity.class);
                intent.putExtra("trigger_type", "unusual_movement");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            lastX = x;
            lastY = y;
            lastZ = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "SafeTrack Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
