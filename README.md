🛡️ SafeTrack - Personal Safety & Emergency Monitoring Android App
 ![Android](https://img.shields.io/badge/Platform-Android-green.svg)   ![Java](https://img.shields.io/badge/Language-Java-orange.svg)   ![Min SDK](https://img.shields.io/badge/Min%20SDK-21%20(Lollipop)-blue.svg)   ![Target SDK](https://img.shields.io/badge/Target%20SDK-36-brightgreen.svg)   ![License](https://img.shields.io/badge/License-MIT-purple.svg) 
SafeTrack is an Android personal safety application built to protect users during daily commutes, late-night travel, and solo journeys. Powered by an active background foreground service, sensor monitoring, real-time GPS tracking, and automatic SMS dispatch, SafeTrack ensures help is always just a touch—or automated check-in—away.
✨ Key Features
🚨 1. Instant SOS Alert
•
One-Tap Trigger: Instantly dispatch emergency alert SMS messages with a direct Google Maps live location link to up to 3 emergency contacts.
•
Manual Emergency Screen: Instant access from the main monitoring interface.
🛡️ 2. Active Background Journey Monitoring
•
Runs as a persistent Foreground Service with low battery impact.
•
Continuously records live GPS coordinates (latitude and longitude) to prepare precise coordinates in case of an emergency.
⏳ 3. Automated Safety Check-in & Countdown
•
Periodic Prompts: Automatically prompts the user every 10 minutes to verify safety.
•
60-Second Countdown: If the user does not respond within 60 seconds (or taps "I Need Help"), an Emergency SMS with location details is immediately dispatched to all emergency contacts.
•
Lock Screen Popup: Uses Full-Screen Intent notifications (showWhenLocked, turnScreenOn) to ensure check-in prompts pop up even if the phone is locked.
📳 4. Shake & Unusual Movement Detection
•
Leverages the device's Accelerometer sensor to monitor for sudden impact, falls, or sudden physical struggle (shake threshold detection).
•
Automatically triggers the Emergency Check-in screen upon detecting abnormal movement.
🔋 5. Critical Low Battery Emergency SMS
•
Monitored via BroadcastReceiver (ACTION_BATTERY_CHANGED).
•
Automatically sends a critical low-battery SOS message with the user's last known location link when battery drops to 10% or below.
📞 6. Fake Incoming Call
•
Simulates an incoming phone call complete with default ringtone playback and vibration pattern.
•
Helps users safely and discreetly exit uncomfortable or suspicious situations.
🏁 7. Safe Arrival Notification
•
Upon ending a journey monitoring session, the app prompts the user if they want to notify contacts.
•
Tapping "Notify Them" sends an automated "Safe Arrival" SMS to all emergency contacts.
👤 8. Profile & Emergency Contacts Setup
•
Onboarding flow collecting User Name, Age, Blood Group, Medical Notes, and up to 3 Emergency Contacts.
•
Profile editing supported anytime via the main dashboard.
📱 User Flow & Architecture
                       ┌──────────────────┐
                       │  SplashActivity  │
                       └────────┬─────────┘
                                │
                  ┌─────────────┴─────────────┐
             (First Run)                 (Existing Profile)
                  │                           │
        ┌─────────▼────────┐        ┌─────────▼────────┐
        │  SetupActivity   ├───────►│   MainActivity   │
        └──────────────────┘        └────────┬─────────┘
                                             │
                                ┌────────────┴────────────┐
                                │                         │
                       (Edit Profile)             (Start Monitoring)
                                │                         │
                       ┌────────▼─────────┐      ┌────────▼─────────┐
                       │  SetupActivity   │      │MonitoringActivity│
                       └──────────────────┘      └────────┬─────────┘
                                                          │
                                         ┌────────────────┼────────────────┐
                                         │                │                │
                                    (Manual SOS)     (Fake Call)    (MonitoringService)
                                         │                │                │
                                ┌────────▼────────┐┌──────▼───────┐┌───────▼────────┐
                                │EmergencyActivity││FakeCallActivity││ Sensor/Battery │
                                └─────────────────┘└──────────────┘└───────┬────────┘
                                                                           │
                                                                   (Shake/Timer Trigger)
                                                                           │
                                                                  ┌────────▼────────┐
                                                                  │EmergencyActivity│
                                                                  └─────────────────┘
🛠️ Tech Stack & Dependencies
•
Language: Java
•
Architecture: Android Application Architecture (Services, BroadcastReceivers, Activities)
•
UI Framework: AndroidX AppCompat & Google Material Design Components
•
Min SDK: 21 (Android 5.0 Lollipop)
•
Target / Compile SDK: 36 (Android 15+)
•
Build System: Gradle (Kotlin DSL build.gradle.kts)
•
Key APIs & Components:
◦
LocationManager for fine GPS tracking
◦
SensorManager & SensorEventListener for Accelerometer shake detection
◦
SmsManager for direct emergency messaging
◦
BroadcastReceiver for battery percentage monitoring
◦
SharedPreferences for persistent local data storage
◦
NotificationCompat with setFullScreenIntent for lock-screen emergency popups
🔒 Permissions Requested
SafeTrack requires specific system permissions to function reliably as an emergency application:
Permission
Purpose
ACCESS_FINE_LOCATION
Accurately obtain current GPS coordinates for emergency location links.
ACCESS_COARSE_LOCATION
Fallback location monitoring.
SEND_SMS
Directly dispatch emergency SOS and safe arrival messages to designated contacts.
FOREGROUND_SERVICE
Maintain active background monitoring during journeys.
FOREGROUND_SERVICE_LOCATION
Required on Android 14+ for foreground location services.
ACTIVITY_RECOGNITION
Detect user motion state transitions.
POST_NOTIFICATIONS
Send background service status and emergency check-in notifications (Android 13+).
USE_FULL_SCREEN_INTENT
Display full-screen emergency check-in screens over lock screen.
VIBRATE
Provide tactile feedback during fake calls and emergency alerts.
📂 Project Structure
SAFETRACK/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/com/example/safetrack/
│           │   ├── SplashActivity.java       # Launch screen routing
│           │   ├── SetupActivity.java        # User profile & emergency contacts setup
│           │   ├── MainActivity.java         # Main dashboard & greeting
│           │   ├── MonitoringActivity.java   # Active journey monitoring UI
│           │   ├── MonitoringService.java    # Background service (GPS, Sensor, Battery)
│           │   ├── EmergencyActivity.java    # 60-second countdown & SMS trigger
│           │   └── FakeCallActivity.java     # Fake incoming call simulation
│           └── res/
│               ├── layout/                   # XML Activity layouts
│               ├── drawable/                 # Custom button & background drawables
│               └── values/                   # Themes, colors, strings
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
🚀 Getting Started
Prerequisites
•
Android Studio: Ladybug (2024.2.1+) or newer
•
JDK Version: Java 11 or higher
•
Android Device / Emulator: Running Android 5.0 (API 21) or higher
Note: Sending SMS and GPS location tracking are best tested on a physical Android device with an active SIM card.
Installation Steps
1.
Clone the Repository:
git clone https://github.com/YOUR_USERNAME/SAFETRACK.git
cd SAFETRACK
2.
Open in Android Studio:
◦
Launch Android Studio.
◦
Select Open an Existing Project and navigate to the cloned SAFETRACK folder.
3.
Build the Project:
◦
Allow Gradle sync to complete automatically.
◦
Build using Gradle -> app -> Tasks -> build -> assembleDebug or click Build > Make Project.
4.
Run on Device:
◦
Connect your Android physical device via USB Debugging (or start an emulator).
◦
Press Shift + F10 or click Run 'app'.
5.
Grant Permissions:
◦
Upon first launching the app, tap Grant Permissions on the Setup screen to allow Location, SMS, Notification, and Motion permissions.

⚠️ Disclaimer
SafeTrack is designed as a personal safety tool to assist in personal emergency situations. It relies on standard Android cellular network SMS and GPS location hardware. It is not a replacement for official local emergency response services (such as 911 / 112 / 100). Always contact local authorities directly whenever possible in life-threatening situations.
📄 License
This project is licensed under the MIT License - feel free to modify and adapt for personal or educational use.
