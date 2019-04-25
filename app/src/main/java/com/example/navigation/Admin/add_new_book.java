package com.example.navigation.Admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.navigation.R;
import com.example.navigation.others.add_book_data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class add_new_book extends AppCompatActivity {
     EditText editText1,editText2,editText3,editText4,editText5;
     Spinner spinner;
     Button button;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ArrayAdapter aa;
    String selected;
    String genre[] = { "Physics", "fantasy_fiction", "sci-fi", "Mathematics", "Chemistry","biology","abstract","others"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);
        editText1=findViewById(R.id.newbook11);
        editText2=findViewById(R.id.newbook12);
        editText3=findViewById(R.id.newbook13);
        editText4=findViewById(R.id.newbook14);
        editText5=findViewById(R.id.newbook15);
        spinner=findViewById(R.id.newbook16);
        button=findViewById(R.id.newbook17);
        aa = new ArrayAdapter(add_new_book.this,android.R.layout.simple_spinner_item,genre);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
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
    }

    private void upload() throws IOException {
        if(editText3.getText().toString()!=null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(add_new_book.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            databaseReference = FirebaseDatabase.getInstance().getReference("books");

            selected=spinner.getSelectedItem().toString();
            String k=databaseReference.push().getKey();
            add_new_book_class add_new_book_class = new add_new_book_class(editText1.getText().toString(),k,editText2.getText().toString(),selected,editText3.getText().toString(),editText5.getText().toString(),editText4.getText().toString());
            databaseReference.child(k).setValue(add_new_book_class);
            progressDialog.dismiss();
            Toast.makeText(add_new_book.this,"Book requested",Toast.LENGTH_LONG).show();
            editText1.setText("");
            editText2.setText("");
            editText3.setText("");

        }
        else
        {
            Toast.makeText(add_new_book.this,"failed",Toast.LENGTH_LONG).show();
        }

    }
}
