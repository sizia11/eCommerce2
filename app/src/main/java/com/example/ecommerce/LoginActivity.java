package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.rey.material.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.model.Users;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;

    //we will use this variable (parentDbName) for the database check
    private String parentDbName = "Users";
    //remember me  is for when you close the app it will keep you logged in
    private CheckBox chkBoxRememberMe;

    private TextView AdminLink, NotAdminLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        loadingBar = new ProgressDialog(this);

       chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);



        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {   //calling login methode that we created
                LoginUser();
            }
        });
        /** by default any user when entering to login the parentDbName="Users" so when you enter
         * the id and password the controller will check users table ,so we created
         * some sored of switch that switch the controller to dearch through admins table
         */
        //search through admins table when authenticate
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });
        //search through users table when authenticate
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private void LoginUser()
    {    //getting the  inputs
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
        //checking if they are not empty
        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            // calling a mthode that we created
            AllowAccessToAccount(phone, password);
        }
    }
    private void AllowAccessToAccount(final String phone, final String password)
    {
        //checking if remember me is checked
        if(chkBoxRememberMe.isChecked())
        { //storing (writing)the data to the phone memorie
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }




        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {   //reminder : private String parentDbName = "Users";
                //searching for the user if he exist in the data base
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {  // getting and giving user data from database to an object (from entity to a model)
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    //this condition is not nesccary to do
                   // if (usersData.getPhone().equals(phone))
                    //{
                    //checking if the password match the user (id)
                        if (usersData.getPassword().equals(password))
                        {     //checking if the user is admin
                            if (parentDbName.equals("Admins"))
                            {//to create admin user go to data base and make sure every input is string (ABC option in firebase)
                                Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            } //else the user is normale user
                            else if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                // saving the username(phone) and passwod into the phone memorie
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }
                        //if the password is incorrect
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    //}
                }
                //if the account doesn't existe
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}