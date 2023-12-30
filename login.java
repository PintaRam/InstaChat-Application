package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    Button button;
    EditText email , password;
    FirebaseAuth auth;
    TextView logsignup;
    String emailPattern ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    android.app.ProgressDialog progressDialog; // to show the loading messege when it open the mainscreen after the signup or login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false); // there will be a option to delete the please wait message which can click by user show we made it as non editable
        getSupportActionBar().hide(); // to hide the top margin or bar

        auth =  FirebaseAuth.getInstance();
        button = findViewById(R.id.logbutton);
        email = findViewById(R.id.editTexLogEmail);
        password = findViewById(R.id.editTextLogPassword);
        logsignup =findViewById(R.id.logsignup);

        logsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this , registration.class);
                startActivity(intent);
                finish();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String pass = password.getText().toString();

                if(TextUtils.isEmpty(Email))
                {   progressDialog.dismiss(); // we don;t won't to show while it checking the email and same for rest
                    Toast.makeText(login.this, "Enter the Email", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(pass)){
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Enter the Password", Toast.LENGTH_SHORT).show();
                }
                else if(!Email.matches(emailPattern))
                {   progressDialog.dismiss();
                    email.setError("Give Proper Email Address");
                } else if (password.length()<6) {
                    password.setError("More than Six Error");
                    Toast.makeText(login.this, "Password need to longer than the six character", Toast.LENGTH_SHORT).show();

                }
                else {
                    auth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.show();
                                try{
                                    Intent intent = new Intent(login.this,MainActivity.class);// to move from login class to MainActivity class after clicking on login button
                                    startActivity(intent);
                                    finish();
                                }catch (Exception e)
                                {
                                    Toast.makeText(login.this,e.getMessage(),Toast.LENGTH_SHORT).show(); // Toast is used to show the messages like alert , prompt
                                }
                            }else {
                                Toast.makeText(login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();// this print the exception while runtime
                            }
                        }
                    });
                }



             }
        });
    }
}