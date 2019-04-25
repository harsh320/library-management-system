package com.example.navigation.login_registeration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    EditText edit,edit1;
    Button button2,button3;
    FirebaseAuth auth;
    TextView button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        edit=findViewById(R.id.database);
        edit1=findViewById(R.id.database1);
        button1=findViewById(R.id.button2);
        button2=findViewById(R.id.button5);

        auth= FirebaseAuth.getInstance();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,Login.class));
                finish();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edit.getText().toString().trim();
                String pass=edit1.getText().toString().trim();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(Registration.this,"enter the email",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(TextUtils.isEmpty(pass))
                {
                    Toast.makeText(Registration.this,"enter the password",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(pass.length()<6)
                {
                    Toast.makeText(Registration.this,"enter the correct lenght",Toast.LENGTH_LONG).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Registration.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(Registration.this,Login.class));
                            finish();
                        }
                    }

                });
            }

        });
    }
}
