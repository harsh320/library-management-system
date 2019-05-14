package com.example.navigation.Admin;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.navigation.R;
import com.example.navigation.activity.BookDetail;
import com.example.navigation.others.sort_comp;
import com.example.navigation.others.webview;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class req_retrieve extends AppCompatActivity {

ListView listView;
    ArrayAdapter<String> arrayAdapter;
    String branch1;
    String branch[]={"CSE","MECHANICAL","ELECTRICAL","ECE","PRODUCTION","METALLURGY","AEROSPACE"};
    String url="https://library-48f63.firebaseio.com/uploads123.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_req_retrieve);
        listView=findViewById(R.id.reqret2);
        Intent intent=getIntent();

        branch1 = intent.getStringExtra("EXTRA_MESSAGE");

        showdata();
    }
    private void showdata() {
        final List<JSONObject> jsonObjects = new ArrayList<>();
        final StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Iterator<String> kl = jsonObject.keys();
                    while (kl.hasNext()) {
                        String key = String.valueOf(kl.next());
                        JSONObject child = jsonObject.getJSONObject(key);
                        if (child != null&&child.optString("branch").equalsIgnoreCase(branch1)) {
                            jsonObjects.add(child);
                        }
                    }
                    if(jsonObjects.size()==0)
                        Toast.makeText(getApplicationContext(),"No request",Toast.LENGTH_LONG).show();



                    JSONObject jsonObject1=null;
                    final ArrayList<String> list;
                    final ArrayList<String> list1;
                    final HashMap<String, Integer> counter = new HashMap<>();
                    final List<String> outputArray = new ArrayList<>();
                    list=new ArrayList<>();
                    list1=new ArrayList<>();
                    for (int i = 0; i < jsonObjects.size(); i++)
                    {
                        jsonObject1=jsonObjects.get(i);
                        String bookname=jsonObject1.optString("isbn");
                        list.add(bookname);
                    }

                   /* for (String str : list) {
                        counter.put(str, 1 + (counter.containsKey(str) ? counter.get(str) : 0));
                    }
                    final List<String> list2 = new ArrayList<String>(counter.keySet());
                    Collections.sort(list, new Comparator<String>() {
                        @Override
                        public int compare(String x, String y) {
                            return counter.get(x) - counter.get(y);
                        }
                    });
                    for (String i : list2) {
                        list1.add(i);
                    }
                    Collections.reverse(list1);*/

                    Map<String, Integer> map123 = new HashMap<>();
                    ArrayList<String> outputArray123 = new ArrayList<>();

                    // Assign elements and their count in the list and map
                    for (String current : list) {
                        int count = map123.getOrDefault(current, 0);
                        map123.put(current, count + 1);
                        outputArray123.add(current);
                    }

                    // Compare the map by value
                    SortComparator comp = new SortComparator(map123);

                    // Sort the map using Collections CLass
                    Set<String> hash_Set = new HashSet<String>();
                    Collections.sort(outputArray123, comp);
                    for(int i=0;i<outputArray123.size();i++)
                    {
                      hash_Set.add(outputArray123.get(i));
                    }
                    ArrayList<String> list456 = new ArrayList<>(hash_Set);


                    arrayAdapter=new ArrayAdapter<String>(req_retrieve.this, R.layout.book_list_item, R.id.book_name,list456);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String bookname= (String) parent.getItemAtPosition(position);
                            Intent intent=new Intent(view.getContext(), webview.class);
                            intent.putExtra("EXTRA_MESSAGE", "https://isbnsearch.org/isbn/"+bookname);
                            startActivity(intent);
                        }
                    });













                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    try {
                        JSONObject jsonObject = new JSONObject(error.getMessage());
                        Toast.makeText(req_retrieve.this, jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(req_retrieve.this, req_retrieve.this.getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(req_retrieve.this);
        requestQueue.add(stringRequest);

    }
    class SortComparator implements Comparator<String> {
        private final Map<String, Integer> freqMap;

        // Assign the specified map
        SortComparator(Map<String, Integer> tFreqMap)
        {
            this.freqMap = tFreqMap;
        }

        // Compare the values
        @Override
        public int compare(String k1, String k2)
        {

            // Compare value by frequency
            int freqCompare = freqMap.get(k2).compareTo(freqMap.get(k1));

            // Compare value if frequency is equal
            int valueCompare = k1.compareTo(k2);

            // If frequency is equal, then just compare by value, otherwise -
            // compare by the frequency.
            if (freqCompare == 0)
                return valueCompare;
            else
                return freqCompare;
        }}}

