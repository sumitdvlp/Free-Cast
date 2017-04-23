package com.example.star.increment_ui;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by star on 4/16/17.
 */

public class Button_listener extends BroadcastReceiver{
    String message;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("sumit-sendNotification", "message is :" +message);
        NotificationManager manager=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        message=intent.getExtras().getString("message");
        manager.cancel(intent.getExtras().getInt("id"));

        String lat = intent.getExtras().getString("latitude");
        String lng = intent.getExtras().getString("longitude");
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+lat+","+lng+"(Current Location)");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mapIntent.setPackage("com.google.android.apps.maps");


        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }
}
