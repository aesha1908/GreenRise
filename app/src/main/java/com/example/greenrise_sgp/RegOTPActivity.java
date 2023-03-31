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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.greenrise_sgp.databinding.ActivityOtpactivityBinding;
import com.example.greenrise_sgp.databinding.ActivityRegOtpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class RegOTPActivity extends AppCompatActivity {
    EditText t1, t2, t3, t4, t5, t6, name_var, email_var, phone_var, pass_var, confpass_var, upiid_var;
    Button b2;
    String verificationId;
    FirebaseAuth mAuth;
    ActivityRegOtpactivityBinding binding;
    ProgressDialog progressDialog;
    Spinner selltype_var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        t1 = findViewById(R.id.otp1);
        t2 = findViewById(R.id.otp2);
        t3 = findViewById(R.id.otp3);
        t4 = findViewById(R.id.otp4);
        t5 = findViewById(R.id.otp5);
        t6 = findViewById(R.id.otp6);
        b2 = findViewById(R.id.btnotpreg);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        String num = getIntent().getStringExtra("phone");
        binding.mobilereg.setText(String.format("+91-%s", getIntent().getStringExtra("phone")));
        binding.resendreg.setEnabled(false);
        verificationId = getIntent().getStringExtra("verificationId");
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvtimerreg.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                binding.resendreg.setEnabled(true);
                binding.tvtimerreg.setText("");
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
                if (binding.otp1.getText().toString().trim().isEmpty()
                        || binding.otp2.getText().toString().trim().isEmpty()
                        || binding.otp3.getText().toString().trim().isEmpty()
                        || binding.otp4.getText().toString().trim().isEmpty()
                        || binding.otp5.getText().toString().trim().isEmpty()
                        || binding.otp6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RegOTPActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (verificationId != null) {
                        String code = binding.otp1.getText().toString().trim() + binding.otp2.getText().toString().trim()
                                + binding.otp3.getText().toString().trim() + binding.otp4.getText().toString().trim()
                                + binding.otp5.getText().toString().trim() + binding.otp6.getText().toString().trim();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            performAuthentication();
                                        } else {
                                            Toast.makeText(RegOTPActivity.this, "Invalid OTP!", Toast.LENGTH_SHORT).show();
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
                            Seller seller = new Seller(uid, name, email, phone, pass, confpass, selltype, upiid);
                            FirebaseDatabase.getInstance().getReference("Sellers")
                                    .child(uid)
                                    .setValue(seller).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(RegOTPActivity.this, SellerLoginActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                        } else {
                            Toast.makeText(RegOTPActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void edittextinput() {
        binding.otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.otp1.getText().toString().length() == 1) {
                    binding.otp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.otp2.getText().toString().length() == 1) {
                    binding.otp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.otp3.getText().toString().length() == 1) {
                    binding.otp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.otp4.getText().toString().length() == 1) {
                    binding.otp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.otp5.getText().toString().length() == 1) {
                    binding.otp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.otp6.getText().toString().length() == 1) {
                    binding.btnotpreg.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}