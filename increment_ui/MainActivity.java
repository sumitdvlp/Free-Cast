package com.example.star.increment_ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


import com.google.android.gms.location.LocationRequest;


//this is our main activity
public class MainActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    //Creating a broadcast receiver for gcm registration
    private RadioGroup radioGroup;
    public Spinner eventLocation;
    public Spinner foodType;

    public Button buttonSend;
    public EditText otherText;
    public String freeBieValue;
    public String locationValue;
    public SeekBar hotBar;
    public int hotBarValue;
    public  String message;
    public String lat;
    public String lon;
    public String locationString;
    public EditText customMessage;


    LocationAPI locationService;
    LocationRequest mLocationRequest;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = (RadioGroup) findViewById(R.id.RGroup);
        buttonSend=(Button) findViewById(R.id.buttonSend);
        eventLocation=(Spinner) findViewById(R.id.eventLocation);
        foodType=(Spinner)findViewById(R.id.foodType);
        hotBar = (SeekBar)findViewById(R.id.hotBar);
        locationService = new LocationAPI(this);
        otherText=(EditText) findViewById(R.id.otherText);
        customMessage=(EditText) findViewById(R.id.customMessage);
        foodType.setEnabled(false);
        otherText.setEnabled(false);
        radioGroup.setOnCheckedChangeListener(this);

     //   Intent intent = new Intent(this, RegistrationIntentService.class);
     //   startService(intent);
        //Initializing our broadcast receiver
        /*mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully

                if(intent.getAction().equals(RegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    //Displaying the token as toast
                    Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //if the intent is not with success then displaying error messages
                } else if(intent.getAction().equals(RegistrationIntentService.REGISTRATION_ERROR)){
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };*/

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, RegistrationIntentService.class);
            startService(itent);
        }
        buttonSend.setOnClickListener(this);
    }

    @Override
    protected void onStart(){
        locationService.connect();
        super.onStart();
    }

    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_ERROR));
    }
    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
    @Override
    public void onClick(View view) {
        if(view==buttonSend ){
            hotBarValue=hotBar.getProgress();
            locationValue= eventLocation.getSelectedItem().toString().trim();
//            Log.w("Latitude", String.valueOf(LocationAPI.mLastLocation.getLatitude()));
  //          Log.w("Longitude", String.valueOf(LocationAPI.mLastLocation.getLongitude()));
//            String lng = String.valueOf(LocationAPI.mLastLocation.getLongitude());

            if(locationValue.equals("Current Location")){
                //openMaps();
                locationValue=getLocation();
                if(locationValue.equals(null)){locationValue=getLocation();}
            }
            else  //pre-defined hotspots
            {

            }

            switch (locationValue){
                case "Current Location":
                    locationString=getResources().getString(R.string.Memorial_Union_North);
                case "Memorial Union North":
                    locationString=getResources().getString(R.string.Memorial_Union_North);
                case "Memorial Union 2nd Floor":
                    locationString=getResources().getString(R.string.Memorial_Union_2nd_Floor);
                case "Hayden Lawn":
                    locationString=getResources().getString(R.string.Hayden_Lawn);
                case "College Ave Commons":
                    locationString=getResources().getString(R.string.College_Ave_Commons);
                case "Sundevils Stadium":
                    locationString=getResources().getString(R.string.Sundevils_Stadium);
            }
            String[] parts = locationString.split(",");
            lon=parts[0];
            lat=parts[1];


            if(radioGroup.getCheckedRadioButtonId()==R.id.other){

                freeBieValue=otherText.getText().toString().trim();
            }
            else if(radioGroup.getCheckedRadioButtonId()==R.id.swag){freeBieValue="Swag";}
            else if(radioGroup.getCheckedRadioButtonId()==R.id.freefood){
                freeBieValue=foodType.getSelectedItem().toString().trim();
            }
            else if(radioGroup.getCheckedRadioButtonId()==R.id.tshirt){freeBieValue="T-Shirt";}

            //customMessage
            //lat
            //long
            //hotBarValue
            //freeBieValue
            Bundle data = new Bundle();
            data.putString("longitude",lat);
            sendMessage(data);

            Toast.makeText(getApplicationContext(), "Freebie  is "+freeBieValue+" at location "+lon+","+lat+" hot bar value "+hotBarValue+" \n :)", Toast.LENGTH_LONG).show();
        }
    }
    void sendMessage(Bundle message){
        //
    }
    @Override
    protected void onStop() {
        locationService.disconnect();
        super.onStop();
    }
    public String getLocation() {
        Location mLastLocation = locationService.fetchLatestLocation();
        if(mLastLocation.toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "turn location on you device and press send button again", Toast.LENGTH_LONG).show();
            return  null;
        }
        else {
            Log.w("sumit in getLocation", "location is :" + mLastLocation.toString());
            return mLastLocation.toString();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if(checkedId==R.id.freefood){
            foodType.setEnabled(true);
        }
        else
        if(checkedId==R.id.other){
            otherText.setEnabled(true);
        }
        else{
            foodType.setEnabled(false);
            otherText.setEnabled(false);
        }
    }
}
