package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassUser extends AppCompatActivity {
    EditText email;
    Button forbtn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_user);
        email = findViewById(R.id.FPEmail);
        forbtn = findViewById(R.id.FPpassbtn);
        mAuth = FirebaseAuth.getInstance();
        forbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpass();
            }
        });
    }

    private void resetpass() {
        String Email = email.getText().toString().trim();
        if(Email.isEmpty()){
            email.setError("Email is required!");
            email.setText(CurrentSeller.currentSeller.getEmail());
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Enter valid email!");
        }
        mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetPassUser.this, "Please check your email", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ForgetPassUser.this, "Try another email!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}