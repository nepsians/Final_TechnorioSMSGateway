package com.technorio.master.techoriosmsgateway.FCM;



import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.technorio.master.techoriosmsgateway.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ujjwal on 10/30/2018.
 */

public class MyMessengerService extends JobService {

    String TAG = "sms";
    SmsManager smsManager;

    @Override
    public boolean onStartJob(JobParameters job) {

        String data = job.getExtras().getString("myData");

        smsManager = SmsManager.getDefault();
        fetchData(data);

        return true;
    }

    private void fetchData(String data) {
         try {
            JSONObject jsonData = new JSONObject(data);
            String message = jsonData.getString("message");
            Log.d("messsage", message);

            JSONArray array = jsonData.getJSONArray("numbers");
             ArrayList<String> numberList = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                numberList.add(array.getString(i));
                Log.d("number_" + i, array.getString(i));
                sendMessage(message, array.getString(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("sms", "exception. cannot fetch data");
        }

    }

    public void sendMessage(String message, String phoneNo){
        SubscriptionManager subscriptionManager = SubscriptionManager.from(this);
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


    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}