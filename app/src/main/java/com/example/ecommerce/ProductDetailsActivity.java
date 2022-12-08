package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.model.Products;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
 private Button addtocart;
 private ImageView productimage;
 private TextView productprice,productdescription,productname;
 private String productId;
 private ElegantNumberButton numberButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productId=getIntent().getStringExtra("pid");
        addtocart = (Button) findViewById(R.id.detail_add_to_cart);
        numberButton= (ElegantNumberButton) findViewById(R.id.number_btn);
        productimage = (ImageView) findViewById(R.id.product_image_details);
        productname = (TextView) findViewById(R.id.product_name_details);
        productdescription = (TextView) findViewById(R.id.product_discription_details);
        productprice = (TextView) findViewById(R.id.product_price_details);
        getProductDetail(productId);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
    }


    private void addingToCartList(){
        String saveCurrentTime,savecurrentDate;
        Calendar calendar =  Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        savecurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
       final  DatabaseReference cartListRef =  FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String ,Object > cartMap = new HashMap<>();
        cartMap.put("pid",productId );
        cartMap.put("pname",productname.getText().toString() );
        cartMap.put("price",productprice.getText().toString() );
        cartMap.put("date",savecurrentDate );
        cartMap.put("time",saveCurrentTime );
        cartMap.put("quantity",numberButton.getNumber() );
        cartMap.put("discount","" );
        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
    .child("Products").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //remove from here
                            Toast.makeText(ProductDetailsActivity.this, "add to cart list", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                            startActivity(intent);
                            //to here (when finish testing)
                            /**cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productId).updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this, "add to cart list", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });*/
                        }
                    }
                });
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