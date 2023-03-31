package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.UiModeManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greenrise_sgp.databinding.ActivityOtpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
    EditText t1, t2, t3, t4, t5, t6;
    Button b2;
    String verificationId;
    FirebaseAuth mAuth;
    ActivityOtpactivityBinding binding;
    SellerRegisterActivity registerActivity = new SellerRegisterActivity();
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        t1 = (EditText) findViewById(R.id.ot1);
        t2 = (EditText) findViewById(R.id.ot2);
        t3 = (EditText) findViewById(R.id.ot3);
        t4 = (EditText) findViewById(R.id.ot4);
        t5 = (EditText) findViewById(R.id.ot5);
        t6 = (EditText) findViewById(R.id.ot6);
        b2 = (Button) findViewById(R.id.btnotp);
        mAuth = FirebaseAuth.getInstance();
        String num = getIntent().getStringExtra("phone");

        binding.mobile.setText(String.format("+91-%s", getIntent().getStringExtra("phone")));
        binding.resend.setEnabled(false);
        verificationId = getIntent().getStringExtra("verificationId");
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvtimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                binding.resend.setEnabled(true);
                binding.tvtimer.setText("");
            }
        }.start();
        edittextinput();
        binding.resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OTPActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        Intent intent = new Intent(OTPActivity.this, OTPActivity.class);
                        intent.putExtra("phone",num);
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }

                };
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+91" + num)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(OTPActivity.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
        binding.btnotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.ot1.getText().toString().trim().isEmpty()
                        || binding.ot2.getText().toString().trim().isEmpty()
                        || binding.ot3.getText().toString().trim().isEmpty()
                        || binding.ot4.getText().toString().trim().isEmpty()
                        || binding.ot5.getText().toString().trim().isEmpty()
                        || binding.ot6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OTPActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (verificationId != null) {
                        String code = binding.ot1.getText().toString().trim() + binding.ot2.getText().toString().trim()
                                + binding.ot3.getText().toString().trim() + binding.ot4.getText().toString().trim()
                                + binding.ot5.getText().toString().trim() + binding.ot6.getText().toString().trim();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(OTPActivity.this, SellerHomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(OTPActivity.this, "Invalid OTP!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
        binding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OTPActivity.this, PhoneActivity.class);
                intent.putExtra("mobile", num);
                startActivity(intent);
            }
        });
    }

    private void edittextinput() {
        binding.ot1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.ot1.getText().toString().length() == 1) {
                    binding.ot2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.ot2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.ot2.getText().toString().length() == 1) {
                    binding.ot3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.ot3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.ot3.getText().toString().length() == 1) {
                    binding.ot4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.ot4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.ot4.getText().toString().length() == 1) {
                    binding.ot5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.ot5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.ot5.getText().toString().length() == 1) {
                    binding.ot6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.ot6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.ot6.getText().toString().length() == 1) {
                    binding.btnotp.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
