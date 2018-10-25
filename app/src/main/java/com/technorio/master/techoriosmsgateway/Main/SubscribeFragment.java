package com.technorio.master.techoriosmsgateway.Main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.technorio.master.techoriosmsgateway.R;
import com.technorio.master.techoriosmsgateway.Utils.SharedPrefManager;

/**
 * Created by Ujjwal on 10/23/2018.
 */

public class SubscribeFragment extends Fragment{

    Button subscribe_new, subscribe_change;
    LinearLayout has_subscribed, not_subscribed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.subscribe_layout, null);

        has_subscribed = view.findViewById(R.id.has_subscribed);
        not_subscribed = view.findViewById(R.id.not_subscribed);

        String myTopic = SharedPrefManager.getInstance(getContext()).getSubsctiptionTopic();

        subscribe_change = view.findViewById(R.id.btn_subscribe_change);
        subscribe_new = view.findViewById(R.id.btn_subscribe_new);
        subscribe_change.setOnClickListener(dialogOpener);
        subscribe_new.setOnClickListener(dialogOpener);

        if(myTopic.isEmpty()){
            has_subscribed.setVisibility(View.INVISIBLE);
            not_subscribed.setVisibility(View.VISIBLE);

        }else{
            subscribe_change.setText(myTopic);
            has_subscribed.setVisibility(View.VISIBLE);
            not_subscribed.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private void showSubscribeDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.subscribe_dialog, null);

        final EditText topic = view.findViewById(R.id.topic);
        Button subscribe = view.findViewById(R.id.btn_subscribe);

        mBuilder.setView(view);
        final AlertDialog dialog = mBuilder.create();

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topic.getText().toString().isEmpty()){
                    topic.setError("Please enter a topic");
                    topic.requestFocus();
                }else{
                    FirebaseMessaging.getInstance().subscribeToTopic(topic.getText().toString());
                    SharedPrefManager.getInstance(getContext()).setSubscriptionTopic(topic.getText().toString());
                    subscribe_change.setText(SharedPrefManager.getInstance(getContext()).getSubsctiptionTopic());

                    has_subscribed.setVisibility(View.VISIBLE);
                    not_subscribed.setVisibility(View.INVISIBLE);

                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    View.OnClickListener dialogOpener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showSubscribeDialog();
        }
    };
}
