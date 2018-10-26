package com.technorio.master.techoriosmsgateway.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.technorio.master.techoriosmsgateway.Main.MainActivity;
import com.technorio.master.techoriosmsgateway.R;
import com.technorio.master.techoriosmsgateway.Utils.Constants;

import java.util.ArrayList;

public class MyNotificationManager {

    private Context mCtx;
    private static MyNotificationManager mInstance;
    SmsManager smsManager = SmsManager.getDefault();

    private MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized  MyNotificationManager getmInstance(Context context){
        if(mInstance == null){
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String body, String message, ArrayList<String> numList){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(body);

        Intent intent = new Intent(mCtx, MainActivity.class);
        intent.putExtra("message", message);
        intent.putStringArrayListExtra("numberList", numList);

        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if(mNotificationManager != null){
            mNotificationManager.notify(1, mBuilder.build());
        }
        mBuilder.setAutoCancel(true);

        Log.d("start", "notification inside--------");
    }

}
