package shoppingapp.com.shoppingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import shoppingapp.com.shoppingapp.Model.Cart;
import shoppingapp.com.shoppingapp.Prevalent.Prevalent;
import shoppingapp.com.shoppingapp.ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {

    private TextView totalPrice_tv,cart_msg;
    private Button nxt_btn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String overTotalPrice = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Initialization();

        nxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderStatus();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View")

                .child(Prevalent.currentOnLineUsers.getPhone())
                .child("products"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                holder.cartProductName_tv.setText(model.getPname());
                holder.cartProductPrice_tv.setText("Price = " +model.getPrice());
                holder.cartProductQuantity_tv.setText("Quantity = " +model.getQuantity());

                int oneTypeProductPrice = (Integer.valueOf(model.getPrice())) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductPrice;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CartActivity.this);
                        builder1.setMessage("Write your message here.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Remove",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        cartListRef.child("User View")
                                                .child(Prevalent.currentOnLineUsers.getPhone())
                                                .child("products")
                                                .child(model.getPid())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(CartActivity.this,"Item Remove Successfully",Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });
                                    }
                                });

                        builder1.setNegativeButton(
                                "Edit",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                        intent.putExtra("pid",model.getPid());
                                        startActivity(intent);
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private void CheckOrderStatus(){

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnLineUsers.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String shippingName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")){
                        totalPrice_tv.setText("Dear " + shippingName + "\n order is shipped is successfully");
                        recyclerView.setVisibility(View.GONE);
                        cart_msg.setVisibility(View.VISIBLE);
                        nxt_btn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this,"you can purchase, onec you received your first final order",Toast.LENGTH_SHORT).show();

                    }else if (shippingState.equals("not shipped")){
                        totalPrice_tv.setText("Shipping State = Not Shipped");
                        recyclerView.setVisibility(View.GONE);
                        cart_msg.setVisibility(View.VISIBLE);
                        nxt_btn.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void Initialization() {
        totalPrice_tv = findViewById(R.id.total_price);
        nxt_btn = findViewById(R.id.cart_next_btn);
        recyclerView = findViewById(R.id.cart_recyclerview);
        cart_msg = findViewById(R.id.cart_msg);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CartActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        totalPrice_tv.setText("Total Price = $" + String.valueOf(overTotalPrice));
    }
}
