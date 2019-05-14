package com.example.navigation.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.navigation.Admin.add_new_book;
import com.example.navigation.R;
import com.example.navigation.activity.adapter;
import com.example.navigation.activity.adapter2;
import com.example.navigation.lending.add_book;
import com.example.navigation.lending.add_book_class;
import com.example.navigation.lending.show_lend_data;
import com.example.navigation.others.ImageAdapter;
import com.example.navigation.others.Remove_user_book;
import com.example.navigation.others.suggestion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment {

    String genre;
    String url1="https://library-48f63.firebaseio.com/uploads.json";
    String url="https://library-48f63.firebaseio.com/books.json";
     String urlp="https://library-48f63.firebaseio.com/books.json";
    adapter adapter;
    adapter2 adapter2;
    RecyclerView recyclerView,recyclerView1;
    Button button,button1,button2,button3;
    View ChildView;
    int RecyclerViewItemPosition;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManager1;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home,container,false);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPage);
        ImageAdapter adapterView = new ImageAdapter(getContext());
        mViewPager.setAdapter(adapterView);
        button2=view.findViewById(R.id.but1);
        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView1=view.findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new adapter(new ArrayList<JSONObject>(), getActivity());
        showdata();
        recyclerView.setAdapter(adapter);
        layoutManager1=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        adapter2 =new adapter2(new ArrayList<JSONObject>(), getActivity());
        showdata1();
        recyclerView1.setAdapter(adapter2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // Creating alert Dialog with one Button
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(),R.style.Theme_AppCompat);

                    //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Suggestion Box");

                    // Setting Dialog Message
                    alertDialog.setMessage("If you have any suggestions to improve our system please feel free to send us through this Suggestion Box                                                             Write here:-");
                final EditText input = new EditText(getContext());

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                input.setLayoutParams(lp);
                input.setTextColor(Color.WHITE);
                alertDialog.setView(input);
                    alertDialog.setPositiveButton("SUBMIT",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    String x=input.getText().toString();

                                    DatabaseReference db;
                                    db = FirebaseDatabase.getInstance().getReference("Suggestions/");
                                    String k = db.push().getKey();


                                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                    progressDialog.setTitle("Uploading");
                                    progressDialog.show();
                                   suggestion suggestion=new suggestion(x);
                                    db.child(k).setValue(suggestion);
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Successful", Toast.LENGTH_LONG).show();
                                }
                            });
                    alertDialog.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
        });

        return view;
    }

    private void showdata() {




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
                            FirebaseAuth firebaseAuth;
                            firebaseAuth=FirebaseAuth.getInstance();
                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                            String email=firebaseUser.getEmail();
                            if (child != null) {
                                if(child.optString("email").equalsIgnoreCase(email))
                                {
                                    genre=child.optString("preference");
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
                                                    if(child.optString("genre").equals(genre))
                                                        jsonObjects.add(child);
                                                }
                                                adapter.updateView(jsonObjects);
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
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getActivity());
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
    private void showdata1() {



        StringRequest stringRequest=new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final List<JSONObject> jsonObjects = new ArrayList<>();
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
                                if(child.optString("email").equalsIgnoreCase(email))
                                {
                                    JSONObject jsonObject1=child.getJSONObject("pref");

                                    if(jsonObject1!=null)
                                    {
                                        Iterator<String> k3=jsonObject1.keys();
                                        while (k3.hasNext())
                                        {
                                            String key3=String.valueOf(k3.next());
                                            JSONObject child3=jsonObject1.getJSONObject(key3);
                                            if(child3 !=null)
                                            {
                                                jsonObjects.add(child3);
                                            }
                                            adapter2.updateView(jsonObjects);
                                        }
                                    }












                                }


                                final List<JSONObject> jsonObjects1 = new ArrayList<>();
                                final StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Iterator<String> kl = jsonObject.keys();
                                            while (kl.hasNext()) {
                                                String key = String.valueOf(kl.next());
                                                JSONObject child6 = jsonObject.getJSONObject(key);
                                                if (child6 != null&&(child6.optString("genre")==jsonObjects.get((jsonObjects.size()-1)).optString("pref")||child6.optString("genre")==jsonObjects.get(jsonObjects.size()-2).optString("pref"))) {
                                                    jsonObjects1.add(child6);
                                                }
                                                adapter2.updateView(jsonObjects1);

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
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
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
                                                    if(child.optString("genre").equals(genre))
                                                        jsonObjects.add(child);
                                                }
                                                adapter.updateView(jsonObjects);
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
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getActivity());
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
