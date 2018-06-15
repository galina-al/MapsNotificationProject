package com.example.user.mapsproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.user.mapsproject.ui.MapsActivity;

public class ProximityIntentReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {

        String key = LocationManager.KEY_PROXIMITY_ENTERING;

        Boolean entering = intent.getBooleanExtra(key, false);

        if (entering) {
            Log.d(getClass().getSimpleName(), "entering");
            createNotification(context, "entering");
        }
        else {
            Log.d(getClass().getSimpleName(), "exiting");
            createNotification(context, "exiting");
        }

//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, null, 0);
//
//        Notification notification = createNotification();
//        notification.contentIntent = pendingIntent;
//
//        notificationManager.notify(NOTIFICATION_ID, notification);


    }

//    private Notification createNotification() {
//        Notification notification = new Notification();
//
//        notification.icon = R.drawable.add_icon;
//        notification.when = System.currentTimeMillis();
//
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
//
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//        notification.defaults |= Notification.DEFAULT_LIGHTS;
//
//        notification.ledARGB = Color.WHITE;
//        notification.ledOnMS = 1500;
//        notification.ledOffMS = 1500;
//
//        return notification;
//    }

    public void createNotification(Context context, String contentText) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.support.v4.R.drawable.notification_icon_background)
                        .setContentTitle("Notice")
                        .setContentText(contentText);
        Intent resultIntent = new Intent(context, MapsActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MapsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        Notification note = mBuilder.build();
        note.defaults |= Notification.DEFAULT_VIBRATE;
        note.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, note);
    }

}