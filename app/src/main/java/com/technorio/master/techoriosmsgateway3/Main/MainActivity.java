package com.technorio.master.techoriosmsgateway3.Main;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.technorio.master.techoriosmsgateway3.LoginActivity;
import com.technorio.master.techoriosmsgateway3.R;
import com.technorio.master.techoriosmsgateway3.Utils.Constants;
import com.technorio.master.techoriosmsgateway3.Utils.SharedPrefManager;
import com.technorio.master.techoriosmsgateway3.Utils.TelephonyInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private TextView sim1TextView;
    private TextView sim2TextView;
    Boolean isSimSelected= SharedPrefManager.getInstance(this).isSimSelected();
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private String selectedFragment = "subscribeFragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //checkPermission();
        createNotificationChannel();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SubscribeFragment(), "root_fragment").commit();

        if (checkAndRequestPermissions()) {

            TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String getSimSerialNumber = telemamanger.getSimSerialNumber();
            String getSimNumber = telemamanger.getLine1Number();

            TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
            final SubscriptionManager subscriptionManager;
            final SubscriptionManager subscriptionManagerDetail;
            final SubscriptionManager subscriptionManagerSim1Detail;
            final SubscriptionManager subscriptionManagerSim2Detail;

            if (!isSimSelected) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    subscriptionManager = SubscriptionManager.from(this);

                    if ((subscriptionManager.getActiveSubscriptionInfoCountMax() == 1)) {
//                        Toast.makeText(this, "Single Sim.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onCreate: " + "Single Sim");
                    } else if (subscriptionManager.getActiveSubscriptionInfoCountMax() == 2) {
//                        Toast.makeText(this, "Dual Sim.", Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "onCreate: " + "Dual Sim");
                        if (telephonyInfo.isSIM1Ready() && telephonyInfo.isSIM2Ready()) {

//                            new AlertDialog.Builder(this)
//                                    .setMessage("Select Sim")
//                                    .setPositiveButton("SIM 1", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            prefManager.setSimId(0);
//                                            Toast.makeText(MainActivity.this, "Sim1 Selected.", Toast.LENGTH_SHORT).show();
//                                            Log.d(TAG, "onCreate: " + "SIM1 Selected");
//                                        }
//                                    })
//                                    .setNegativeButton("SIM 2", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            prefManager.setSimId(1);
//                                            Toast.makeText(MainActivity.this, "Sim2 Selected.", Toast.LENGTH_SHORT).show();
//                                            Log.d(TAG, "onCreate: " + "SIM2 Selected");
//                                        }
//                                    })
//                                    .setCancelable(false)
//                                    .create()
//                                    .show();


                            subscriptionManagerSim1Detail = SubscriptionManager.from(this);
                            SubscriptionInfo subscriptionInfoSim1 = subscriptionManagerSim1Detail.getActiveSubscriptionInfoForSimSlotIndex(0);
                            subscriptionManagerSim2Detail = SubscriptionManager.from(this);
                            SubscriptionInfo subscriptionInfoSim2 = subscriptionManagerSim2Detail.getActiveSubscriptionInfoForSimSlotIndex(1);


                            Log.d(TAG, "number sim1: " + subscriptionInfoSim1.getNumber());
                            Log.d(TAG, "number sim1: " + subscriptionInfoSim1.getCarrierName());
                            Log.d(TAG, "number sim1: " + subscriptionInfoSim1.getDisplayName());
                            Log.d(TAG, "number sim1: " + subscriptionInfoSim1.getMcc());
                            Log.d(TAG, "number sim1: " + subscriptionInfoSim1.getMnc());

                            Log.d(TAG, "number sim2: " + subscriptionInfoSim2.getNumber());

                            AlertDialog.Builder builder = new AlertDialog.Builder(this);

                            LayoutInflater inflater = this.getLayoutInflater();
                            View view = inflater.inflate(R.layout.dialog_sim_selection, null);


                            sim1TextView = view.findViewById(R.id.sim1TextView);
                            sim2TextView = view.findViewById(R.id.sim2TextView);

                            sim1TextView.setText(subscriptionInfoSim1.getDisplayName() + "\n" + "SIM 1");
                            sim2TextView.setText(subscriptionInfoSim2.getDisplayName() + "\n" + "SIM 2");




                            builder.setTitle("SELECT SIM");
                            builder.setView(view).setCancelable(false);
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                            sim1TextView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //  prefManager.setSimId(0);
                                    SharedPrefManager.getInstance(getApplicationContext()).setSimId(0);
                                    Toast.makeText(MainActivity.this, "Sim1 Selected.", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onCreate: " + "SIM1 Selected");
                                    alertDialog.dismiss();
                                }
                            });
                            sim2TextView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //prefManager.setSimId(1);
                                    SharedPrefManager.getInstance(getApplicationContext()).setSimId(1);
                                    Toast.makeText(MainActivity.this, "Sim2 Selected.", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onCreate: " + "SIM2 Selected");
                                    alertDialog.dismiss();
                                }
                            });


                        } else {
                            if (telephonyInfo.isSIM1Ready()) {
                                /*prefManager.setSimId(0);*/
                                SharedPrefManager.getInstance(getApplicationContext()).setSimId(0);
                                Toast.makeText(this, "Sim1 Selected.", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onCreate: " + "SIM1 Selected");
                            }
                            if (telephonyInfo.isSIM2Ready()) {
                                /*prefManager.setSimId(1);*/
                                SharedPrefManager.getInstance(getApplicationContext()).setSimId(1);
                                Toast.makeText(this, "Sim2 Selected.", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onCreate: " + "SIM2 Selected");
                            }
                        }

                    } else {
                        Toast.makeText(this, "Greater Than 2 Sim.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }}

    Boolean doubleBackToExitPressedOnce = false;


    @Override
    public void onBackPressed() {

        SubscribeFragment subscribeFragment = (SubscribeFragment) getSupportFragmentManager().findFragmentByTag("root_fragment");

        if(subscribeFragment != null && subscribeFragment.isVisible()) {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {

                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                doubleBackToExitPressedOnce = true;

                Toast.makeText(this, "Press once again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

            }
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SubscribeFragment(), "root_fragment").commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_subscription) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SubscribeFragment(), "root_fragment").commit();
        } else if (id == R.id.nav_history) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new MessageHistoryFragment()).commit();
        } else if (id == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AboutFragment()).commit();
        } else if (id == R.id.nav_logout) {
            SharedPrefManager.getInstance(getApplicationContext()).setUserStatus(false);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            //SharedPrefManager.getInstance(getApplicationContext()).clearSubscriptionTopic();
            finish();
        } else if (id == R.id.nav_sim_setting){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SimSettingFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createNotificationChannel() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);

            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableLights(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int phoneStatePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (phoneStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted

                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("SMS and Phone Permission is required for this app.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setCancelable(false)
                .create()
                .show();
    }
    
    


}
