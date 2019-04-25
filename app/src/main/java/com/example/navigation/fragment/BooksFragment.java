package com.example.navigation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BooksFragment extends Fragment {

    String url="https://library-48f63.firebaseio.com/books.json";
    ListView listView;
    EditText editText;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String>key123;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_book,container,false);
        listView=view.findViewById(R.id.book);
        editText=view.findViewById(R.id.searchView);
        showdata();
        return view;
    }

    private void showdata() {
        final List<JSONObject> jsonObjects = new ArrayList<>();
        key123=new ArrayList<String>();
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
                            jsonObjects.add(child);
                            key123.add(key);
                        }

                    }



                    JSONObject jsonObject1=null;
                    final ArrayList<String> list;
                    final ArrayList<String> author1=new ArrayList<>() ;
                    final ArrayList<String> genre3=new ArrayList<>();

                    list=new ArrayList<>();
                    for (int i = 0; i < jsonObjects.size(); i++)
                    {
                        jsonObject1=jsonObjects.get(i);

                        author1.add(jsonObject1.optString("author"));
                        genre3.add(jsonObject1.optString("genre"));
                        String bookname=jsonObject1.optString("name")+"(- "+jsonObject1.optString("author")+")";
                        list.add(bookname);
                    }
                    arrayAdapter=new ArrayAdapter<String>(getContext(), R.layout.book_list_item, R.id.book_name,list);
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
                            String author,location,isbn,genre;
                            JSONObject j;
                            j=jsonObjects.get(position);
                            author=j.optString("author");
                            location=j.optString("location");
                            isbn=j.optString("isbn");
                            genre=j.optString("genre");
                            Intent intent=new Intent(view.getContext(), BookDetail.class);
                            intent.putExtra("EXTRA_MESSAGE", bookname);
                            intent.putExtra("EXTRA_MESSAGE2", author);
                            intent.putExtra("EXTRA_MESSAGE3", location);
                            intent.putExtra("EXTRA_MESSAGE4", genre);
                            intent.putExtra("EXTRA_MESSAGE5", isbn);
                            intent.putExtra("EXTRA_MESSAGE1", key123.get(position));
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
                        Toast.makeText(getContext(), jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (volleyError instanceof NoConnectionError)
                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
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
