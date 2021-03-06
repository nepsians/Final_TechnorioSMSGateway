package com.technorio.master.techoriosmsgateway3.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.technorio.master.techoriosmsgateway3.Model.Message;
import com.technorio.master.techoriosmsgateway3.Model.Sms;
import com.technorio.master.techoriosmsgateway3.R;
import com.technorio.master.techoriosmsgateway3.Utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ujjwal on 10/23/2018.
 */

public class MessageHistoryFragment extends Fragment {

    ListView listView;

    LinearLayout no_record_layout;
    LinearLayout record_layout;
    SwipeRefreshLayout pullToRefresh;

    ArrayList<Message> messageList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.message_history, null);
        listView = view.findViewById(R.id.messageList);

        no_record_layout = view.findViewById(R.id.no_record_layout);
        record_layout = view.findViewById(R.id.record_layout);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);

        messageList = new DatabaseHelper(getContext()).getMessageList();

        if (messageList.size() <= 0) {
            record_layout.setVisibility(View.GONE);
            no_record_layout.setVisibility(View.VISIBLE);

        } else {
            record_layout.setVisibility(View.VISIBLE);
            no_record_layout.setVisibility(View.GONE);
        }
        listView.setAdapter(new MessageListAdapter(getContext(), messageList));

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                pullToRefresh.setRefreshing(false);

            }
        });

        return view;
    }

    public class MessageListAdapter extends ArrayAdapter<Message> {

        Context context;
        ArrayList<Message> messageList;

        public MessageListAdapter(@NonNull Context context, ArrayList<Message> messageList) {
            super(context, 0, messageList);
            this.context = context;
            this.messageList = messageList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = LayoutInflater.from(context).inflate(R.layout.sms_item_layout, null);

            TextView message = view.findViewById(R.id.message);
            TextView message_no = view.findViewById(R.id.message_no);
            TextView message_date = view.findViewById(R.id.message_date);


            final Message msg = messageList.get(position);
            message.setText(msg.getMessage());
            message_no.setText(new DatabaseHelper(getContext()).getSMSNoByMessageId(msg.getId()) + " messages sent");
            message_date.setText(msg.getDate().substring(5));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), MessageDetailActivity.class);
                    i.putExtra("message_id", msg.getId());
                    startActivity(i);
                }
            });

            return view;
        }
    }

    private void refreshData() {

        messageList = new DatabaseHelper(getContext()).getMessageList();

        if (messageList.size() <= 0) {
            record_layout.setVisibility(View.GONE);
            no_record_layout.setVisibility(View.VISIBLE);

        } else {
            record_layout.setVisibility(View.VISIBLE);
            no_record_layout.setVisibility(View.GONE);
        }
        listView.setAdapter(new MessageListAdapter(getContext(), messageList));
    }

}
