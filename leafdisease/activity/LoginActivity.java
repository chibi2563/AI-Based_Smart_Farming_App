package com.samcore.leafdisease.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samcore.leafdisease.R;
import com.samcore.leafdisease.model.UserModel;
import com.samcore.leafdisease.components.AppSession;
import com.samcore.leafdisease.components.ValidationLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    //login
    LinearLayout ll_loginContainer,ll_registerContainer;
    TextInputLayout loginEmail,loginPassword,
            registerFirstname,registerLastname,registerEmail,registerPassword,registerConfirmPassword;
    AppCompatButton loginButton,registerButton;
    ImageView loginImage;
    TextView registerHeaderText,loginRegisterDescTxt,loginRegisterText;
    AppCompatSpinner typeSpinner;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,adminReference;
    public static final String TAG="LoginActivity";

    UserModel userModel=new UserModel();
    AppSession appSession;
    String userType="";
    String adminEmail="",adminPassword="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appSession=new AppSession(getApplicationContext());
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("UserInfo");
        adminReference= firebaseDatabase.getReference("admin");
        firebaseUser=firebaseAuth.getCurrentUser();

        findViewById();
    }

    private void findViewById() {
        registerHeaderText=findViewById(R.id.register_header_text);

        loginRegisterDescTxt=findViewById(R.id.login_register_desc);
        loginRegisterText=findViewById(R.id.login_register_text);

        loginImage=findViewById(R.id.login_logo);
        loginButton=findViewById(R.id.login_button);
        loginEmail=findViewById(R.id.login_email);
        loginPassword=findViewById(R.id.login_password);

        ll_loginContainer=findViewById(R.id.ll_login_container);
        ll_registerContainer=findViewById(R.id.ll_register_container);

        registerFirstname=findViewById(R.id.register_firstname);
        registerLastname=findViewById(R.id.register_lastname);
        registerEmail=findViewById(R.id.register_email);
        registerPassword=findViewById(R.id.register_password);
        registerConfirmPassword=findViewById(R.id.register_confirm_password);
        registerButton=findViewById(R.id.register_button);

        typeSpinner=findViewById(R.id.type_spinner);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        loginRegisterText.setOnClickListener(this);
        typeSpinner.setOnItemSelectedListener(this);



        // Spinner Drop down elements
        List<String> types = new ArrayList<>();
        types.add("User");


        Objects.requireNonNull(loginEmail.getEditText()).addTextChangedListener(textWatcher(loginEmail));
        Objects.requireNonNull(loginPassword.getEditText()).addTextChangedListener(textWatcher(loginPassword));

        Objects.requireNonNull(registerFirstname.getEditText()).addTextChangedListener(textWatcher(registerFirstname));
        Objects.requireNonNull(registerLastname.getEditText()).addTextChangedListener(textWatcher(registerLastname));
        Objects.requireNonNull(registerEmail.getEditText()).addTextChangedListener(textWatcher(registerEmail));
        Objects.requireNonNull(registerPassword.getEditText()).addTextChangedListener(textWatcher(registerPassword));
        Objects.requireNonNull(registerConfirmPassword.getEditText()).addTextChangedListener(textWatcher(registerConfirmPassword));


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        typeSpinner.setAdapter(dataAdapter);
        getAdminInfo();

    }

    private void getAdminInfo() {
        adminReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminEmail=snapshot.child("email").getValue(String.class);
                adminPassword=snapshot.child("password").getValue(String.class);

                Log.e(TAG, "onDataChange: admin email "+adminEmail+"  password  "+adminPassword );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private TextWatcher textWatcher(final TextInputLayout textInputLayout){

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validation(textInputLayout,editable.toString());
            }
        };
    }

    private void validation(TextInputLayout textInputLayout, String text) {
        /*if (textInputLayout==loginEmail) {
            if (text.isEmpty()) {
                textInputLayout.setError(getString(R.string.text_empty_error));
                loginButton.setEnabled(false);
            } else if (ValidationLogic.isValidEmail(text)) {
                textInputLayout.setError(getString(R.string.email_error));
                loginButton.setEnabled(false);
            }
            else {
                textInputLayout.setErrorEnabled(false);
                loginButton.setEnabled(true);
            }
        } else if (textInputLayout == loginPassword) {
            if (text.isEmpty()) {
                textInputLayout.setError(getString(R.string.text_empty_error));
                loginButton.setEnabled(false);
            } else if (ValidationLogic.isValidPassword(text)) {
                textInputLayout.setError(getString(R.string.password_invalid_error));
                loginButton.setEnabled(false);
            }else {
                textInputLayout.setErrorEnabled(false);
                loginButton.setEnabled(true);
            }
        }*/
        if (textInputLayout==registerEmail) {
            if (text.isEmpty()) {
                textInputLayout.setError(getString(R.string.text_empty_error));
                registerButton.setEnabled(false);
            }
            else if (ValidationLogic.isValidEmail(text)) {
                textInputLayout.setError(getString(R.string.email_error));
                registerButton.setEnabled(false);
            }
            else {
                textInputLayout.setErrorEnabled(false);
                registerButton.setEnabled(true);
            }
        } else if (textInputLayout==registerFirstname) {
            if (text.isEmpty()) {
                textInputLayout.setError(getString(R.string.text_empty_error));
                registerButton.setEnabled(false);
            }
            else if (text.length()<=2) {
                textInputLayout.setError(getString(R.string.first_name_error));
                registerButton.setEnabled(false);
            }
            else {
                textInputLayout.setErrorEnabled(false);
                registerButton.setEnabled(true);
            }
        } else if (textInputLayout==registerLastname) {
            if (text.isEmpty()) {
                textInputLayout.setError(getString(R.string.text_empty_error));
                registerButton.setEnabled(false);
            }
            else {
                textInputLayout.setErrorEnabled(false);
                registerButton.setEnabled(true);
            }
        }/*else if (textInputLayout==registerPassword){
            if (text.isEmpty())
                textInputLayout.setError(getString(R.string.text_empty_error));
            else if (!validationLogic.isValidPhoneNumber(Objects.requireNonNull(phone.getEditText()).getText().toString().trim()))
                textInputLayout.setError(getString(R.string.phone_number_error));
            else
                textInputLayout.setErrorEnabled(false);
        }*/
        else if (textInputLayout==registerPassword){
            if (text.isEmpty()) {
                textInputLayout.setError(getString(R.string.text_empty_error));
                registerButton.setEnabled(false);
            }
            else if (ValidationLogic.isValidPassword(text)) {
                textInputLayout.setError(getString(R.string.password_error));
                registerButton.setEnabled(false);
            }
            else {
                textInputLayout.setErrorEnabled(false);
                registerButton.setEnabled(true);
            }
        } else if (textInputLayout==registerConfirmPassword) {
            if (text.isEmpty()) {
                textInputLayout.setError(getString(R.string.text_empty_error));
                registerButton.setEnabled(false);
            }
            else if (!Objects.requireNonNull(registerPassword.getEditText()).getText().toString()
                    .equals(Objects.requireNonNull(registerConfirmPassword.getEditText()).getText().toString())) {
                textInputLayout.setError(getString(R.string.confirm_password_error));
                registerButton.setEnabled(false);
            }
            else {
                textInputLayout.setErrorEnabled(false);
                registerButton.setEnabled(true);
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onClick(View v) {
        if (v==loginButton) {
            String email=Objects.requireNonNull(loginEmail.getEditText()).getText().toString().trim();
            String password=Objects.requireNonNull(loginPassword.getEditText()).getText().toString();
          if (loginValidation()){

                  signIn(email, password);
            }

        }else if (v==registerButton) {
            if (registerValidation()){
                createAccount(Objects.requireNonNull(registerEmail.getEditText()).getText().toString().trim(), Objects.requireNonNull(registerPassword.getEditText()).getText().toString());
                Toasty.success(LoginActivity.this, "Register Success", Toasty.LENGTH_SHORT).show();
            }

        }else if (v==loginRegisterText){
            if(loginRegisterText.getText().equals(getString(R.string.login))){
                registerHeaderText.setVisibility(View.GONE);
                loginImage.setVisibility(View.VISIBLE);

                ll_loginContainer.setVisibility(View.VISIBLE);
                ll_registerContainer.setVisibility(View.GONE);

                loginRegisterDescTxt.setText(getString(R.string.login_to_register));
                loginRegisterText.setText(getString(R.string.register));
            }else {
                registerHeaderText.setVisibility(View.VISIBLE);
                loginImage.setVisibility(View.GONE);

                ll_loginContainer.setVisibility(View.GONE);
                ll_registerContainer.setVisibility(View.VISIBLE);

                loginRegisterDescTxt.setText(getString(R.string.register_to_login));
                loginRegisterText.setText(getString(R.string.login));
            }
        }
    }
    private boolean loginValidation(){
        boolean validation=false;
        if (Objects.requireNonNull(loginEmail.getEditText()).getText().toString().isEmpty()){
            loginEmail.setError(getString(R.string.text_empty_error));
        } else if (Objects.requireNonNull(loginPassword.getEditText()).getText().toString().isEmpty()) {
            loginPassword.setError(getString(R.string.text_empty_error));
        }else{
            validation=true;
        }
        return validation;
    }
    private boolean registerValidation(){
        boolean validation=false;
        if (Objects.requireNonNull(registerFirstname.getEditText()).getText().toString().isEmpty())
            registerFirstname.setError(getString(R.string.text_empty_error));
        else if (Objects.requireNonNull(registerLastname.getEditText()).getText().toString().isEmpty()) {
            registerLastname.setError(getString(R.string.text_empty_error));
        } else if (Objects.requireNonNull(registerEmail.getEditText()).getText().toString().isEmpty()) {
            registerEmail.setError(getString(R.string.text_empty_error));
        } else if (Objects.requireNonNull(registerPassword.getEditText()).getText().toString().isEmpty()) {
            registerPassword.setError(getString(R.string.text_empty_error));
        } else if (Objects.requireNonNull(registerConfirmPassword.getEditText()).getText().toString().isEmpty()) {
            registerConfirmPassword.setError(getString(R.string.text_empty_error));
        } else{
            validation=true;
        }
        return validation;
    }
    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
//                        updateUI(user);
                        if (!Objects.requireNonNull(user).isEmailVerified()){
                            Toasty.warning(LoginActivity.this, "Please verify your email", Toasty.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this, EmailVerificationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            appSession.setKeyIsLoggedIn(true);
                            getUserInfo(user);
                            Toasty.success(LoginActivity.this, "logged in successfully", Toasty.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        // [END sign_in_with_email]
    }
    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        addDataToFirebase(Objects.requireNonNull(user));
                        user.sendEmailVerification()
                                .addOnSuccessListener(unused -> Toasty.success(LoginActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Log.e(TAG, "onFailure: onFailure: Email not sent"+e.getMessage()));
                        startActivity(new Intent(LoginActivity.this,LoginActivity.class));
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        // If sign in fails, display a message to the user.
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthWeakPasswordException e) {
                            // Handle weak password exception
                            String errorCode = e.getErrorCode();
                            String errorMessage = e.getMessage();

                            if (errorCode.equals("ERROR_WEAK_PASSWORD")) {
                                // Password is too weak
                                Toast.makeText(LoginActivity.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                            } else {
                                // Other weak password exception, handle accordingly
                                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            // Handle other exceptions
                            Toast.makeText(LoginActivity.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        updateUI(null);
                    }
                });
        // [END create_user_with_email]
    }
    private void getUserInfo(FirebaseUser user) {

        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userName=snapshot.child("firstName").getValue(String.class);
                    String uid=snapshot.child("id").getValue(String.class);
                    String userType=snapshot.child("userType").getValue(String.class);
                    Log.e(TAG, "onDataChange: username"+ userType );
                    appSession.setKeyUserName(userName);
                    appSession.setKeyUid(uid);
                    appSession.setKeyUserType(userType);
                }else
                    Toast.makeText(LoginActivity.this, "user name not found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void addDataToFirebase( FirebaseUser user) {
        // below 3 lines of code is used to set
        // data in our object class.

        userModel.setFirstName(Objects.requireNonNull(registerFirstname.getEditText()).getText().toString().trim());
        userModel.setLastName(Objects.requireNonNull(registerLastname.getEditText()).getText().toString().trim());
        userModel.setEmail(user.getEmail());
        userModel.setId(user.getUid());
        userModel.setUserType(userType);
        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.orderByChild("id").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                if (snapshot.exists())
                    Toast.makeText(LoginActivity.this, "This user already exist", Toast.LENGTH_SHORT).show();
                else {
                    databaseReference.child(user.getUid()).setValue(userModel);
                    // after adding this data we are showing toast message.
//                    Toast.makeText(LoginActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(LoginActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        userType = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toasty.info(parent.getContext(), "Selected: " + userType, Toasty.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
//        firebaseUser.reload();
    }

    private void updateUI(FirebaseUser currentUser) {

    }
}
