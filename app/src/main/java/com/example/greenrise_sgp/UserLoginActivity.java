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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserLoginActivity extends AppCompatActivity {
    EditText email_login, pass_login;
    Button loginbtn;
    TextView reg, forpass, google_login;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    GoogleSignInClient googleSignInClient;
    ProgressDialog progressDialog;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "GOOGLEAUTH";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        email_login = findViewById(R.id.UserLoginEmail);
        pass_login = findViewById(R.id.UserLoginPasswd);
        loginbtn = findViewById(R.id.Userloginbtn);
        google_login = findViewById(R.id.Usergooglesignin);
        progressDialog = new ProgressDialog(this);
        reg = findViewById(R.id.Userregtv);
        mAuth = FirebaseAuth.getInstance();
        forpass = findViewById(R.id.Userforgotpass);
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user != null)
//        {
//            Intent intent = new Intent(UserLoginActivity.this,homePage.class);
//            startActivity(intent);
//        }
        forpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this, ForgetPassUser.class);
                startActivity(intent);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticate_user();

            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this,UserRegisterActivity.class);
                startActivity(intent);
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,gso);
        google_login.setOnClickListener(new View.OnClickListener() {
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
                            Intent i = new Intent(UserLoginActivity.this, homePage.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(UserLoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void authenticate_user() {

        progressDialog.setTitle("Login...");
        progressDialog.setMessage("Wait while we authenticate...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        String email = email_login.getText().toString();
        String pass = pass_login.getText().toString();

        if(TextUtils.isEmpty(email)){
            email_login.setError("Email is required!");
        }
        else if(TextUtils.isEmpty(pass)){
            pass_login.setError("Password is required!");
        }
        else{

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                uniqueUser u =new uniqueUser(email);
                                                CurrentUser.currentUser = snapshot.getValue(User.class);
                                              //  FirebaseDatabase db = FirebaseDatabase.getInstance();
//                                                DatabaseReference cart = db.getReference("CartPresentUser");
//                                                DatabaseReference wish = db.getReference("WishlistPresentUser");
//                                                DatabaseReference order = db.getReference("OrderPresentUser");
//                                                cart.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                        for(DataSnapshot snapshot1:snapshot.getChildren())
//                                                        {
//                                                            cart.child(snapshot1.child("parent").getValue().toString()).removeValue();
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                                    }
//                                                });
//                                                wish.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                        for(DataSnapshot snapshot1:snapshot.getChildren())
//                                                        {
//                                                            wish.child(snapshot1.child("parent").getValue().toString()).removeValue();
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                                    }
//                                                });
//                                                order.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                        for(DataSnapshot snapshot1:snapshot.getChildren())
//                                                        {
//                                                            order.child(snapshot1.child("parent").getValue().toString()).removeValue();
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                                    }
//                                                });
                                                Intent intent = new Intent(UserLoginActivity.this, homePage.class);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(UserLoginActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}