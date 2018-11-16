package com.technorio.master.techoriosmsgateway3.FCM;

import android.content.ContentValues;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.technorio.master.techoriosmsgateway3.Utils.DatabaseHelper;
import com.technorio.master.techoriosmsgateway3.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ujjwal on 10/23/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String message;
    String TAG = "sms";

    ArrayList<String> numberList = new ArrayList<>();
    SmsManager smsManager;
    DatabaseHelper databaseHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (SharedPrefManager.getInstance(getApplicationContext()).getUserStatus()) {

            smsManager = SmsManager.getDefault();

            //String title = remoteMessage.getNotification().getTitle();
            //String body = remoteMessage.getNotification().getBody();
            String data = remoteMessage.getData().get("body");
            databaseHelper = new DatabaseHelper(getApplicationContext());
            try {

                JSONObject jsonData = new JSONObject(data);
                message = jsonData.getString("message");
                Log.d("messsage", message);


                long message_id = databaseHelper.insertMessage(message);

                JSONArray array = jsonData.getJSONArray("numbers");
                for (int i = 0; i < array.length(); i++) {
                    numberList.add(array.getString(i));
                    Log.d("number_" + i, array.getString(i));
                    sendMessage(array.getString(i));

                    databaseHelper.insertRecord(array.getString(i), message_id);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("sms", "exception. cannot fetch data");
            }

            MyNotificationManager.getmInstance(getApplicationContext())
                    .displayNotification("Technorio SMS notification", "Technorio SMS notification", message, numberList);



            // scheduleJob(data);
        }

    }


    public void sendMessage(String phoneNo) {
        SubscriptionManager subscriptionManager = SubscriptionManager.from(this);
        SubscriptionInfo subscriptionInfo = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(SharedPrefManager.getInstance(getApplicationContext()).getSimId());

        ArrayList<String> parts = smsManager.divideMessage(message);

        if (parts.size() != 1) {
            smsManager.getSmsManagerForSubscriptionId(subscriptionInfo.getSubscriptionId()).sendMultipartTextMessage(phoneNo, null, parts, null, null);

        } else {
            smsManager.getSmsManagerForSubscriptionId(subscriptionInfo.getSubscriptionId()).sendTextMessage(phoneNo, null, message, null, null);
        }
        if (SharedPrefManager.getInstance(getApplicationContext()).getSimId() == 0) {
            Log.d(TAG, "sendSMS: " + "Message send from sim 1.");
        } else if (SharedPrefManager.getInstance(getApplicationContext()).getSimId() == 1) {
            Log.d(TAG, "sendSMS: " + "Message send from sim 2.");
        } else {
            Log.d(TAG, "sendSMS: " + "Invalid Number.");
        }
    }

}
