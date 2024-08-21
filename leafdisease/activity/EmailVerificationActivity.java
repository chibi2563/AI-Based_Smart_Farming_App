package com.samcore.leafdisease.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.samcore.leafdisease.R;

import es.dmoral.toasty.Toasty;

public class EmailVerificationActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    AppCompatButton verifyButton;
    public static final String TAG="Email Verification Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        verifyButton=findViewById(R.id.email_verify_button);

        verifyButton.setOnClickListener((view)->{
            if (firebaseUser != null) {
                firebaseUser.sendEmailVerification()
                        .addOnSuccessListener(unused -> {
                            Toasty.success(EmailVerificationActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this,LoginActivity.class));
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Log.e(TAG, "onFailure: onFailure: Email not sent" + e.getMessage()));
            }else
                Toasty.error(EmailVerificationActivity.this, " email not sent", Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
//        firebase.initializeApp({apiKey: 'abc123'})

    }
}
