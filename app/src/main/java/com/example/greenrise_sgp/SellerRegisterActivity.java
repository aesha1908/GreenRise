package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenrise_sgp.databinding.ActivitySellerRegisterBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class SellerRegisterActivity extends AppCompatActivity {
    EditText name_var, email_var, phone_var, pass_var, confpass_var, upiid_var;
    TextView log;
    Button regbutton;
    Spinner selltype_var;
    CountryCodePicker ccpick;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    public static String name, email, phone, pass, confpass, selltype, upiid;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String[] types = {"Seller Type", "Plant Seller", "Flower Seller", "Event Decor"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);
        log = findViewById(R.id.logtv);
        regbutton = findViewById(R.id.passbtn);
        selltype_var = findViewById(R.id.SellerType);
        ccpick = findViewById(R.id.ccp);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item, types);
        adapter.setDropDownViewResource(com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item);
        selltype_var.setAdapter(adapter);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logint = new Intent(SellerRegisterActivity.this, SellerLoginActivity.class);
                startActivity(logint);
            }
        });
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //performAuthentication();
                name_var = findViewById(R.id.Name);
                email_var = findViewById(R.id.Email);
                phone_var = findViewById(R.id.Phone);
                pass_var = findViewById(R.id.RegPasswd);
                confpass_var = findViewById(R.id.ConfRegPasswd);
                upiid_var = findViewById(R.id.UPIid);
                name = name_var.getText().toString();
                email = email_var.getText().toString();
                phone = phone_var.getText().toString();
                pass = pass_var.getText().toString();
                confpass = confpass_var.getText().toString();
                selltype = selltype_var.getSelectedItem().toString();
                upiid = upiid_var.getText().toString();
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
                } else if (selltype_var.getSelectedItem().toString().trim().equals("Seller Type")) {
                    Toast.makeText(SellerRegisterActivity.this, "Select Seller Type", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(upiid)) {
                    email_var.setError("Enter Proper UPI ID!");
                }
                sendotp();
            }

        });
    }

    private void performAuthentication() {
        name_var = findViewById(R.id.Name);
        email_var = findViewById(R.id.Email);
        phone_var = findViewById(R.id.Phone);
        pass_var = findViewById(R.id.RegPasswd);
        confpass_var = findViewById(R.id.ConfRegPasswd);
        upiid_var = findViewById(R.id.UPIid);
        String name = name_var.getText().toString();
        String email = email_var.getText().toString();
        String phone = phone_var.getText().toString();
        String pass = pass_var.getText().toString();
        String confpass = confpass_var.getText().toString();
        String selltype = selltype_var.getSelectedItem().toString();
        String upiid = upiid_var.getText().toString();
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
        } else if (selltype_var.getSelectedItem().toString().trim().equals("Seller Type")) {
            Toast.makeText(SellerRegisterActivity.this, "Select Seller Type", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(upiid)) {
            email_var.setError("Enter Proper UPI ID!");
        } else {
            progressDialog.setTitle("Registration...");
            progressDialog.setMessage("Wait while we register your data...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Seller seller = new Seller(uid, name, email, phone, pass, confpass, selltype, upiid);
                                FirebaseDatabase.getInstance().getReference("Sellers")
                                        .child(uid)
                                        .setValue(seller).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent = new Intent(SellerRegisterActivity.this, SellerLoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                            } else {
                                Toast.makeText(SellerRegisterActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }
    public void sendotp() {
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                regbutton.setVisibility(View.VISIBLE);
                Toast.makeText(SellerRegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Intent intent = new Intent(SellerRegisterActivity.this, RegOTPActivity.class);
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


