package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerce.model.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {
 private FloatingActionButton addtocart;
 private ImageView productimage;
 private TextView productprice,productdescription,productname;
 private String productId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productId=getIntent().getStringExtra("pid");
        addtocart = (FloatingActionButton) findViewById(R.id.product_to_cart);
        productimage = (ImageView) findViewById(R.id.product_image_details);
        productname = (TextView) findViewById(R.id.product_name_details);
        productdescription = (TextView) findViewById(R.id.product_discription_details);
        productprice = (TextView) findViewById(R.id.product_price_details);

        getProductDetail(productId);

    }
   private void getProductDetail(String productId){
       DatabaseReference ProductsRef =  FirebaseDatabase.getInstance().getReference().child("Products");
       ProductsRef.child(productId).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   Products products =snapshot.getValue(Products.class);
                   productname.setText(products.getPname());
                   productprice.setText(products.getPrice());
                   productdescription.setText(products.getDescription());
                   Picasso.get().load(products.getImage()).into(productimage);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
   }
}