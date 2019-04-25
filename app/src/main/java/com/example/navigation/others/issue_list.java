package com.example.navigation.others;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.navigation.activity.BookDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class issue_list extends AppCompatActivity {

    String url="https://library-48f63.firebaseio.com/issuedata.json";
    String url123="https://library-48f63.firebaseio.com/books.json";
    ListView listView;
    EditText editText;
    ArrayAdapter<String> arrayAdapter;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    FirebaseAuth firebaseAuth;
    String bookkey,userkey,issuebookkey;
    AlertDialog.Builder builder;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_list);
        listView = findViewById(R.id.issue2);
        editText = findViewById(R.id.issue3);

        builder = new AlertDialog.Builder(this);


        final List<JSONObject> jsonObjects = new ArrayList<>();
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                        }

                    }


                    JSONObject jsonObject1 = null;
                    final ArrayList<String> list;
                    list = new ArrayList<>();
                    for (int i = 0; i < jsonObjects.size(); i++) {
                        jsonObject1 = jsonObjects.get(i);
                        String sid = jsonObject1.optString("sid");
                        String bookname = jsonObject1.optString("bookname");
                        String total = sid + "(" + bookname + ")";
                        list.add(total);
                    }
                    arrayAdapter = new ArrayAdapter<String>(issue_list.this, R.layout.book_list_item, R.id.book_name, list);
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

                            builder.setMessage("Do you want to issue ?").setTitle("Library");
                            builder.setMessage("Do you want to issue ?")
                                    .setCancelable(false)
                                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {






                                            JSONObject jsonObject11=null;
                                            jsonObject11=jsonObjects.get(position);
                                            bookkey=jsonObject11.optString("bookkey");
                                            userkey=jsonObject11.optString("key1");
                                            issuebookkey=jsonObject11.optString("key");


                                            if(userkey!=null) {
                                                final int[] a = {0};


                                                final JSONObject finalJsonObject1 = jsonObject11;
                                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url123, new Response.Listener<String>() {
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
                                                                        firebaseAuth = FirebaseAuth.getInstance();
                                                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                                                        assert user != null;
                                                                        if (key.equalsIgnoreCase(bookkey)) {
                                                                            if (Integer.parseInt(child.optString("copies")) > 0) {
                                                                                DatabaseReference db;
                                                                                db = FirebaseDatabase.getInstance().getReference("books/" + bookkey + "/");
                                                                                String k = db.push().getKey();
                                                                                String count123;
                                                                                count123 = Integer.toString(Integer.parseInt(child.optString("copies")) - 1);


                                                                                db.child("copies").setValue(count123);


                                                                                final ProgressDialog progressDialog = new ProgressDialog(issue_list.this);
                                                                                progressDialog.setTitle("Uploading");
                                                                                progressDialog.show();
                                                                                databaseReference = FirebaseDatabase.getInstance().getReference("uploads/" + userkey + "/book_issue/");
                                                                                databaseReference2=FirebaseDatabase.getInstance().getReference("uploads/"+userkey+"/pref/");

                                                                                String k1 = databaseReference.push().getKey();
                                                                                String k2 = databaseReference2.push().getKey();
                                                                                firebaseAuth = FirebaseAuth.getInstance();
                                                                                final FirebaseUser user1 = firebaseAuth.getCurrentUser();
                                                                                String name = user.getEmail();
                                                                                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                                                                                book_data_added_user book_data_added_user = new book_data_added_user(finalJsonObject1.optString("bookname"), finalJsonObject1.optString("isbn"), timeStamp,k1,finalJsonObject1.optString("bookkey"));
                                                                                databaseReference.child(k1).setValue(book_data_added_user);
                                                                                pref pref=new pref(child.optString("genre"));
                                                                                databaseReference2.child(k2).setValue(pref);
                                                                                progressDialog.dismiss();
                                                                                Toast.makeText(issue_list.this, "Book Issued", Toast.LENGTH_LONG).show();


                                                                                databaseReference1=FirebaseDatabase.getInstance().getReference("issuedata/"+issuebookkey+"/");
                                                                                databaseReference1.setValue(null);






                                                                            }
                                                                            else
                                                                                Toast.makeText(issue_list.this,"Required book not there",Toast.LENGTH_LONG).show();
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
                                                                Toast.makeText(issue_list.this, jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else if (volleyError instanceof NoConnectionError)
                                                            Toast.makeText(issue_list.this, getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
                                                RequestQueue requestQueue = Volley.newRequestQueue(issue_list.this);
                                                requestQueue.add(stringRequest);

                                            }
                                            else
                                            {
                                                Toast.makeText(issue_list.this,"failed",Toast.LENGTH_LONG).show();
                                            }




                                            Toast.makeText(getApplicationContext(), "success",
                                                    Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(issue_list.this, jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(issue_list.this, issue_list.this.getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(issue_list.this);
        requestQueue.add(stringRequest);
    }






















}






















