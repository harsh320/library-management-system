package com.example.navigation.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class removetempreq extends AppCompatActivity {
String url="https://library-48f63.firebaseio.com/issuedata.json";
    DatabaseReference databaseReference1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removetempreq);


showdata();

    }
     void showdata() {
         final ProgressDialog progress = new ProgressDialog(this);
         progress.setTitle("Loading");
         progress.setMessage("Wait while loading...");
         progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
         progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
         progress.show();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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

                        }
                    }

                    for(int i=0;i<jsonObjects.size();i++)
                    {
                        String date=jsonObjects.get(i).optString("time");
                        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss");
                        final LocalDate firstDate = LocalDate.parse(timeStamp, formatter);
                        final LocalDate secondDate = LocalDate.parse(date, formatter);
                        long days = ChronoUnit.DAYS.between(secondDate, firstDate);
                       if(days>2)
                       {
                           databaseReference1= FirebaseDatabase.getInstance().getReference("issuedata/"+jsonObjects.get(i).optString("key"));

                           databaseReference1.setValue(null);
                           Toast.makeText(removetempreq.this,"List Updated",Toast.LENGTH_LONG).show();
                       }
                    }
                    Intent intent=new Intent(removetempreq.this,Admin.class);
                    startActivity(intent);
                    progress.dismiss();


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
