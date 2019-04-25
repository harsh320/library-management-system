package com.example.navigation.lending;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class frag1 extends Fragment {
    EditText editText1,editText2,editText3,editText4;
    Button button;
    String key123;
    String url="https://library-48f63.firebaseio.com/uploads.json";
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_add_book, container, false);
        editText1=view.findViewById(R.id.lend11);
        editText2=view.findViewById(R.id.lend12);
        editText3=view.findViewById(R.id.lend13);
        editText4=view.findViewById(R.id.lend14);
        button=view.findViewById(R.id.lend15);


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
                        Toast.makeText(getContext(), jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(getContext(), getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db;
                db = FirebaseDatabase.getInstance().getReference("uploads/" + key123 + "/book_data/");
                String k = db.push().getKey();


                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading");
                progressDialog.show();
                add_book_class add_book_class=new add_book_class(editText1.getText().toString(),editText2.getText().toString(),editText3.getText().toString(),editText4.getText().toString(),k);
                db.child(k).setValue(add_book_class);
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Book added", Toast.LENGTH_LONG).show();
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
                editText4.setText("");

            }
        });




   return view;
    }

}