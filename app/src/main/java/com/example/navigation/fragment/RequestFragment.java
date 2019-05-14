package com.example.navigation.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.navigation.R;
import com.example.navigation.activity.MainActivity;
import com.example.navigation.activity.UserProfile;
import com.example.navigation.activity.add_user_data;
import com.example.navigation.others.add_book_data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;

public class RequestFragment extends Fragment {
    @Nullable
    EditText editText1,editText2,editText3;
    Button button;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Spinner spin;
    String branch[]={"CSE","MECHANICAL","ELECTRICAL","ECE","PRODUCTION","METALLURGY","AEROSPACE"};
     String a,b,c;
     FirebaseAuth firebaseAuth;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_request,container,false);
        editText1=view.findViewById(R.id.req11);
        editText2=view.findViewById(R.id.req12);
        editText3=view.findViewById(R.id.req13);

        button=view.findViewById(R.id.req123);
        spin = (Spinner) view.findViewById(R.id.spinner);

        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,branch);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads123");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    upload();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
    private void upload() throws IOException {
        a=editText1.getText().toString().trim();
        b=editText2.getText().toString().trim();
        c=editText3.getText().toString().trim();
        if(c!=null&&c.length()==13)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            databaseReference = FirebaseDatabase.getInstance().getReference("uploads123");
            String k=databaseReference.push().getKey();
            firebaseAuth=FirebaseAuth.getInstance();
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            String name=user.getEmail();
            String branch=spin.getSelectedItem().toString();
            add_book_data add_user_data=new add_book_data(editText1.getText().toString().trim(),editText2.getText().toString().trim(),editText3.getText().toString().trim(),name,branch);
            databaseReference.child(k).setValue(add_user_data);
            progressDialog.dismiss();
            Toast.makeText(getContext(),"Book requested",Toast.LENGTH_LONG).show();
            editText1.setText("");
            editText2.setText("");
            editText3.setText("");

        }
        else
        {
            Toast.makeText(getContext(),"Enter correct ISBN",Toast.LENGTH_LONG).show();
        }

    }
}
