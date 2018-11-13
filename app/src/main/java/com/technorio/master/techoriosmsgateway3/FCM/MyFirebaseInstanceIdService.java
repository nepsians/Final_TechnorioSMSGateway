package com.technorio.master.techoriosmsgateway3.FCM;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;



public class MyFirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("firebaseToken", "Refreshed token: " + refreshedToken);

    }

}
