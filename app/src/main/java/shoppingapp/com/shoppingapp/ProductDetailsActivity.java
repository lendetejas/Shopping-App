package shoppingapp.com.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import shoppingapp.com.shoppingapp.Model.Products;
import shoppingapp.com.shoppingapp.Prevalent.Prevalent;

public class ProductDetailsActivity extends AppCompatActivity {

    private ElegantNumberButton numberButton;
    private Button addToCartButton;
    private ImageView ProductImage;
    private TextView product_name,product_desc,product_price;
    private String productID = "",state = "Normal";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Initialization();

        GetProductDetails(productID);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderStatus();
    }

    private void addingToCartList() {

        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());


        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForTime.getTime());


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");


        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("pid", productID);
        cartMap.put("pname", product_name.getText().toString().trim());
        cartMap.put("price", product_price.getText().toString().trim());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");


        cartListRef.child("User View").child(Prevalent.currentOnLineUsers.getPhone()).child("products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            cartListRef.child("Admin View").child(Prevalent.currentOnLineUsers.getPhone())
                                    .child("products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                Toast.makeText(ProductDetailsActivity.this,"Added to Cart List...",Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void GetProductDetails(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);


                    product_name.setText(products.getPname());
                    product_desc.setText(products.getDescription());
                    product_price.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(ProductImage);

                    Log.d("getPname","getPnamegetPnamegetPname"+products.getPname());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void CheckOrderStatus(){

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnLineUsers.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.exists()){
                        String shippingState = dataSnapshot.child("state").getValue().toString();
                        if (shippingState.equals("shipped")){
                           state = "Order Shipped";

                            Toast.makeText(ProductDetailsActivity.this,"you can purchase, onec you received your first final order",Toast.LENGTH_SHORT).show();

                        }else if (shippingState.equals("not shipped")){
                          state = "Order Placed";
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void Initialization() {
        productID = getIntent().getStringExtra("pid");
        ProductImage = findViewById(R.id.product_details_img);
        product_name = findViewById(R.id.product_details_name);
        product_desc = findViewById(R.id.product_details_desc);
        product_price = findViewById(R.id.product_details_price);
        addToCartButton = findViewById(R.id.add_to_cart_product);
        numberButton = findViewById(R.id.elegentButton);

        Log.d("productID","productIDproductIDproductIDproductID"+productID);
    }
}
