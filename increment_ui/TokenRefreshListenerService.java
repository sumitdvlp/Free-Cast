package com.example.star.increment_ui;

import android.app.Service;

/**
 * Created by star on 4/15/17.
 */
import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;


/**
 * Created by Belal on 4/15/2016.
 */
public class TokenRefreshListenerService extends InstanceIDListenerService {

    //If the token is changed registering the device again
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}