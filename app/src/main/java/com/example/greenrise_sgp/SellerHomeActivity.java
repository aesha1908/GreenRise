package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class SellerHomeActivity extends AppCompatActivity {
    Button addp,viewp,up;
    EditText name,about,price,quantity;
    Plant plant;
    BottomNavigationView bnv;
    StorageReference storageReference;
    DatabaseReference reference;
    int Image_Request_Code = 7;
    long maxid = 0;
    ProgressDialog progressDialog;
    ImageView imageView;
    Uri FilePathUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        addp = findViewById(R.id.add);
        up = findViewById(R.id.upload);
        viewp = findViewById(R.id.view);
        name = findViewById(R.id.Pname);
        about = findViewById(R.id.Pabout);
        price = findViewById(R.id.Pprice);
        quantity = findViewById(R.id.Pquantity);
        imageView = findViewById(R.id.pimage);
        bnv = findViewById(R.id.bottomnav);
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        reference = FirebaseDatabase.getInstance().getReference("Plant");
        progressDialog = new ProgressDialog(SellerHomeActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        bnv.setSelectedItemId(R.id.homei);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.homei:
                        return true;
                    case R.id.profilei:
                        Intent intent1 = new Intent(SellerHomeActivity.this,SellerInformationActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.carti:
                        Intent intent2 = new Intent(SellerHomeActivity.this,SellerOrdersActivity.class);
                        startActivity(intent2);
                        return true;
                }
                return false;
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),Image_Request_Code);
            }
        });
        addp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Upload();
                if(FilePathUri!=null){
                    //String key = reference.child("Plants").push().getKey();
                    String Name = name.getText().toString().trim();
                    String About = about.getText().toString().trim();
                    String Price = price.getText().toString().trim();
                    String Quantity = quantity.getText().toString().trim();

                    if(Name.isEmpty() || About.isEmpty()){
                        Toast.makeText(SellerHomeActivity.this, "All the fields are required", Toast.LENGTH_LONG).show();
                    }
                    else{
                        progressDialog.setTitle("Uploading...");
                        progressDialog.setMessage("Please wait while we upload the data");
                        progressDialog.show();
                        StorageReference str = storageReference.child(System.currentTimeMillis()+"."+GetFileExtension(FilePathUri));
                        str.putFile(FilePathUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SellerHomeActivity.this, "Data Uploaded...", Toast.LENGTH_SHORT).show();
                                        str.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String ImageUploadId = reference.push().getKey();
                                                // String Key = ImageUploadId;
                                                // plant = new Plant(Name,About,Price,Quantity,uri.toString(),ImageUploadId, ServerValue.TIMESTAMP);
                                                plant = new Plant(Name,About,Price,Quantity,uri.toString(),ImageUploadId);
                                                // reference.child(String.valueOf(maxid+1)).setValue(plant);
                                                reference.child(ImageUploadId).setValue(plant);
                                                Intent intent = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                });
                    }

                }
                else {
                    Toast.makeText(SellerHomeActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerHomeActivity.this,PlantListActivity.class);
                startActivity(intent);
            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxid = snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Image_Request_Code && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            FilePathUri = data.getData();
            imageView.setImageURI(FilePathUri);
        }
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

//    private void Upload() {
//
//    }

}