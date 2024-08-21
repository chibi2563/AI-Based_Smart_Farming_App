package com.samcore.leafdisease.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.samcore.leafdisease.R;
import com.samcore.leafdisease.components.AppSession;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView logout;
    CardView openCamara,selectImage,soilPrediction;
    TextView username;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    AppSession appSession;
    FloatingActionButton chatButton;
    private static final int FILE_CHOOSER_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseUser= firebaseAuth.getCurrentUser();
        appSession=new AppSession(HomeActivity.this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById();
    }

    @SuppressLint("SetTextI18n")
    private void findViewById() {

        logout=findViewById(R.id.logout);
        openCamara=findViewById(R.id.open_camara);
        selectImage=findViewById(R.id.select_image);
        username=findViewById(R.id.user_name);
        soilPrediction=findViewById(R.id.soil_image);

        chatButton=findViewById(R.id.chat_button);

        username.setText("Welcome Back! "+(appSession.getKeyUserName().toUpperCase()));

        logout.setOnClickListener(this);
        openCamara.setOnClickListener(this);
        selectImage.setOnClickListener(this);
        chatButton.setOnClickListener(this);
        soilPrediction.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v==logout) signOut();
        else if (v==openCamara) startActivity(new Intent(this, MainActivity.class));
        else if (v==selectImage) startActivity(new Intent(this, ImageActivity.class));
        else if (v==chatButton) startActivity(new Intent(this, ChatActivity.class));
        else if (v==soilPrediction) startActivity(new Intent(this, PredictionActivity.class));

    }
    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.logout))
                .setMessage("Are you sure want to logout?")
                .setPositiveButton("Yes",(dialog, which) -> {
                    firebaseAuth.signOut();
                    appSession.setKeyIsLoggedIn(false);
                    appSession.setKeyUserName("");
                    appSession.setKeyUid("");
                    appSession.setKeyUserType("");
                    Toasty.success(HomeActivity.this,"Logout successfully.. ",Toasty.LENGTH_SHORT).show();
                    Intent intent =new Intent(HomeActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }).setNegativeButton("No",(dialog, which) -> dialog.dismiss());
        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }

}
