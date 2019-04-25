package com.example.navigation.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.example.navigation.R;
import com.example.navigation.fragment.BooksFragment;
import com.example.navigation.others.add_book_data;
import com.example.navigation.others.add_issue_data;
import com.example.navigation.others.webview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookDetail extends AppCompatActivity {
    TextView textView1,textView2,textView3,textView4,textView5;
    Button button,button1;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String name,author,location,genre,isbn;
    String key1;
    String sid;
    String key123;
    String url456;
    AlertDialog.Builder builder;
    int success=0;
    String url="https://library-48f63.firebaseio.com/uploads.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        textView1=findViewById(R.id.detail1);
        textView2=findViewById(R.id.detail2);
        textView3=findViewById(R.id.detail3);
        textView4=findViewById(R.id.detail4);
        textView5=findViewById(R.id.detail5);
        button=findViewById(R.id.detail11);
        button1=findViewById(R.id.detail10);

        Intent intent = getIntent();
        name = intent.getStringExtra("EXTRA_MESSAGE");
        author = intent.getStringExtra("EXTRA_MESSAGE2");
        location = intent.getStringExtra("EXTRA_MESSAGE3");
        genre = intent.getStringExtra("EXTRA_MESSAGE4");
        isbn = intent.getStringExtra("EXTRA_MESSAGE5");
        key123 = intent.getStringExtra("EXTRA_MESSAGE1");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookDetail.this, webview.class);

                intent.putExtra("EXTRA_MESSAGE","https://isbnsearch.org/search?s="+name);
                startActivity(intent);
            }
        });

        url456="https://library-48f63.firebaseio.com/books/"+key123+".json";
        textView1.setText(name);
        textView2.setText(author);
        textView3.setText(location);
        textView4.setText(genre);
        textView5.setText(isbn);
        builder = new AlertDialog.Builder(this);

        showdata();
















        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Do you want to issue ?").setTitle("Library");
                builder.setMessage("Do you want to issue ?")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                showdata1();
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

            }});}

    private void upload() throws IOException {
        if(name!=null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(BookDetail.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            databaseReference = FirebaseDatabase.getInstance().getReference("issuedata");
            String k=databaseReference.push().getKey();
            firebaseAuth= FirebaseAuth.getInstance();
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            String name123=user.getEmail();
            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            add_issue_data add_issue_data=new add_issue_data(name123,name,timeStamp,isbn,k,key1,sid,key123,genre);
            databaseReference.child(k).setValue(add_issue_data);
            progressDialog.dismiss();
        }
        else
        {
            Toast.makeText(BookDetail.this,"failed",Toast.LENGTH_LONG).show();
        }

    }


    private void showdata()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                firebaseAuth=FirebaseAuth.getInstance();
                                FirebaseUser user=firebaseAuth.getCurrentUser();
                                assert user != null;
                                if(child.optString("email").equals(user.getEmail()))
                                {
                                key1=key;
                                sid=child.optString("sid");


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
                        Toast.makeText(BookDetail.this, jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(BookDetail.this, getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(BookDetail.this);
        requestQueue.add(stringRequest);
    }
    private void showdata1()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url456, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {

                            if (jsonObject != null) {
                                if(Integer.parseInt(jsonObject.optString("copies")) > 0)
                                {
                                    Toast.makeText(BookDetail.this,"book issue request sent",Toast.LENGTH_LONG).show();
                                    upload();

                                }

                            }

                        }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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
                        Toast.makeText(BookDetail.this, jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(BookDetail.this, getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(BookDetail.this);
        requestQueue.add(stringRequest);
    }
}
