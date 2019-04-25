package com.example.navigation.lending;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.navigation.others.book_data_added_user;
import com.example.navigation.others.issue_list;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class add_book extends AppCompatActivity {
    EditText editText1,editText2,editText3,editText4;
    Button button;
    String key123;
    String url="https://library-48f63.firebaseio.com/uploads.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        editText1=findViewById(R.id.lend11);
        editText2=findViewById(R.id.lend12);
        editText3=findViewById(R.id.lend13);
        editText4=findViewById(R.id.lend14);
        button=findViewById(R.id.lend15);


        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                if(child.optString("email").equals(email))
                                    key123=key;
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
                        Toast.makeText(add_book.this, jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(add_book.this, getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(add_book.this);
        requestQueue.add(stringRequest);





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db;
                db = FirebaseDatabase.getInstance().getReference("uploads/" + key123 + "/book_data/");
                String k = db.push().getKey();


                final ProgressDialog progressDialog = new ProgressDialog(add_book.this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();
                add_book_class add_book_class=new add_book_class(editText1.getText().toString(),editText2.getText().toString(),editText3.getText().toString(),editText4.getText().toString(),k);
                db.child(k).setValue(add_book_class);
                progressDialog.dismiss();
                Toast.makeText(add_book.this, "Book added", Toast.LENGTH_LONG).show();

            }
        });





    }







    }

