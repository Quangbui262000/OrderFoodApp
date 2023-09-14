package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG="TAG";
    private FirebaseAuth auth;
    private EditText signupEmail,signupPassword,signupName;
    private Button signupButton;
    private TextView loginRedirectText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth=FirebaseAuth.getInstance();
        signupEmail=findViewById(R.id.sigup_email);
        signupName=findViewById(R.id.sigup_name);
        signupPassword=findViewById(R.id.sigup_password);
        signupButton=findViewById(R.id.sigup_button);
        loginRedirectText=findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =signupName.getText().toString().trim();
                String user =signupEmail.getText().toString().trim();
                String pass =signupPassword.getText().toString().trim();
                if (name.isEmpty()){
                    signupName.setError("Name cannot be empty!");
                }
                if (user.isEmpty()){
                    signupEmail.setError("Email cannot be empty!");
                }
                if (pass.isEmpty()){
                    signupPassword.setError("Pass cannot be empty!");
                }else {
                    auth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                //Send Verification code
                                FirebaseUser fuse=auth.getCurrentUser();
                                fuse.sendEmailVerification().addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(SignUpActivity.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"onFailure:Email not sent"+e.getMessage());
                                    }
                                });

                            }else {
                                Toast.makeText(SignUpActivity.this, "SignUp Fail"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });

    }
}