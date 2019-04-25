package com.example.navigation.lending;

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

import com.example.navigation.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class frag4 extends Fragment {
    RecyclerView recyclerView;
    View ChildView;
    int RecyclerViewItemPosition;
    RecyclerView.LayoutManager layoutManager;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag4, container, false);
        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }
}
