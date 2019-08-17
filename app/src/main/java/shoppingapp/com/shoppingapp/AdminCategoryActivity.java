package shoppingapp.com.shoppingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView sweather,tshirts,female_dresses,sports,
            purses_bags,books,hats,shoess,headphoness,mobiles,laptops,watches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        Initialization();

        sweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPRODUCTActivity.class);
                intent.putExtra("category","sweather");
                startActivity(intent);
            }
        });
        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPRODUCTActivity.class);
                intent.putExtra("category","tshirts");
                startActivity(intent);
            }
        });
        female_dresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPRODUCTActivity.class);
                intent.putExtra("category","female dresses");
                startActivity(intent);
            }
        });
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPRODUCTActivity.class);
                intent.putExtra("category","sports");
                startActivity(intent);
            }
        });  purses_bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPRODUCTActivity.class);
                intent.putExtra("category","purses bags");
                startActivity(intent);
            }
        });  books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPRODUCTActivity.class);
                intent.putExtra("category","books");
                startActivity(intent);
            }
        });  hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPRODUCTActivity.class);
                intent.putExtra("category","hats");
                startActivity(intent);
            }
        });  shoess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPRODUCTActivity.class);
                intent.putExtra("category","shoess");
                startActivity(intent);
            }
        });  headphoness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPRODUCTActivity.class);
                intent.putExtra("category","headphoness");
                startActivity(intent);
            }
        });  mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPRODUCTActivity.class);
                intent.putExtra("category","mobiles");
                startActivity(intent);
            }
        });
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPRODUCTActivity.class);
                intent.putExtra("category","laptops");
                startActivity(intent);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPRODUCTActivity.class);
                intent.putExtra("category","watches");
                startActivity(intent);
            }
        });

    }

    private void Initialization() {
        sweather = findViewById(R.id.sweather);
        tshirts = findViewById(R.id.tshirts);
        female_dresses = findViewById(R.id.female_dresses);
        sports = findViewById(R.id.sports);
        purses_bags = findViewById(R.id.purses_bags);
        books = findViewById(R.id.books);
        hats = findViewById(R.id.hats);
        shoess = findViewById(R.id.shoess);
        headphoness = findViewById(R.id.headphoness);
        mobiles = findViewById(R.id.mobiles);
        laptops = findViewById(R.id.laptops);
        watches = findViewById(R.id.watches);
    }
}
