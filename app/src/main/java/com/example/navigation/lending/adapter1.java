package com.example.navigation.lending;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.navigation.R;
import com.example.navigation.activity.adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.List;

public class adapter1  extends RecyclerView.Adapter<adapter1.myviewholder>{
    List<JSONObject> mlist;
    List<String> mlist1;
    Activity mactivity;
    static CardView cardView;

    public static class myviewholder extends RecyclerView.ViewHolder {
        TextView Text,Text1;

        public myviewholder(final View itemView) {
            super(itemView);
            this.Text = itemView.findViewById(R.id.text);
            this.Text1 = itemView.findViewById(R.id.text1);
            cardView=itemView.findViewById(R.id.card4);

        }
    }

    public adapter1(List<JSONObject> mlist,List<String>mlist1, Activity mactivity) {
        this.mlist = mlist;
        this.mlist1=mlist1;
        this.mactivity = mactivity;
    }


    @NonNull
    @Override
    public adapter1.myviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater =mactivity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.card4, viewGroup, false);
        adapter1.myviewholder myviewholder = new adapter1.myviewholder(view);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, final int position) {
        JSONObject userbin1=null;
        TextView textView = holder.Text;
        TextView textView1 = holder.Text1;
        userbin1 = mlist.get(position);
        final String data = userbin1.optString("name");
        final String email=mlist1.get(position);
        textView.setText(email+" (Click to send Email to Seller)");
        textView1.setText(data);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to=email;
                FirebaseAuth firebaseAuth;
                firebaseAuth=FirebaseAuth.getInstance();
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                String email1=firebaseUser.getEmail();
                String subject="Lend a Book";
                String message="If "+data+" book is available please reply back1 to"+email1;


                Intent email2 = new Intent(Intent.ACTION_SEND);
                email2.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                email2.putExtra(Intent.EXTRA_SUBJECT, subject);
                email2.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email2.setType("message/rfc822");

                mactivity.startActivity(Intent.createChooser(email2, "Choose an Email client :"));

            }
        });



    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }
    public void updateView(List<JSONObject> list,List<String> list1) {
        mlist.clear();
        mlist1.clear();
        mlist.addAll(list);
        mlist1.addAll(list1);
        notifyDataSetChanged();
    }
    public void filterList(List<JSONObject> list,List<String> list1) {
        mlist.clear();
        mlist1.clear();
        mlist.addAll(list);
        mlist1.addAll(list1);
        notifyDataSetChanged();
    }

}