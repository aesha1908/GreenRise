package com.example.greenrise_sgp;

import static com.example.greenrise_sgp.SellerRegisterActivity.confpass;
import static com.example.greenrise_sgp.SellerRegisterActivity.email;
import static com.example.greenrise_sgp.SellerRegisterActivity.name;
import static com.example.greenrise_sgp.SellerRegisterActivity.pass;
import static com.example.greenrise_sgp.SellerRegisterActivity.phone;
import static com.example.greenrise_sgp.SellerRegisterActivity.selltype;
import static com.example.greenrise_sgp.SellerRegisterActivity.upiid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greenrise_sgp.databinding.ActivityRegOtpactivityBinding;
import com.example.greenrise_sgp.databinding.ActivityUserRegOtpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegOTPActivity extends AppCompatActivity {
    EditText t1, t2, t3, t4, t5, t6, name_var, email_var, phone_var, pass_var, confpass_var;
    Button b2;
    String verificationId;
    FirebaseAuth mAuth;
    ActivityUserRegOtpactivityBinding binding;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        t1 = findViewById(R.id.uotp1);
        t2 = findViewById(R.id.uotp2);
        t3 = findViewById(R.id.uotp3);
        t4 = findViewById(R.id.uotp4);
        t5 = findViewById(R.id.uotp5);
        t6 = findViewById(R.id.uotp6);
        b2 = findViewById(R.id.ubtnotpreg);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        String num = getIntent().getStringExtra("phone");
        binding.umobilereg.setText(String.format("+91-%s", getIntent().getStringExtra("phone")));
        binding.uresendreg.setEnabled(false);
        verificationId = getIntent().getStringExtra("verificationId");
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.utvtimerreg.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                binding.uresendreg.setEnabled(true);
                binding.utvtimerreg.setText("");
            }
        }.start();
        edittextinput();
//        binding.resendreg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PhoneAuthProvider.getInstance()
//            }
//        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.uotp1.getText().toString().trim().isEmpty()
                        || binding.uotp2.getText().toString().trim().isEmpty()
                        || binding.uotp3.getText().toString().trim().isEmpty()
                        || binding.uotp4.getText().toString().trim().isEmpty()
                        || binding.uotp5.getText().toString().trim().isEmpty()
                        || binding.uotp6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(UserRegOTPActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (verificationId != null) {
                        String code = binding.uotp1.getText().toString().trim() + binding.uotp2.getText().toString().trim()
                                + binding.uotp3.getText().toString().trim() + binding.uotp4.getText().toString().trim()
                                + binding.uotp5.getText().toString().trim() + binding.uotp6.getText().toString().trim();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            performAuthentication();
                                        } else {
                                            Toast.makeText(UserRegOTPActivity.this, "Invalid OTP!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
    private void performAuthentication() {
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
                            User seller = new User(uid, name, email, phone, pass, confpass);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(uid)
                                    .setValue(seller).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(UserRegOTPActivity.this, HomeFragment.class);
                                            startActivity(intent);
                                        }
                                    });
                        } else {
                            Toast.makeText(UserRegOTPActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void edittextinput() {
        binding.uotp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.uotp1.getText().toString().length() == 1) {
                    binding.uotp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.uotp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.uotp2.getText().toString().length() == 1) {
                    binding.uotp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.uotp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.uotp3.getText().toString().length() == 1) {
                    binding.uotp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.uotp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.uotp4.getText().toString().length() == 1) {
                    binding.uotp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.uotp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.uotp5.getText().toString().length() == 1) {
                    binding.uotp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.uotp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.uotp6.getText().toString().length() == 1) {
                    binding.ubtnotpreg.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}