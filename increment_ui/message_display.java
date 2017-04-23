package com.example.star.increment_ui;

/**
 * Created by star on 4/16/17.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class message_display extends AppCompatActivity implements View.OnClickListener{

    public Button buttonShowMap;
    public TextView messageView;
    public String longitude;
    public String latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_display);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        String meg = bundle.getString("stuff");
        messageView = (TextView)findViewById(R.id.stuffView);
        meg=meg+" is available";
        messageView.setText(meg);

        String avail = bundle.getString("availRating");
        messageView = (TextView)findViewById(R.id.availView);
        if(Integer.valueOf(avail)>7){messageView.setBackgroundColor(Color.GREEN);}
        else if(Integer.valueOf(avail)<7&&Integer.valueOf(avail)>3){messageView.setBackgroundColor(Color.YELLOW);}
        else if(Integer.valueOf(avail)<3){messageView.setBackgroundColor(Color.RED);}
        else{messageView.setBackgroundColor(Color.WHITE);}

        latitude=bundle.getString("latitude");
        longitude=bundle.getString("longitude");
        buttonShowMap=(Button)findViewById(R.id.showMapButton);
        buttonShowMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==buttonShowMap) {
            openMaps();
        }
    }
    public void openMaps()
    {
        String lat = latitude;
        String lng = longitude;
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+lat+","+lng+"(Current Location)");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}