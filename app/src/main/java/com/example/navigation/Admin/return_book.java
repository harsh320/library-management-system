package com.example.navigation.Admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.bumptech.glide.Glide;
import com.example.navigation.R;
import com.example.navigation.activity.BookDetail;
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

public class return_book extends AppCompatActivity {
    ListView listView;
    EditText editText;
    ArrayAdapter<String> arrayAdapter;
    String key;
    String url="https://library-48f63.firebaseio.com/uploads.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        editText=findViewById(R.id.return1);
        listView=findViewById(R.id.return2);
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
                        String sid = jsonObject1.optString("sid");
                        String total = sid ;
                        list.add(total);
                    }
                    arrayAdapter = new ArrayAdapter<String>(return_book.this, R.layout.book_list_item, R.id.book_name, list);
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
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String bookname= (String) parent.getItemAtPosition(position);
                            Intent intent=new Intent(view.getContext(), return_book_detail.class);
                            JSONObject jsonObject3=null;
                            jsonObject3=jsonObjects.get(position);

                            intent.putExtra("EXTRA_MESSAGE",jsonObject3.optString("key"));
                            intent.putExtra("EXTRA_MESSAGE1",jsonObject3.optString("email"));
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
                        Toast.makeText(return_book.this, jsonObject.optString("error"), Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(return_book.this, return_book.this.getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(return_book.this);
        requestQueue.add(stringRequest);
    }







}
