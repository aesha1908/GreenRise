package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greenrise_sgp.databinding.ActivityOtpactivityBinding;
import com.example.greenrise_sgp.databinding.ActivityUserOtpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class UserOTPActivity extends AppCompatActivity {
    EditText t1, t2, t3, t4, t5, t6;
    Button b2;
    String verificationId;
    FirebaseAuth mAuth;
    ActivityUserOtpactivityBinding binding;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserOtpactivityBinding.inflate(getLayoutInflater());
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
        binding.umobile.setText(String.format("+91-%s", getIntent().getStringExtra("phone")));
        binding.uresend.setEnabled(false);
        verificationId = getIntent().getStringExtra("verificationId");
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.utvtimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                binding.uresend.setEnabled(true);
                binding.utvtimer.setText("");
            }
        }.start();
        edittextinput();
        binding.uresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(UserOTPActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        Intent intent = new Intent(UserOTPActivity.this, OTPActivity.class);
                        intent.putExtra("phone",num);
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }

                };
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+91" + num)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(UserOTPActivity.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
        binding.ubtnotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.uot1.getText().toString().trim().isEmpty()
                        || binding.uot2.getText().toString().trim().isEmpty()
                        || binding.uot3.getText().toString().trim().isEmpty()
                        || binding.uot4.getText().toString().trim().isEmpty()
                        || binding.uot5.getText().toString().trim().isEmpty()
                        || binding.uot6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(UserOTPActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (verificationId != null) {
                        String code = binding.uot1.getText().toString().trim() + binding.uot2.getText().toString().trim()
                                + binding.uot3.getText().toString().trim() + binding.uot4.getText().toString().trim()
                                + binding.uot5.getText().toString().trim() + binding.uot6.getText().toString().trim();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(UserOTPActivity.this, SellerHomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(UserOTPActivity.this, "Invalid OTP!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
        binding.uchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserOTPActivity.this, PhoneActivity.class);
                intent.putExtra("mobile", num);
                startActivity(intent);
            }
        });
    }
    private void edittextinput() {
        binding.uot1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.uot1.getText().toString().length() == 1) {
                    binding.uot2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.uot2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.uot2.getText().toString().length() == 1) {
                    binding.uot3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.uot3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.uot3.getText().toString().length() == 1) {
                    binding.uot4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.uot4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.uot4.getText().toString().length() == 1) {
                    binding.uot5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.uot5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.uot5.getText().toString().length() == 1) {
                    binding.uot6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.uot6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.uot6.getText().toString().length() == 1) {
                    binding.ubtnotp.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}