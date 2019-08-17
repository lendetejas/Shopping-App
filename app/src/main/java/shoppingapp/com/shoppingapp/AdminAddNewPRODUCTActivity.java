package shoppingapp.com.shoppingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewPRODUCTActivity extends AppCompatActivity {

    private EditText productName_et,productDesc_et,productPrice_et;
    private Button add_product_btn;
    private ImageView add_product_image;
    private String CategoryName,Description,Price,pName;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String saveCurretnDate,saveCurretnTime;
    private String ProductRandomKey,downloadImageUri;
    private StorageReference ProductImageRef;
    private DatabaseReference ProducteRef;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        Initialization();
        CategoryName = getIntent().getExtras().get("category").toString();
        Toast.makeText(AdminAddNewPRODUCTActivity.this,CategoryName,Toast.LENGTH_SHORT).show();
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProducteRef = FirebaseDatabase.getInstance().getReference().child("Products");

        add_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            add_product_image.setImageURI(ImageUri);
        }
    }

    private void Initialization() {

        productName_et = findViewById(R.id.product_et);
        productDesc_et = findViewById(R.id.product_desc_et);
        productPrice_et = findViewById(R.id.product_price_et);
        add_product_btn = findViewById(R.id.add_pro_btn);
        add_product_image = findViewById(R.id.product_add_img);
        progressDialog = new ProgressDialog(AdminAddNewPRODUCTActivity.this);
    }
    private void ValidateProductData(){
        Description = productDesc_et.getText().toString().trim();
        pName = productName_et.getText().toString().trim();
        Price = productPrice_et.getText().toString().trim();

        if (ImageUri == null){
            Toast.makeText(AdminAddNewPRODUCTActivity.this,"Product Image Mamdatory",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Description)){
            Toast.makeText(AdminAddNewPRODUCTActivity.this,"Please Enter Product Desc",Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(pName)){
            Toast.makeText(AdminAddNewPRODUCTActivity.this,"Please Enter Product Name",Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(Price)){
            Toast.makeText(AdminAddNewPRODUCTActivity.this,"Please Enter Product Price",Toast.LENGTH_SHORT).show();

        }else {
            StoreProductInformation();
        }

    }

    private void StoreProductInformation() {

        progressDialog.setTitle("Adding New Product");
        progressDialog.setMessage("Please wait, while we are upload Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurretnDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurretnTime = currentTime.format(calendar.getTime());

        ProductRandomKey = saveCurretnDate + saveCurretnTime;


        final StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + ProductRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewPRODUCTActivity.this,"Error : " + message,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewPRODUCTActivity.this,"ProductImage Upload Successfully...",Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();

                        }
                        downloadImageUri = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){

                            downloadImageUri =task.getResult().toString();
                            Toast.makeText(AdminAddNewPRODUCTActivity.this,"got the Product Image Save Database Successfully...",Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDatabase() {
        HashMap<String, Object> productmap = new HashMap<>();

        productmap.put("pid", ProductRandomKey);
        productmap.put("date", saveCurretnDate);
        productmap.put("time", saveCurretnTime);
        productmap.put("description", Description);
        productmap.put("image", downloadImageUri);
        productmap.put("category", CategoryName);
        productmap.put("price", Price);
        productmap.put("pname", pName);
        ProducteRef.child(ProductRandomKey).updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Intent intent = new Intent(AdminAddNewPRODUCTActivity.this,AdminCategoryActivity.class);
                    startActivity(intent);

                    progressDialog.dismiss();
                    Toast.makeText(AdminAddNewPRODUCTActivity.this,"Product is added Successfully...",Toast.LENGTH_SHORT).show();

                }else {
                    progressDialog.dismiss();
                    String message = String.valueOf(task.getException());
                    Toast.makeText(AdminAddNewPRODUCTActivity.this,message,Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
