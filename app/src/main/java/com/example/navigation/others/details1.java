package com.example.navigation.others;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.navigation.activity.adapter4;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class details1 extends AppCompatActivity {
    String url1="https://library-48f63.firebaseio.com/uploads.json";
    String bookkey;
    String url;
    TextView textView;
    String email="";
    com.example.navigation.activity.adapter4 adapter4;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details1);
        Intent intent=getIntent();
        email=intent.getStringExtra("EXTRA_MESSAGE1");
        recyclerView=findViewById(R.id.Issuelist);
        textView=findViewById(R.id.fine);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter4 = new adapter4(new ArrayList<JSONObject>(), this);
        showdata();
        recyclerView.setAdapter(adapter4);
    }
    private void showdata()
    {




        StringRequest stringRequest=new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        Iterator<String> kl = jsonObject.keys();
                        while (kl.hasNext()) {
                            String key = String.valueOf(kl.next());
                            JSONObject child = jsonObject.getJSONObject(key);
                            if (child != null) {
                                if(child.optString("email").equalsIgnoreCase(email))
                                {
                                    bookkey=child.optString("key");
                                    url="https://library-48f63.firebaseio.com/uploads/"+bookkey+"/book_issue.json";
                                    textView.setText("Total Fine : ₹"+child.optString("fine"));
                                }

                            }



                            StringRequest stringRequest1=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    List<JSONObject> jsonObjects = new ArrayList<>();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject != null) {
                                            Iterator<String> kl = jsonObject.keys();
                                            while (kl.hasNext()) {
                                                String key = String.valueOf(kl.next());
                                                JSONObject child = jsonObject.getJSONObject(key);
                                                if (child != null) {
                                                    jsonObjects.add(child);
                                                }
                                                adapter4.updateView(jsonObjects);
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
                                            Toast.makeText(getApplicationContext(), jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (volleyError instanceof NoConnectionError)
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            requestQueue1.add(stringRequest1);








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
                        Toast.makeText(getApplicationContext(), jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);






    }
}
