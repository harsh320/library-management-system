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

import com.example.navigation.Admin.Admin;
import com.example.navigation.R;
import com.example.navigation.activity.MainActivity;
import com.example.navigation.activity.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    EditText edit1,edit2;
    Button bt,bt2,bt3;
    TextView bt1;
    FirebaseAuth auth1;
    List<JSONObject> jsonObjects = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edit1=findViewById(R.id.edit1);
        edit2=findViewById(R.id.edit2);
        bt=findViewById(R.id.button3);
        bt1=findViewById(R.id.button4);


        auth1= FirebaseAuth.getInstance();


        final FirebaseUser user = auth1.getCurrentUser();
        if(user!=null)
        {
            if(user.getUid().equalsIgnoreCase("91WJZZSoBMMKR8JdRPlUl4juKJx2"))
            {
                Toast.makeText(Login.this,user.getUid(),Toast.LENGTH_LONG).show();
                Intent i=new Intent(Login.this, Admin.class);
                startActivity(i);
                finish();
            }

             else if(user.isEmailVerified()) {
                 Toast.makeText(Login.this,"Welcome",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }
        }
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Registration.class));
                finish();
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit1.getText().toString();
                final String password = edit2.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth1.signInWithEmailAndPassword(email,password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        { if (password.length() < 6) {
                            Toast.makeText(Login.this, "pass short", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            Toast.makeText(Login.this, "failed", Toast.LENGTH_LONG).show();}
                        }
                        else
                        {
                            final FirebaseUser user = auth1.getCurrentUser();
                            if(user.isEmailVerified()) {

                                Intent i = new Intent(Login.this, UserProfile.class);
                                Toast.makeText(Login.this,"Please enter Details",Toast.LENGTH_LONG).show();
                                startActivity(i);
                                finish();

                            }
                            else
                            {
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Login.this,
                                                    "Verification mail sent",
                                                    Toast.LENGTH_SHORT).show();


                                        } else {
                                            Toast.makeText(Login.this,
                                                    "Please try again",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

            }
        });


    }
}
