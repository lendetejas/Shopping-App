package shoppingapp.com.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import shoppingapp.com.shoppingapp.Prevalent.Prevalent;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText name_et,address_et,city_et,phone_et;
    private Button confirm_btn;
    private String totalAmount = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        totalAmount = getIntent().getStringExtra("Total Price");
        Initialization();

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name_et.getText().toString())){
                    Toast.makeText(ConfirmFinalOrderActivity.this,"Please Enter Name",Toast.LENGTH_SHORT).show();

                }
                else if (TextUtils.isEmpty(phone_et.getText().toString())){
                    Toast.makeText(ConfirmFinalOrderActivity.this,"Please Enter Phone Number",Toast.LENGTH_SHORT).show();

                }
               else if (TextUtils.isEmpty(city_et.getText().toString())){
                    Toast.makeText(ConfirmFinalOrderActivity.this,"Please Enter City",Toast.LENGTH_SHORT).show();

                }
                else if (TextUtils.isEmpty(address_et.getText().toString())){
                    Toast.makeText(ConfirmFinalOrderActivity.this,"Please Enter Address",Toast.LENGTH_SHORT).show();

                }else{
                    ConfirmOrder();
                }
            }
        });
    }

    private void ConfirmOrder() {
       final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());


        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForTime.getTime());


        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");


        final HashMap<String, Object> ordersMap = new HashMap<>();

        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", name_et.getText().toString().trim());
        ordersMap.put("phone", phone_et.getText().toString().trim());
        ordersMap.put("address", address_et.getText().toString().trim());
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("city", city_et.getText().toString().trim());
        ordersMap.put("state", "not shipped");


        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                            .child(Prevalent.currentOnLineUsers.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this,"your final order has been placed successful",Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

    }

    private void Initialization() {
        name_et = findViewById(R.id.confirm_userName);
        address_et = findViewById(R.id.confirm_userAddress);
        city_et = findViewById(R.id.confirm_userCity);
        phone_et = findViewById(R.id.confirm_userPhone);
        confirm_btn = findViewById(R.id.confirm_btn);
    }

}
