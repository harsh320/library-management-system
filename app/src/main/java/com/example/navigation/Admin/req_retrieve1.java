package com.example.navigation.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.navigation.R;
import com.example.navigation.others.webview;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class req_retrieve1 extends AppCompatActivity implements Serializable {
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    String branch[]={"CSE","MECHANICAL","ELECTRICAL","ECE","PRODUCTION","METALLURGY","AEROSPACE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_retrieve1);
        listView=findViewById(R.id.reqret3);
        arrayAdapter=new ArrayAdapter<String>(req_retrieve1.this, R.layout.book_list_item, R.id.book_name,branch);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String branch1= (String) parent.getItemAtPosition(position);
                Intent intent=new Intent(view.getContext(), req_retrieve.class);
                intent.putExtra("EXTRA_MESSAGE", branch1);
                startActivity(intent);
            }
        });


    }
}
