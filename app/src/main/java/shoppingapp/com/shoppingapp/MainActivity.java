package shoppingapp.com.shoppingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import shoppingapp.com.shoppingapp.Model.Users;
import shoppingapp.com.shoppingapp.Prevalent.Prevalent;

public class MainActivity extends AppCompatActivity {
    private Button joinNowButton,loginButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initialization();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                 startActivity(intent);
            }
        });
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });




        String UserPhonekey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if (!TextUtils.isEmpty(UserPhonekey) && !TextUtils.isEmpty(UserPasswordKey)){
            AllowAccess(UserPhonekey,UserPasswordKey);

            progressDialog.setTitle("Login Account");
            progressDialog.setMessage("Please wait, while we are checking the credentials.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

    }

    private void AllowAccess(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()){

                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)){
                        if (usersData.getPassword().equals(password)){
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Logged in Successfully...",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }else {

                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Password is incorrect...",Toast.LENGTH_SHORT).show();

                        }
                    }

                }else{
                    Toast.makeText(MainActivity.this,"Account with this " + phone + " number not exists.",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"you need to create a new Account.",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Initialization() {

        Paper.init(MainActivity.this);
        joinNowButton = findViewById(R.id.join_btn);
        loginButton = findViewById(R.id.login_btn);
        progressDialog = new ProgressDialog(MainActivity.this);
    }
}
