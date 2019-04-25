package com.example.navigation.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.navigation.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;
public class adapter extends RecyclerView.Adapter<adapter.myviewholder> {
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

    public adapter(List<JSONObject> mlist, Activity mactivity) {
        this.mlist = mlist;
        this.mactivity = mactivity;
    }

    @NonNull
    @Override
    public adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =mactivity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.cardview, parent, false);
        myviewholder myviewholder = new myviewholder(view);
        return myviewholder;

    }

    @Override
    public void onBindViewHolder(@NonNull final adapter.myviewholder holder, int position)
    {  JSONObject userbin1=null;
        TextView textView = holder.Text;
        TextView textView1 = holder.Text1;
            userbin1 = mlist.get(position);
            String data = userbin1.optString("name");
            String data1 = "- "+userbin1.optString("author");
            //String data1 = userbin1.optString("author");
            textView.setText(data1);
            textView1.setText(data);



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
