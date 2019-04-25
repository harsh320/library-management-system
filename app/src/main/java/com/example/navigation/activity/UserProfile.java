package com.example.navigation.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.navigation.Admin.add_new_book;
import com.example.navigation.R;
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

public class UserProfile extends AppCompatActivity {
     EditText text1,text2,text3,text4;
     Button button;
    Spinner spinner,spinner1;
    ImageView imageView;
    Uri path;
    add_user_data add_user_data;
    StorageReference storageReference;
    ArrayAdapter aa;
    String selected;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String genre[] = { "Physics", "fantasy_fiction", "sci-fi", "Mathematics", "Chemistry","biology","abstract","others"};
    final int request=1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        text1=findViewById(R.id.data1);
        text2=findViewById(R.id.data2);
        text3=findViewById(R.id.data3);
        spinner=findViewById(R.id.data4);
        button=findViewById(R.id.data5);
        imageView=findViewById(R.id.user_pic);
        aa = new ArrayAdapter(UserProfile.this,android.R.layout.simple_spinner_item,genre);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectimage();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==request&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            path=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(UserProfile.this,"failed",Toast.LENGTH_LONG).show();
        }
    }

    public void selectimage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select picture"),request);
    }
    private void upload() throws IOException {
        if(path!=null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(UserProfile.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            storageReference = FirebaseStorage.getInstance().getReference();
            databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
            final StorageReference storageReference1=storageReference.child("uploads/"+System.currentTimeMillis()+"."+getfileextension(path));

            storageReference1.putFile(path)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            String k=databaseReference.push().getKey();
                            firebaseAuth=FirebaseAuth.getInstance();
                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            @SuppressWarnings("VisibleForTests") Task<Uri> downloadUrl = storageReference1.getDownloadUrl();
                            String name=user.getEmail();
                            selected=spinner.getSelectedItem().toString();
                            add_user_data=new add_user_data(text1.getText().toString(),text3.getText().toString(),name,downloadUrl.toString(),text2.getText().toString(),selected,k,"0");

                            String ae=user.getEmail();
                            databaseReference.child(k).setValue(add_user_data);
                            Intent intent=new Intent(UserProfile.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        else
        {
            Toast.makeText(UserProfile.this,"Failed",Toast.LENGTH_LONG).show();
        }

    }
    private String getfileextension(Uri path) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(path));
    }
}
