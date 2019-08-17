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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import shoppingapp.com.shoppingapp.Model.Users;
import shoppingapp.com.shoppingapp.Prevalent.Prevalent;

public class LoginActivity extends AppCompatActivity {

    EditText username_et,password_et;
    Button login_btn;
    com.rey.material.widget.CheckBox checkBox;
    TextView forget_tv;
    TextView NotAdminLink;
    TextView AdminLink;
    ProgressDialog progressDialog;
    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Initialization();

      login_btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              LoginUser();
          }
      });

      AdminLink.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              login_btn.setText("Login Admin");
              AdminLink.setVisibility(View.INVISIBLE);
              NotAdminLink.setVisibility(View.VISIBLE);
              parentDbName = "Admin";
          }
      });

      NotAdminLink.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              NotAdminLink.setVisibility(View.INVISIBLE);
              AdminLink.setVisibility(View.VISIBLE);
              login_btn.setText("Users");

          }
      });

    }

    private void LoginUser() {
        String phone = username_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(LoginActivity.this,"Please write Mobile Number...",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this,"Please write your Password...",Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setTitle("Create Account");
            progressDialog.setMessage("Please wait, while we are checking the credentials.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            ValidatePhoneNumber(phone,password);
        }
    }

    private void ValidatePhoneNumber(final String phone, final String password) {

        if (checkBox.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()){

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)){
                        if (usersData.getPassword().equals(password)){
                            progressDialog.dismiss();

                            if (parentDbName.equals("Admins")){
                                Toast.makeText(LoginActivity.this,"Admin Logged in Successfully...",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);
                            }else if (parentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this,"Logged in Successfully...",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                Prevalent.currentOnLineUsers = usersData;
                                startActivity(intent);
                            }

                        }else {

                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,"Password is incorrect...",Toast.LENGTH_SHORT).show();

                        }
                    }

                }else{
                    Toast.makeText(LoginActivity.this,"Account with this " + phone + " number not exists.",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"you need to create a new Account.",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void Initialization() {

        username_et = findViewById(R.id.username_et);
        password_et = findViewById(R.id.password_et);
        checkBox = (com.rey.material.widget.CheckBox)findViewById(R.id.checkBox_pass);
        forget_tv = findViewById(R.id.forget_tv);
        login_btn = findViewById(R.id.login_btn);
        AdminLink = findViewById(R.id.IamAdmin);
        NotAdminLink  =findViewById(R.id.IamNotAdmin);
        progressDialog = new ProgressDialog(LoginActivity.this);

        Paper.init(LoginActivity.this);
    }
}
