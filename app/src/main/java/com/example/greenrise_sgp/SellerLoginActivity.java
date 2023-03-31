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
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenrise_sgp.databinding.ActivitySellerLoginBinding;
import com.example.greenrise_sgp.databinding.ActivitySellerRegisterBinding;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class SellerLoginActivity extends AppCompatActivity {
    EditText email_login, pass_login;
    Button loginbtn;
    TextView reg, forpass, otp;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    ActivitySellerLoginBinding binding;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    CountryCodePicker ccp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        email_login = findViewById(R.id.LoginEmail);
        pass_login = findViewById(R.id.LoginPasswd);
        loginbtn = findViewById(R.id.loginbtn);
        progressDialog = new ProgressDialog(this);
        reg = findViewById(R.id.regtv);
        mAuth = FirebaseAuth.getInstance();
        forpass = findViewById(R.id.forgotpass);
        ccp = findViewById(R.id.ccplog);
        forpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerLoginActivity.this, ForgetPass.class);
                startActivity(intent);
            }
        });
        binding.otplogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendotp();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.LoginEmail.getText().toString().isEmpty())
                {
                    binding.LoginEmail.setError("Enter valid email or phone number");
                }
                if(binding.LoginEmail.getText().toString().contains("@"))
                {
                    authenticate_user();
                }
                else{
                    sendotp();
                }
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerLoginActivity.this, SellerRegisterActivity.class);
                startActivity(intent);
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
                binding.loginbtn.setVisibility(View.VISIBLE);
                Toast.makeText(SellerLoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Intent intent = new Intent(SellerLoginActivity.this, OTPActivity.class);
                intent.putExtra("phone", binding.LoginEmail.getText().toString().trim());
                intent.putExtra("verificationId", verificationId);
                startActivity(intent);
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + binding.LoginEmail.getText().toString().trim())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void authenticate_user() {
        progressDialog.setTitle("Login...");
        progressDialog.setMessage("Wait while we authenticate...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String email = email_login.getText().toString();
        String pass = pass_login.getText().toString();

        if (TextUtils.isEmpty(email)) {
            email_login.setError("Email is required!");
        } else if (TextUtils.isEmpty(pass)) {
            pass_login.setError("Password is required!");
        } else {
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                FirebaseDatabase.getInstance().getReference("Sellers")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                CurrentSeller.currentSeller = snapshot.getValue(Seller.class);
                                                Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SellerLoginActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}

