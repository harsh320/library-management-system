package com.example.navigation.fragment;

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
import android.widget.ImageView;
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
import com.example.navigation.activity.adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhotoFragment extends Fragment {
    TextView textView1,textView2,textView3,textView4,textView5;
    String url="https://library-48f63.firebaseio.com/uploads.json";
    FirebaseAuth firebaseAuth;
    ImageView imageView;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_photo,container,false);

        imageView=view.findViewById(R.id.photo3);
        textView1=view.findViewById(R.id.photo21);
        textView2=view.findViewById(R.id.photo22);
        textView3=view.findViewById(R.id.photo23);
        textView4=view.findViewById(R.id.photo24);
        textView5=view.findViewById(R.id.photo25);
        showdata();
        return view;
    }

    private void showdata()
    {
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
                            if (child != null) {
                                firebaseAuth=FirebaseAuth.getInstance();
                                FirebaseUser user=firebaseAuth.getCurrentUser();
                                String name1=user.getEmail();
                                String name2 =child.optString("email");
                                if(name2.equalsIgnoreCase(name1))
                                {
                                    String url = child.optString("path");
                                    Glide.with(getActivity()).load(url).into(imageView);
                                    String name=child.optString("name");
                                    textView1.setText(name);

                                    String sid=child.optString("sid");
                                    textView2.setText(sid);

                                    String email=child.optString("email");
                                    textView3.setText(email);

                                    String phone=child.optString("phone_number");
                                    textView4.setText(phone);

                                    String pref=child.optString("preference");
                                    textView5.setText(pref);



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
                        Toast.makeText(getActivity(), jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(getActivity(), getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
