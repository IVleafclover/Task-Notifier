package de.ivleafcloverapps.tasknotifier;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class MainActivity extends Activity {

    private EditText notificationText;
    private EditText notificationTime;
    private Button setNotification;
    private Button goToSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the view elements
        notificationText = findViewById(R.id.editTextNotificationText);
        notificationTime = findViewById(R.id.editTextNotificationTime);
        setNotification = findViewById(R.id.buttonSetNotification);
        goToSettings = findViewById(R.id.buttonGoToSettings);

        // load last values from the shared preferences and set them to the inputs
        // our context is this activity
        Context context = this;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        // only load message if switch is not active
        if(!sharedPreferences.getBoolean("switchOn", false)) {
            notificationText.setText(sharedPreferences.getString("notificationText", "Hello World"));
        } else {
            notificationText.setText("Hello World");
        }
        notificationTime.setText(sharedPreferences.getString("notificationTime", "60"));

        // set listeners for buttons
        goToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        setNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save notificationText before setting notification
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("notificationText", notificationText.getText().toString());
                editor.apply();

                // create AlarmManager
                MainActivity context = MainActivity.this;
                AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
                Intent startNotificationServiceIntent = new Intent(MainActivity.this, NotificationService.class);
                PendingIntent startNotificationServicePendingIntent = PendingIntent.getService(MainActivity.this, 0, startNotificationServiceIntent, 0);

                // calculate the notification time
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, Integer.parseInt(notificationTime.getText().toString()));

                // set notification
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), startNotificationServicePendingIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // when coming back from the settings we have to check the switch
        // overwrite input if switch is on
        // our context is this activity
        Context context = this;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences.getBoolean("switchOn", false)) {
            notificationText.setText("Hello World");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // save inputs to shared preferences
        // our context is this activity
        Context context = this;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("notificationText", notificationText.getText().toString());
        editor.putString("notificationTime", notificationTime.getText().toString());
        // commit changes
        editor.apply();
    }
}
