package com.example.greenrise_sgp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class checkoutFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String totalPrice;
    Button btn;
    String SECRET_KEY="sk_test_51LuUZWSJo5nDYZPwg2mHFMw0BFgkmG5rhmhKZBlT30PDwmvgsoPqNMTBxKqPgzk8uDRy8Ne5DGO3bc3ow7E67YVO001jC2guWq";
    String PUBLISH_KEY="pk_test_51LuUZWSJo5nDYZPwvoDbXNbQIXT3GE7FpUtYx9FbuLNGKiOT3CeV0xjleW0aPP47sCoUqnjo9xsZWXv6i9XcoQNJ00fKk2dg7R";
  PaymentSheet paymentSheet;
  String customerID;
    String EmphericalKey;
  String clientSecret;
  View view1;
    TextView tv;
    String name,unitprice,currentdate,currenttime,totalquantity,totalprice,UUID,SUID,parent,image;
    String quantityInPlants;
    RecyclerView rv;
    myadapter adapter;
    static int i=0;
    public checkoutFragment() {
        // Required empty public constructor
    }
    public checkoutFragment(String totalPrice)
    {
        this.totalPrice=totalPrice;
    }


    public static checkoutFragment newInstance(String param1, String param2) {
        checkoutFragment fragment = new checkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        view1=view;
        tv = view.findViewById(R.id.textView);
        TextView tv1 = view.findViewById(R.id.quote);
        FirebaseDatabase db1 = FirebaseDatabase.getInstance();
        DatabaseReference quotes = db1.getReference("Quotes");
        quotes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Random random = new Random();
                    int random1=random.nextInt(21)+1;
                    String k1="q"+random1;
                    DataSnapshot snapshot1=snapshot.child(String.valueOf(random1));
                tv1.setText("\""+snapshot1.child(k1).getValue().toString()+"\"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn = view.findViewById(R.id.button6);
        tv.setText("Your total amount is:"+totalPrice);
        btn.setEnabled(false);
        PaymentConfiguration.init(view.getContext(),PUBLISH_KEY);
        paymentSheet=new PaymentSheet(this,paymentSheetResult -> {
          onPaymentResult(paymentSheetResult);
        });
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);
                            customerID=obj.getString("id");

                            getEmphericalKey(customerID);

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+SECRET_KEY);
                return headers;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  gotoURL("https://buy.stripe.com/test_28o3fK9a0glTcsEfYZ?default_price="+totalPrice);
                PaymentFlow();
            }
        });
        return view;
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof  PaymentSheetResult.Completed)
        {
            Toast.makeText(view1.getContext(),"Succesful payment",Toast.LENGTH_SHORT).show();
            tv = view1.findViewById(R.id.textView);
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference orders = db.getReference("Orders");
            DatabaseReference cart =db.getReference("CartPresentUser");
            DatabaseReference cart1 =db.getReference("Cart");
            DatabaseReference plants =db.getReference("Plants");
            cart.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        name=dataSnapshot.child("name").getValue().toString();
                        unitprice=dataSnapshot.child("unitprice").getValue().toString();
                        currentdate=dataSnapshot.child("currentdate").getValue().toString();
                        currenttime=dataSnapshot.child("currenttime").getValue().toString();
                        totalquantity=dataSnapshot.child("totalquantity").getValue().toString();
                        totalprice=dataSnapshot.child("totalprice").getValue().toString();
                        UUID=dataSnapshot.child("uuid").getValue().toString();
                        SUID=dataSnapshot.child("suid").getValue().toString();
                        parent=dataSnapshot.child("parent").getValue().toString();
                        image=dataSnapshot.child("image").getValue().toString();
                        cartModel cm = new cartModel(name, unitprice, currentdate, currenttime, totalquantity, totalprice,UUID, SUID,parent,image);
                        orders.child(parent).setValue(cm);
                      //  i++;
                        plants.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap updateq=new HashMap();
                                quantityInPlants=String.valueOf(Integer.parseInt(snapshot.child("quantity").getValue().toString())+Integer.parseInt(totalquantity));
                                updateq.put("quantity",quantityInPlants);
                                plants.child(parent).updateChildren(updateq);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        cart.child(parent).removeValue();
                        cart1.child(parent).removeValue();
                        tv.setText("You have nothing in cart!");
                    }

                }



                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }


    private void getEmphericalKey(String customerID){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);
                            EmphericalKey=obj.getString("id");

                            getClientSecret(customerID,EmphericalKey);

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+SECRET_KEY);
                headers.put("Stripe-Version","2022-08-01");
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> body = new HashMap<>();
                body.put("customer",customerID);
                return body;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(view1.getContext());
        requestQueue.add(stringRequest);
    }

    private void getClientSecret(String customerID, String emphericalKey) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);
                            clientSecret=obj.getString("client_secret");
                           // Toast.makeText(view1.getContext(),"Now press button",Toast.LENGTH_SHORT).show();
                            btn.setEnabled(true);

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+SECRET_KEY);
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> body = new HashMap<>();
                body.put("customer",customerID);
                body.put("amount",totalPrice+"00");
                body.put("currency","INR");
                body.put("automatic_payment_methods[enabled]","true");
                return body;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(view1.getContext());
        requestQueue.add(stringRequest);
    }

    private void PaymentFlow() {
        paymentSheet.presentWithPaymentIntent(clientSecret,new PaymentSheet.Configuration("GreenRise",
                new PaymentSheet.CustomerConfiguration(customerID,EmphericalKey)));
    }

    public void onBackPressed()
    {
        AppCompatActivity activity = (AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new BuyFragment()).addToBackStack(null).commit();

    }
    private  void gotoURL(String url)
    {
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}