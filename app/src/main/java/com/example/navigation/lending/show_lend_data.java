package com.example.navigation.lending;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.example.navigation.activity.adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class show_lend_data extends AppCompatActivity {
    String url2="https://library-48f63.firebaseio.com/uploads";
    String url= url2+".json";
    String url1;
    EditText editText;
    ArrayList<String>key123=new ArrayList<>();
    ArrayList<String> list=new ArrayList<>();
    FirebaseAuth firebaseAuth;
    adapter1 adapter1;
    RecyclerView recyclerView;
    Button button,button1;
    View ChildView;
    int RecyclerViewItemPosition;

    RecyclerView.LayoutManager layoutManager;

    final List<JSONObject> jsonObjects = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lend_data);
        recyclerView=findViewById(R.id.lend_data1);

        editText=findViewById(R.id.lend_data2);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter1 = new adapter1(new ArrayList<JSONObject>(),new ArrayList<String>(), show_lend_data.this);
        showdata();
        recyclerView.setAdapter(adapter1);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });



    }
    private void filter(String text) {
        ArrayList<JSONObject> filteredList = new ArrayList<>();
        ArrayList<String> filteredList1 = new ArrayList<>();

        for (int i=0;i<jsonObjects.size();i++) {
            if (jsonObjects.get(i).optString("name").toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(jsonObjects.get(i));
                filteredList1.add(key123.get(i));
            }
        }

        adapter1.filterList(filteredList,filteredList1);
    }
    private void showdata() {
        final StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Iterator<String> kl = jsonObject.keys();
                    while (kl.hasNext()) {
                        String key = String.valueOf(kl.next());
                        JSONObject child = jsonObject.getJSONObject(key);
                        if (child != null) {
                            if (child.optJSONObject("book_data") != null) {
                                url1 = url2 + "/" + key + "/book_data.json";
                                final String email = child.optString("email");


                                final StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Iterator<String> kl = jsonObject.keys();
                                            while (kl.hasNext()) {
                                                String key = String.valueOf(kl.next());
                                                JSONObject child = jsonObject.getJSONObject(key);
                                                if (child != null) {

                                                    jsonObjects.add(child);
                                                    key123.add(email);

                                                }
                                                adapter1.updateView(jsonObjects,key123);

                                            }




















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
                                                Toast.makeText(show_lend_data.this, jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else if (volleyError instanceof NoConnectionError)
                                            Toast.makeText(show_lend_data.this, show_lend_data.this.getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
                                    }
                                });
                                stringRequest1.setRetryPolicy(new RetryPolicy() {
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
                                RequestQueue requestQueue = Volley.newRequestQueue(show_lend_data.this);
                                requestQueue.add(stringRequest1);


                            }

                        }
                    }













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
                        Toast.makeText(show_lend_data.this, jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(show_lend_data.this, show_lend_data.this.getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(show_lend_data.this);
        requestQueue.add(stringRequest);







    }

}
