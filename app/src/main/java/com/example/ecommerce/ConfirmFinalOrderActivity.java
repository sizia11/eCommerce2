package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEditText,phoneEditText,adressEditText,cityEditText;
    private Button confirmOrderBtn;
    private String totalAmount ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this,"Total Price = " + totalAmount,Toast.LENGTH_SHORT).show();

        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText) findViewById(R.id.shippment_name);
        phoneEditText = (EditText) findViewById(R.id.shippment_phone_number);
        adressEditText = (EditText) findViewById(R.id.shippment_adresse);
        cityEditText = (EditText) findViewById(R.id.shippment_city);
    }
}