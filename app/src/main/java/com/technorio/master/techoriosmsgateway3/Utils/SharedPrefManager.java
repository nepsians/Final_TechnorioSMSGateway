package com.technorio.master.techoriosmsgateway3.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ujjwal on 10/23/2018.
 */

public class SharedPrefManager {

    private static Context mCtx;
    private static SharedPrefManager mInstance;
    private static final String KEY_USER_STATUS= "userStatus";
    private static final String KEY_SUBSC_TOPIC= "subscriptionTopic";
    private static final String PREF_NAME = "SelectSim";
    private static final String SELECTED_SIM_ID = "selected_sim_id";
    private static final String IS_SIM_SELECTED = "is_sim_selected";
    private static final String TOPIC_NAME = "topic_name";
    private static final String TOPIC_NAME_SET = "topic_name_set";


    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void storeTitle(String title){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_TITLE, title);
        editor.apply();
    }

    public String getTitle(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.KEY_TITLE, null);
    }

    public void setUserStatus(Boolean userStatus){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_USER_STATUS, userStatus);
        editor.apply();
    }

    public Boolean getUserStatus(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_USER_STATUS, false);
    }

    public void setSubscriptionTopic(String topic){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SUBSC_TOPIC, topic);
        editor.apply();
    }

    public String getSubsctiptionTopic(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SUBSC_TOPIC, "");

    }



    //SIM KO KURA HARU SURU HUNCHA



    public void setSimId(int simId){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SELECTED_SIM_ID, simId);
        editor.putBoolean(IS_SIM_SELECTED,true);
        editor.apply();

      /*  editor.putInt(SELECTED_SIM_ID, simId);
        editor.putBoolean(IS_SIM_SELECTED, true);
*/
        // commit changes
        editor.commit();

    }
    public int getSimId(){
        /*return pref.getInt(SELECTED_SIM_ID,0);*/
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SELECTED_SIM_ID,0);
    }

    public boolean isSimSelected(){
/*
        return pref.getBoolean(IS_SIM_SELECTED,false);
*/
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_SIM_SELECTED,false);
    }


}
