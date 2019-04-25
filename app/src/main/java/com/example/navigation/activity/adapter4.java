package com.example.navigation.activity;

import android.annotation.SuppressLint;
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

import com.example.navigation.Admin.return_book_detail;
import com.example.navigation.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class adapter4 extends RecyclerView.Adapter<adapter.myviewholder> {

        List<JSONObject> mlist;
        Activity mactivity;
static CardView cardView;




        public static class myviewholder extends RecyclerView.ViewHolder {
                TextView Text,Text1,Text3;

                public myviewholder(final View itemView) {
                        super(itemView);
                        this.Text = itemView.findViewById(R.id.text);
                        this.Text1 = itemView.findViewById(R.id.text1);
                        this.Text3 = itemView.findViewById(R.id.text2);
                        cardView=itemView.findViewById(R.id.card4);

                }
        }
        public adapter4(List<JSONObject> mlist, Activity mactivity) {
                this.mlist = mlist;
                this.mactivity = mactivity;
        }

        @NonNull
        @Override
        public adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater layoutInflater =mactivity.getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.card4, viewGroup, false);
                adapter.myviewholder myviewholder = new adapter.myviewholder(view);
                return myviewholder;
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull adapter.myviewholder holder, int position) {
                JSONObject userbin1=null;
                TextView textView = holder.Text;
                TextView textView1 = holder.Text1;
                userbin1 = mlist.get(position);
                String data = userbin1.optString("bookname");
                @SuppressLint("SimpleDateFormat") String timeStamp1 = userbin1.optString("date");
                textView1.setText(data);
                textView.setText("Issue Date and time (yyyy.mm.dd.hh.mm.ss): "+timeStamp1);
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
