package com.example.greenrise_sgp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class SellerHomeActivity extends AppCompatActivity {
    public static final int NOTIFICATION_ID = 10001;
    private final static String CHANNEL_ID = "default";
    Button addp, viewp, up;
    EditText name, about, price, quantity;
    Plant plant;
    Flower flower;
    Pebble pebble;
    Pots pots;
    Fertilizer fertilizer;
    FirebaseAuth mAuth;
    BottomNavigationView bnv;
    StorageReference storageReference;
    DatabaseReference reference, flowerref, fertilizerref,pebblesref, potsref, ref;
    Spinner spinner;
    String[] types = {"Choose Category", "Plant", "Fertilizer", "Flower","Pebbles","Pots"};
    int Image_Request_Code = 7;
    long maxid = 0;
    ProgressDialog progressDialog;
    ImageView imageView;
    Uri FilePathUri;
    Drawable drawable;

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
        spinner = findViewById(R.id.spinnertype);
        mAuth = FirebaseAuth.getInstance();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item, types);
        adapter.setDropDownViewResource(com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        drawable = getDrawable(R.drawable.borderr);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                name.setBackground(drawable);
            }
        });
        about.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                about.setBackground(drawable);
            }
        });
        price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                price.setBackground(drawable);
            }
        });
        quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                quantity.setBackground(drawable);
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        reference = FirebaseDatabase.getInstance().getReference("Plant");
        flowerref = FirebaseDatabase.getInstance().getReference("Flowers");
        fertilizerref = FirebaseDatabase.getInstance().getReference("Fertilizers");
        pebblesref = FirebaseDatabase.getInstance().getReference("Pebbles");
        potsref = FirebaseDatabase.getInstance().getReference("Pots");
        ref = FirebaseDatabase.getInstance().getReference("Sellers");
        progressDialog = new ProgressDialog(SellerHomeActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        bnv.setSelectedItemId(R.id.homei);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homei:
                        return true;
                    case R.id.profilei:
                        Intent intent1 = new Intent(SellerHomeActivity.this, SellerInformationActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.carti:
                        Intent intent2 = new Intent(SellerHomeActivity.this, SellerOrdersActivity.class);
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
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
            }
        });
        addp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FilePathUri != null) {
                    String Name = name.getText().toString().trim();
                    String About = about.getText().toString().trim();
                    String Price = price.getText().toString().trim();
                    String Quantity = quantity.getText().toString().trim();
                    if (Name.isEmpty() || About.isEmpty() || Price.isEmpty() || Quantity.isEmpty()) {
                        Toast.makeText(SellerHomeActivity.this, "All the fields are required", Toast.LENGTH_LONG).show();
                    } else if (spinner.getSelectedItem() == "Choose Category") {
                        Toast.makeText(SellerHomeActivity.this, "Select category", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.setTitle("Uploading...");
                        progressDialog.setMessage("Please wait while we upload the data");
                        progressDialog.show();
                        if (spinner.getSelectedItem() == "Plant") {
                            StorageReference str = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
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
                                                    plant = new Plant(Name, About, Price, Quantity, uri.toString(), ImageUploadId);
                                                    reference.child(ImageUploadId).setValue(plant);
                                                    Intent intent = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                                                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.logo, null);
                                                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                                                    Bitmap largeicon = bitmapDrawable.getBitmap();
                                                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    Notification notification;
                                                    Calendar calendar = Calendar.getInstance();
                                                    SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
                                                    String strDate = mdformat.format(calendar.getTime());
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                        notification = new Notification.Builder(SellerHomeActivity.this)
                                                                .setLargeIcon(largeicon)
                                                                .setSmallIcon(R.drawable.logo)
                                                                .setContentTitle("Product Added")
                                                                .setContentText("New product is added")
                                                                .setSubText(strDate)
                                                                .setTicker("New Product added")
                                                                .setChannelId(CHANNEL_ID)
                                                                .build();

                                                        manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
                                                    } else {
                                                        notification = new Notification.Builder(SellerHomeActivity.this)
                                                                .setLargeIcon(largeicon)
                                                                .setSmallIcon(R.drawable.logo)
                                                                .setContentText("New product is added")
                                                                .setSubText("Product Added")
                                                                .build();
                                                    }
                                                    manager.notify(NOTIFICATION_ID, notification);
                                                    startActivity(intent);
                                                }
                                            });

                                        }
                                    });
                        }
                        else if (spinner.getSelectedItem() == "Flower") {
                            StorageReference str = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
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
                                                    String uid = mAuth.getCurrentUser().getUid();
                                                    flower = new Flower(Name, About, Price, Quantity, uri.toString(), ImageUploadId, uid);
                                                    flowerref.child(ImageUploadId).setValue(flower);
                                                    Intent intent = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                                                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.logo, null);
                                                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                                                    Bitmap largeicon = bitmapDrawable.getBitmap();
                                                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    Notification notification;
                                                    Calendar calendar = Calendar.getInstance();
                                                    SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
                                                    String strDate = mdformat.format(calendar.getTime());
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                        notification = new Notification.Builder(SellerHomeActivity.this)
                                                                .setLargeIcon(largeicon)
                                                                .setSmallIcon(R.drawable.logo)
                                                                .setContentTitle("Product Added")
                                                                .setContentText("New product is added")
                                                                .setSubText(strDate)
                                                                .setTicker("New Product added")
                                                                .setChannelId(CHANNEL_ID)
                                                                .build();

                                                        manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
                                                    } else {
                                                        notification = new Notification.Builder(SellerHomeActivity.this)
                                                                .setLargeIcon(largeicon)
                                                                .setSmallIcon(R.drawable.logo)
                                                                .setContentText("New product is added")
                                                                .setSubText("Product Added")
                                                                .build();
                                                    }
                                                    manager.notify(NOTIFICATION_ID, notification);
                                                    startActivity(intent);
                                                }
                                            });

                                        }
                                    });
                        }
                        else if (spinner.getSelectedItem() == "Fertilizer") {
                            StorageReference str = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
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
                                                    fertilizer = new Fertilizer(Name, About, Price, Quantity, uri.toString(), ImageUploadId);
                                                    fertilizerref.child(ImageUploadId).setValue(fertilizer);
                                                    Intent intent = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                                                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.logo, null);
                                                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                                                    Bitmap largeicon = bitmapDrawable.getBitmap();
                                                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    Notification notification;
                                                    Calendar calendar = Calendar.getInstance();
                                                    SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
                                                    String strDate = mdformat.format(calendar.getTime());
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                        notification = new Notification.Builder(SellerHomeActivity.this)
                                                                .setLargeIcon(largeicon)
                                                                .setSmallIcon(R.drawable.logo)
                                                                .setContentTitle("Product Added")
                                                                .setContentText("New product is added")
                                                                .setSubText(strDate)
                                                                .setTicker("New Product added")
                                                                .setChannelId(CHANNEL_ID)
                                                                .build();

                                                        manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
                                                    } else {
                                                        notification = new Notification.Builder(SellerHomeActivity.this)
                                                                .setLargeIcon(largeicon)
                                                                .setSmallIcon(R.drawable.logo)
                                                                .setContentText("New product is added")
                                                                .setSubText("Product Added")
                                                                .build();
                                                    }
                                                    manager.notify(NOTIFICATION_ID, notification);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    });
                        }
                        else if (spinner.getSelectedItem() == "Pebbles") {
                            StorageReference str = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
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
                                                    pebble = new Pebble(Name, About, Price, Quantity, uri.toString(), ImageUploadId);
                                                    pebblesref.child(ImageUploadId).setValue(pebble);
                                                    Intent intent = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                                                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.logo, null);
                                                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                                                    Bitmap largeicon = bitmapDrawable.getBitmap();
                                                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    Notification notification;
                                                    Calendar calendar = Calendar.getInstance();
                                                    SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
                                                    String strDate = mdformat.format(calendar.getTime());
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                        notification = new Notification.Builder(SellerHomeActivity.this)
                                                                .setLargeIcon(largeicon)
                                                                .setSmallIcon(R.drawable.logo)
                                                                .setContentTitle("Product Added")
                                                                .setContentText("New product is added")
                                                                .setSubText(strDate)
                                                                .setTicker("New Product added")
                                                                .setChannelId(CHANNEL_ID)
                                                                .build();

                                                        manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
                                                    } else {
                                                        notification = new Notification.Builder(SellerHomeActivity.this)
                                                                .setLargeIcon(largeicon)
                                                                .setSmallIcon(R.drawable.logo)
                                                                .setContentText("New product is added")
                                                                .setSubText("Product Added")
                                                                .build();
                                                    }
                                                    manager.notify(NOTIFICATION_ID, notification);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    });
                        }
                        else if (spinner.getSelectedItem() == "Pots") {
                            StorageReference str = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
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
                                                    pots = new Pots(Name, About, Price, Quantity, uri.toString(), ImageUploadId);
                                                    potsref.child(ImageUploadId).setValue(pots);
                                                    Intent intent = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                                                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.logo, null);
                                                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                                                    Bitmap largeicon = bitmapDrawable.getBitmap();
                                                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    Notification notification;
                                                    Calendar calendar = Calendar.getInstance();
                                                    SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
                                                    String strDate = mdformat.format(calendar.getTime());
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                        notification = new Notification.Builder(SellerHomeActivity.this)
                                                                .setLargeIcon(largeicon)
                                                                .setSmallIcon(R.drawable.logo)
                                                                .setContentTitle("Product Added")
                                                                .setContentText("New product is added")
                                                                .setSubText(strDate)
                                                                .setTicker("New Product added")
                                                                .setChannelId(CHANNEL_ID)
                                                                .build();

                                                        manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
                                                    } else {
                                                        notification = new Notification.Builder(SellerHomeActivity.this)
                                                                .setLargeIcon(largeicon)
                                                                .setSmallIcon(R.drawable.logo)
                                                                .setContentText("New product is added")
                                                                .setSubText("Product Added")
                                                                .build();
                                                    }
                                                    manager.notify(NOTIFICATION_ID, notification);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    });
                        }
                    }

                } else {
                    Toast.makeText(SellerHomeActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerHomeActivity.this, GroupActivity.class);
                startActivity(intent);
            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
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
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            imageView.setImageURI(FilePathUri);
        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
}