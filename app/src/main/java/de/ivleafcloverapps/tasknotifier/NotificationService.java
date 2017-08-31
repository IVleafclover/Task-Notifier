package de.ivleafcloverapps.tasknotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Christian on 31.08.2017.
 */

public class NotificationService extends Service {

    private static final int NOTIFICATION_ID = 91;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Test", "hello");
        // set Intent Activity to open, when on notification is clicked
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        // load message for notification
        String message = "Hello World";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!sharedPreferences.getBoolean("switchOn", false)) {
            message = sharedPreferences.getString("notificationText", "Hello World");
        }

        // set Notification here
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.noteicon);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(message).setWhen(System.currentTimeMillis());
        builder.setContentIntent(notificationPendingIntent);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);

        // stop service hear
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // we do not need this method
        return null;
    }
}
