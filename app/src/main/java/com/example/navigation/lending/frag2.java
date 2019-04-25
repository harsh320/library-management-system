package com.example.navigation.lending;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class frag2 extends Fragment {

    String url2="https://library-48f63.firebaseio.com/uploads";
    String url= url2+".json";
    String url1;
    EditText editText;
    ArrayList<String> key123=new ArrayList<>();
    ArrayList<String> list=new ArrayList<>();
    FirebaseAuth firebaseAuth;
    adapter1 adapter1;
    RecyclerView recyclerView;
    Button button,button1;
    View ChildView;
    int RecyclerViewItemPosition;
    RecyclerView.LayoutManager layoutManager;

    final List<JSONObject> jsonObjects = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_show_lend_data, container, false);
        recyclerView=view.findViewById(R.id.lend_data1);

        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView=view.findViewById(R.id.lend_data1);
        recyclerView.setHasFixedSize(true);
        editText=view.findViewById(R.id.lend_data2);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter1 = new adapter1(new ArrayList<JSONObject>(),new ArrayList<String>(), getActivity());
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
        return view;



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
                                                Toast.makeText(getContext(), jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else if (volleyError instanceof NoConnectionError)
                                            Toast.makeText(getContext(), getActivity().getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
                                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                        Toast.makeText(getContext(), jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(getContext(),getActivity().getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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







    }

}