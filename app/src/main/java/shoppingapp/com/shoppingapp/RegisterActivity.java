package shoppingapp.com.shoppingapp;

import android.app.ProgressDialog;
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

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText name_et,username_et,password_et;
    Button create_btn;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Initialization();

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_et.getText().toString().trim();
                String phone = username_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(RegisterActivity.this,"Please write your name...",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(phone)){
                    Toast.makeText(RegisterActivity.this,"Please write Mobile Number...",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this,"Please write your Password...",Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.setTitle("Create Account");
                    progressDialog.setMessage("Please wait, while we are checking the credentials.");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    ValidatePhoneNumber(name,phone,password);
                }
            }
        });
    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("users").child(phone).exists())){

                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("name",name);
                    userdataMap.put("password",password);

                        RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this,"Congratulation your account has been created",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this,"Network Error, please try again",Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                    }else {
                        Toast.makeText(RegisterActivity.this,"This" + phone + "already exit",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Please try again another phone Number",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Initialization() {
        name_et= findViewById(R.id.name_et);
        username_et = findViewById(R.id.mobile_et);
        password_et = findViewById(R.id.register_password_et);
        create_btn = findViewById(R.id.create_btn);
        progressDialog = new ProgressDialog(RegisterActivity.this);
    }
}
