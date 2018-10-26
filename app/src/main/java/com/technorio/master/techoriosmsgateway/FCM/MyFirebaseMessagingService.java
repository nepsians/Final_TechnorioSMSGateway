package com.technorio.master.techoriosmsgateway.FCM;

import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.technorio.master.techoriosmsgateway.Main.MainActivity;
import com.technorio.master.techoriosmsgateway.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ujjwal on 10/23/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String message;
    String TAG="sms";

    ArrayList<String> numberList = new ArrayList<>();
    SmsManager smsManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        smsManager = SmsManager.getDefault();

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String data = remoteMessage.getData().get("body");

        try {
            JSONObject jsonData = new JSONObject(data);
            message = jsonData.getString("message");
            Log.d("messsage", message);

            JSONArray array = jsonData.getJSONArray("numbers");
            for (int i = 0; i < array.length(); i++) {
                numberList.add(array.getString(i));
                Log.d("number_" + i, array.getString(i));
                sendMessage(array.getString(i));  //this method is not called when the app is in background
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("sms", "exception occured====================no------");
        }

        MyNotificationManager.getmInstance(getApplicationContext())
                .displayNotification(title, body, message, numberList);
    }

    public void sendMessage(String phoneNo){
       //smsManager.sendTextMessage(phoneNo, null, message, null, null);
        SubscriptionManager subscriptionManager = null;
        subscriptionManager = SubscriptionManager.from(this);
        SubscriptionInfo subscriptionInfo = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(SharedPrefManager.getInstance(getApplicationContext()).getSimId());

        ArrayList<String> parts = smsManager.divideMessage(message);


        if(parts.size() != 1){
            smsManager.getSmsManagerForSubscriptionId(subscriptionInfo.getSubscriptionId()).sendMultipartTextMessage(phoneNo,null, parts,null,null);

        }else{
            smsManager.getSmsManagerForSubscriptionId(subscriptionInfo.getSubscriptionId()).sendTextMessage(phoneNo,null,message,null,null);
        }
        if (SharedPrefManager.getInstance(getApplicationContext()).getSimId() == 0) {
            Log.d(TAG, "sendSMS: " + "Message send from sim 1.");
        } else if (SharedPrefManager.getInstance(getApplicationContext()).getSimId() == 1) {
            Log.d(TAG, "sendSMS: " + "Message send from sim 2.");
        }else {
            Log.d(TAG, "sendSMS: " + "Invalid Number.");
        }
    }

}
