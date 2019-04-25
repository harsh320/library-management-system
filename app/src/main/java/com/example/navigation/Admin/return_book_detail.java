package com.example.navigation.Admin;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.navigation.lending.add_book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class return_book_detail extends AppCompatActivity {
     String key,fine,fine1;
     String url;
     EditText editText;
     ListView listView;
     String url9;
     int fi=0,fi2=0;
     String fi1;
     String url456="https://library-48f63.firebaseio.com/uploads.json";
     ArrayAdapter<String> arrayAdapter;
    AlertDialog.Builder builder;
     DatabaseReference databaseReference1,databaseReference2,databaseReference3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book_detail);
        Intent intent = getIntent();
        key = intent.getStringExtra("EXTRA_MESSAGE");
        fine = intent.getStringExtra("EXTRA_MESSAGE1");

        builder = new AlertDialog.Builder(this);
        url="https://library-48f63.firebaseio.com/uploads/"+key+"/book_issue.json";
        listView=findViewById(R.id.return_detail2);
        editText=findViewById(R.id.return_detail1);

        final List<JSONObject> jsonObjects = new ArrayList<>();
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    final JSONObject jsonObject = new JSONObject(response);
                    Iterator<String> kl = jsonObject.keys();
                    while (kl.hasNext()) {
                        String key = String.valueOf(kl.next());
                        JSONObject child = jsonObject.getJSONObject(key);
                        if (child != null) {
                            jsonObjects.add(child);
                        }

                    }


                    JSONObject jsonObject1 = null;
                    final ArrayList<String> list;
                    list = new ArrayList<>();
                    for (int i = 0; i < jsonObjects.size(); i++) {
                        jsonObject1 = jsonObjects.get(i);
                        String sid = jsonObject1.optString("bookname");
                        String total = sid ;
                        list.add(total);
                    }
                    arrayAdapter = new ArrayAdapter<String>(return_book_detail.this, R.layout.book_list_item, R.id.book_name, list);
                    listView.setAdapter(arrayAdapter);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            arrayAdapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                            builder.setMessage("Do you want to Return ?").setTitle("Library");
                            builder.setMessage("Do you want to return ?")
                                    .setCancelable(false)
                                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.O)
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {





                                                    StringRequest stringRequest=new StringRequest(Request.Method.GET, url456, new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                if (jsonObject != null) {
                                                                    Iterator<String> kl = jsonObject.keys();
                                                                    while (kl.hasNext()) {
                                                                        String key = String.valueOf(kl.next());
                                                                        JSONObject child = jsonObject.getJSONObject(key);
                                                                        FirebaseAuth firebaseAuth;
                                                                        firebaseAuth=FirebaseAuth.getInstance();
                                                                        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                                                                        String email=firebaseUser.getEmail();
                                                                        if (child != null) {
                                                                            if(child.optString("email").equalsIgnoreCase(fine))
                                                                            {
                                                                                fine1= child.optString("fine");



                                                                                String key45;
                                                                                JSONObject jsonObject123=null;
                                                                                jsonObject123=jsonObjects.get(position);
                                                                                url9="https://library-48f63.firebaseio.com/books/"+jsonObject123.optString("bookkey")+".json";
                                                                                showdata123();
                                                                                String date=jsonObject123.optString("date");

                                                                                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                                                                                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss", Locale.ENGLISH);
                                                                                final LocalDate firstDate = LocalDate.parse(timeStamp, formatter);
                                                                                final LocalDate secondDate = LocalDate.parse(date, formatter);
                                                                                long days = ChronoUnit.DAYS.between(secondDate, firstDate);
                                                                                Toast.makeText(return_book_detail.this,String.valueOf(days),Toast.LENGTH_LONG).show();
                                                                                days=days-15;
                                                                                if(days>=0)
                                                                                {
                                                                                    fi= (int) (fi+days);
                                                                                }
                                                                                fi2=Integer.parseInt(fine1);
                                                                                fi2=fi2+fi;

                                                                                fi1=String.valueOf(fi2);





                                                                                databaseReference1= FirebaseDatabase.getInstance().getReference("uploads/"+key+"/book_issue/"+jsonObject123.optString("key")+"/");
                                                                                databaseReference3=FirebaseDatabase.getInstance().getReference("uploads/"+key+"/fine/");
                                                                                databaseReference3.setValue(fi1);

                                                                                databaseReference1.setValue(null);
                                                                                Toast.makeText(return_book_detail.this,"Book Returned",Toast.LENGTH_LONG).show();

                                                                            }



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
                                            })
                                            .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    Toast.makeText(getApplicationContext(), "Request canceled",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.setTitle("Confirm :");
                            alert.show();
                            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);

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
                        Toast.makeText(return_book_detail.this, jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(return_book_detail.this, return_book_detail.this.getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(return_book_detail.this);
        requestQueue.add(stringRequest);




    }



    private void showdata123()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url9, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                                String cp=jsonObject.optString("copies");
                                int cp1=Integer.parseInt(cp);
                                cp1=cp1+1;
                                String x=String.valueOf(cp1);
                                databaseReference2=FirebaseDatabase.getInstance().getReference("books/"+jsonObject.optString("key")+"/");

                        databaseReference2.child("copies").setValue(x);


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
