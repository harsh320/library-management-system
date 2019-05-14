package com.example.navigation.activity;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.navigation.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class adapter2 extends RecyclerView.Adapter<adapter.myviewholder> {
    @NonNull
    List<JSONObject> mlist;
    Activity mactivity;
    static CardView cardView;



    public static class myviewholder extends RecyclerView.ViewHolder {
        TextView Text,Text1;

        public myviewholder(final View itemView) {
            super(itemView);
            this.Text = itemView.findViewById(R.id.text);
            this.Text1 = itemView.findViewById(R.id.text1);
            cardView=itemView.findViewById(R.id.card);

        }
    }

    public adapter2(List<JSONObject> mlist, Activity mactivity) {
        this.mlist = mlist;
        this.mactivity = mactivity;
    }

    public adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater =mactivity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.cardview, viewGroup, false);
        adapter.myviewholder myviewholder = new adapter.myviewholder(view);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.myviewholder holder, int position) {
        String url="";
        final int last=mlist.size()-1;
        int secondlast=mlist.size()-2;
        JSONObject userbin1=null;
        TextView textView = holder.Text;
        TextView textView1 = holder.Text1;
        userbin1 = mlist.get(position);
        String data = userbin1.optString("pref");
        //String data1 = userbin1.optString("bookname");
        textView.setText(data);
        textView1.setText("BOOK_NAME");
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    public void updateView(List<JSONObject> list) {
        mlist.clear();
        mlist.addAll(list);
        notifyDataSetChanged();
    }
}
