package com.example.star.increment_ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;


/**
 * Created by Belal on 4/15/2016.
 */

//Class is extending GcmListenerService
public class PushReceiverService extends GcmListenerService {
    public String message;
    public String latitude;
    public String longitude;
    public int availRating;

    //This method will be called on every new message received
    @Override
        public void onMessageReceived(String from, Bundle data) {
            //Getting the message from the bundle
            message= data.getString("message");
        Log.d("SUMIT","Recived Message:-");
            //latitude=data.getString("latitude");
            //longitude=data.getString("longitude");
            //longitude=data.getString("availRating");

            //Displaying a notiffication with the message
            sendNotification(message);
        }

    private void sendNotification(String message) {
        Log.d("SUMIT", "---------------message is :" +message);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;

        int my_notification_id;
        RemoteViews myRemoteView;
        Context myContext=getApplicationContext();

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        //Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
          //      .setSmallIcon(R.mipmap.ic_launcher)
          //      .setContentText(message)
          //      .setAutoCancel(true)
          //      .setContentIntent(pendingIntent);

        //NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        my_notification_id = (int) System.currentTimeMillis();

        Intent button_intent = new Intent(this,Button_listener.class);
        button_intent.putExtra("id",my_notification_id);
        button_intent.putExtra("latitude","33.4216129");
        button_intent.putExtra("longitude","-111.9212029");
        PendingIntent button_pending_event = PendingIntent.getBroadcast(myContext,my_notification_id,
                button_intent,0);


        message="kushal bhatt ko mazaa laane k liyee";
        availRating = 5;
        NotificationManager mynotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        myRemoteView=new RemoteViews(getPackageName(),R.layout.custom_notification);
        myRemoteView.setImageViewResource(R.id.notif_icon,R.mipmap.ic_launcher_round);
        myRemoteView.setTextViewText(R.id.notif_title,message);

        myRemoteView.setOnClickPendingIntent(R.id.button,button_pending_event);

        Intent notification_intent = new Intent(myContext,message_display.class);

        notification_intent.putExtra("availRating","5");
        notification_intent.putExtra("stuff","pizza");
        notification_intent.putExtra("latitude","33.4216129");
        notification_intent.putExtra("longitude","-111.9212029");

        PendingIntent pendingIntent = PendingIntent.getActivity(myContext,0,notification_intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(message)
                .setCustomBigContentView(myRemoteView)
                .setContentIntent(pendingIntent)
                .setContentText(message)
                .setPriority(Notification.PRIORITY_MAX);

        mynotificationManager.notify(0,builder.build());

    }
}