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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class SellerRegisterActivity extends AppCompatActivity {
    EditText name_var, email_var, phone_var, pass_var, confpass_var,upiid_var;
    TextView log, google_signin;
    Button regbutton;
    Spinner selltype_var;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "GOOGLEAUTH";
    int maxid = 0;

    String[] types = {"Seller Type", "Plant Seller", "Flower Seller", "Event Decor"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);
        google_signin = findViewById(R.id.googlesignin);
        log = findViewById(R.id.logtv);
        regbutton = findViewById(R.id.passbtn);
        selltype_var = findViewById(R.id.SellerType);
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
                performAuthentication();
            }

        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,gso);
        google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    SignIn();
            }
        });
    }

    private void SignIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);

            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(SellerRegisterActivity.this, SellerHomeActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(SellerRegisterActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
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
        if(TextUtils.isEmpty(name)){
            name_var.setError("Name is required!");
        }
        else if(TextUtils.isEmpty(email)){
            email_var.setError("Enter Proper Email!");
        }
        else if(TextUtils.isEmpty(phone) || phone.length()!=10){
            phone_var.setError("Enter 10 digit phone number!");
        }
        else if(TextUtils.isEmpty(pass) || pass.length()<6){
            pass_var.setError("Create strong password!");
        }
        else if(TextUtils.isEmpty(confpass) || !confpass.matches(pass)){
            confpass_var.setError("Passwords do not match!");
        }
        else if(selltype_var.getSelectedItem().toString().trim().equals("Seller Type")){
            Toast.makeText(SellerRegisterActivity.this, "Select Seller Type", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(upiid)){
            email_var.setError("Enter Proper UPI ID!");
        }
        else{
            progressDialog.setTitle("Registration...");
            progressDialog.setMessage("Wait while we register your data...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Seller seller = new Seller(name, email, phone, pass, confpass, selltype,upiid);
                                FirebaseDatabase.getInstance().getReference("Sellers")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(seller).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent = new Intent(SellerRegisterActivity.this, SellerLoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                            } else {
                                Toast.makeText(SellerRegisterActivity.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }


}