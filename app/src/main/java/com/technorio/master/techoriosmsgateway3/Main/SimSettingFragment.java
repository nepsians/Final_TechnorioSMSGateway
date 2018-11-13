package com.technorio.master.techoriosmsgateway3.Main;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technorio.master.techoriosmsgateway3.R;
import com.technorio.master.techoriosmsgateway3.Utils.SharedPrefManager;

/**
 * Created by Ujjwal on 10/23/2018.
 */

public class SimSettingFragment extends Fragment {

    TextView sim1_operator, sim2_operator;
    LinearLayout sim1_layout, sim2_layout;
    AlertDialog.Builder builder;
    LinearLayout sim1_cardview, sim2_cardview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sim_setting_layout, null);

        sim1_operator = view.findViewById(R.id.sim1_operator);
        sim2_operator = view.findViewById(R.id.sim2_operator);

        sim1_layout = view.findViewById(R.id.sim1_layout);
        sim2_layout = view.findViewById(R.id.sim2_layout);

        sim1_cardview = view.findViewById(R.id.sim_first);
        sim2_cardview = view.findViewById(R.id.sim_second);

        sim1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sim1_selection();
            }
        });

         sim2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sim2_selection();
            }
        });



        setSimInfo();
        return view;
    }

    public void setSimInfo() {

        SubscriptionManager sManager = (SubscriptionManager) getContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        SubscriptionInfo infoSim1 = sManager.getActiveSubscriptionInfoForSimSlotIndex(0);
        SubscriptionInfo infoSim2 = sManager.getActiveSubscriptionInfoForSimSlotIndex(1);

        int simId = SharedPrefManager.getInstance(getContext()).getSimId();

        if (infoSim1 != null) {
            sim1_layout.setVisibility(View.VISIBLE);
            if (simId == 0) {
                sim1_operator.setText(infoSim1.getDisplayName() + " (Active)");
                sim1_cardview.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                sim1_operator.setText(infoSim1.getDisplayName());
                sim1_cardview.setBackgroundColor(getResources().getColor(R.color.white));
            }
        } else {
            sim1_layout.setVisibility(View.GONE);
        }

        if (infoSim2 != null) {
            sim2_layout.setVisibility(View.VISIBLE);
            sim2_operator.setText(infoSim2.getDisplayName());
            if (simId == 1) {
                sim2_operator.setText(infoSim2.getDisplayName() + " (Active)");
                sim2_cardview.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            } else {
                sim2_operator.setText(infoSim2.getDisplayName());
                sim2_cardview.setBackgroundColor(getResources().getColor(R.color.white));

            }
        } else {
            sim2_layout.setVisibility(View.GONE);
        }

    }

    public void sim1_selection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }

        if(sim2_layout.getVisibility()== View.VISIBLE) {
            builder.setTitle("Sim Selection").setMessage("Are you sure you want to select SIM1?").
                    setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPrefManager.getInstance(getContext()).setSimId(0);
                            setSimInfo();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }

    public void sim2_selection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }

        if(sim1_layout.getVisibility()== View.VISIBLE) {
            builder.setTitle("Sim Selection").setMessage("Are you sure you want to select SIM2?").
                    setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPrefManager.getInstance(getContext()).setSimId(1);
                            setSimInfo();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }

    }

}
