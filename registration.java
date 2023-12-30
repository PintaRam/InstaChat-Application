package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {

    TextView loginbut;
    EditText rg_username , rg_email, rg_password , rg_repassword;
    Button rg_signup;
    CircleImageView rg_profileImg;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    String emailPattern ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Establishing The Account");
        progressDialog.setCancelable(false);
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth =FirebaseAuth.getInstance();
        loginbut = findViewById(R.id.loginbut);
        rg_username = findViewById(R.id.rgusername);
        rg_email = findViewById(R.id.rgemail);
        rg_password = findViewById(R.id.rgpassword);
        rg_repassword = findViewById(R.id.rgrepassword);
        rg_profileImg = findViewById(R.id.profilerg0);
        rg_signup = findViewById(R.id.signupbutton);

        loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(registration.this,login.class); // to move from registration class to login class after clicking on login button
                startActivity(intent);
                finish();
            }
        });


        rg_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namee = rg_username.getText().toString(); // to get username
                String emaill = rg_email.getText().toString(); // to get the email
                String Password = rg_password.getText().toString(); // to get password
                String cPassword = rg_repassword.getText().toString(); // to get confirm password
                String status = "Hey I'm Using This Application";

                if(TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(cPassword))
                {   progressDialog.dismiss();
                    Toast.makeText(registration.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show(); // to check the if email or password or username is empty or not
                }else if(!emaill.matches(emailPattern)) // to check the email is matching pattern or not
                {   progressDialog.dismiss();
                    rg_email.setError("Type a Valid Email");
                } else if (Password.length()<6) {  // to check the password length is more than  or equal to 6 or not
                    progressDialog.dismiss();
                    rg_password.setError("Password Must be 6 Characters or More");

                } else if (!Password.equals(cPassword)) {  // to check both the confirm password and normal password is same or not
                    progressDialog.dismiss();
                    rg_password.setError("The Password Doesn;t Match");
                    
                }else {
                    auth.createUserWithEmailAndPassword(emaill,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {    // it will create a email and password user with task as unique id
                                progressDialog.dismiss();
                                String id = task.getResult().getUser().getUid(); // to get task id which has given a unique id
                                DatabaseReference reference = database.getReference().child("user").child(id); // creating a user in firefase
                                StorageReference storageReference = storage.getReference().child("Upload").child(id); // creating a child inside user which shows all information  of the user


                                 if(imageURI != null) // if user upload his images then execute else not
                                 {
                                     storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                         @Override // it will store his photo in firebase
                                         public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                             if(task.isSuccessful())
                                             {
                                                 storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                     @Override
                                                     public void onSuccess(Uri uri) {

                                                         imageuri = uri.toString();
                                                         Users users = new Users(id,namee,emaill, Password,imageuri,status); // it will create in getter and setters for users
                                                         reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {

                                                                 if(task.isSuccessful())
                                                                 {  progressDialog.show();
                                                                     Intent intent  = new Intent(registration.this, MainActivity.class);
                                                                     startActivity(intent);
                                                                     finish();   // if users succesfully got login then go to Main Activity
                                                                 }else{
                                                                     Toast.makeText(registration.this, "Error in Creating Account", Toast.LENGTH_SHORT).show(); // this print the error if user can't able to login or sign up
                                                                 }
                                                             }
                                                         });


                                                     }
                                                 });
                                             }

                                         }
                                     });
                                 }else { // if user won't upload the images then default images will taken from this link
                                     String status = "Hey I'm Using This Application";
                                     imageuri = "https://firebasestorage.googleapis.com/v0/b/mychat-45429.appspot.com/o/man.png?alt=media&token=4c9b0e94-ca0b-4190-a7c5-5a25217aae75";
                                     Users users =new Users(id,namee,emaill,Password,imageuri,status);
                                     reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {

                                             if(task.isSuccessful())
                                             {   progressDialog.show();
                                                 Intent intent  = new Intent(registration.this, MainActivity.class);
                                                 startActivity(intent);
                                                 finish();   // if successfully got login then go to MainActivity
                                             }else{
                                                 Toast.makeText(registration.this, "Error in Creating Account", Toast.LENGTH_SHORT).show(); // error if not able to login
                                             }
                                         }
                                     });

                                 }
                            }else {
                                Toast.makeText(registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show(); // this print the exception while runtime
                            }
                        }
                    });
                }
            }
        });


        rg_profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setType("image/*");  // it open a gallery of user
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 10);  // it will make a user to select the images from gallery or photos
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10)
        {
            if(data != null)
            {
                imageURI = data.getData(); //
                rg_profileImg.setImageURI(imageURI);
            }
        }
    }
}