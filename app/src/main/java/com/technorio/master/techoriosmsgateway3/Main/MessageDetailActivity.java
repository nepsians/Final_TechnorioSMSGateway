package com.technorio.master.techoriosmsgateway3.Main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.technorio.master.techoriosmsgateway3.R;
import com.technorio.master.techoriosmsgateway3.Utils.DatabaseHelper;

import java.util.ArrayList;

public class MessageDetailActivity extends AppCompatActivity {

    TextView message;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        message = findViewById(R.id.message);
        listView = findViewById(R.id.number_list);

        Intent i = getIntent();
        int message_id = i.getIntExtra("message_id", 0);

        ArrayList<String> number_list = new ArrayList<>();
        number_list = new DatabaseHelper(MessageDetailActivity.this).getNumberList(message_id);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, number_list);

        listView.setAdapter(itemsAdapter);

        message.setText(new DatabaseHelper(MessageDetailActivity.this).getMessageById(message_id));

    }




}
