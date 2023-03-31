package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class UserRegisterActivity extends AppCompatActivity {
    EditText name_var, email_var, phone_var, pass_var, confpass_var;
    TextView log;
    Button regbutton;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    public static String name, email, phone, pass, confpass;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        name_var = findViewById(R.id.UserName);
        email_var = findViewById(R.id.UserEmail);
        phone_var = findViewById(R.id.UserPhone);
        pass_var = findViewById(R.id.UserRegPasswd);
        confpass_var = findViewById(R.id.UserConfRegPasswd);
        log = findViewById(R.id.Userlogtv);
        regbutton = findViewById(R.id.Userpassbtn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegisterActivity.this,UserLoginActivity.class);
                startActivity(intent);
            }
        });
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = name_var.getText().toString();
                email = email_var.getText().toString();
                phone = phone_var.getText().toString();
                pass = pass_var.getText().toString();
                confpass = confpass_var.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    name_var.setError("Name is required!");
                } else if (TextUtils.isEmpty(email)) {
                    email_var.setError("Enter Proper Email!");
                } else if (TextUtils.isEmpty(phone) || phone.length() != 10) {
                    phone_var.setError("Enter 10 digit phone number!");
                } else if (TextUtils.isEmpty(pass) || pass.length() < 6) {
                    pass_var.setError("Create strong password!");
                } else if (TextUtils.isEmpty(confpass) || !confpass.matches(pass)) {
                    confpass_var.setError("Passwords do not match!");
                }
                else{
                    sendotp();
                }

            }

        });
    }
    public void sendotp() {
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                regbutton.setVisibility(View.VISIBLE);
                Toast.makeText(UserRegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Intent intent = new Intent(UserRegisterActivity.this, UserRegOTPActivity.class);
                intent.putExtra("phone", phone_var.getText().toString().trim());
                intent.putExtra("verificationId", verificationId);
                startActivity(intent);
            }

        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + phone_var.getText().toString().trim())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
