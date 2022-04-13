package com.project.lasalle.communitybank;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import EnumClasses.AccountType;
import Model.Account;
import Model.Random;
import Model.User;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgLogo;
    TextView txtAlreadyReg;
    TextInputEditText firstName,lastName,password,email,phoneNumber;
    Button btnSignUp;
    String stFirstname,stLastName,stPassword,stEmail,stPhoneNumber;
    TextInputLayout txtEmail,txtPassword,txtPhone,txtFirstName,txtLastName;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    public void init(){
        imgLogo = findViewById(R.id.imgLogo);
        imgLogo.setImageResource(R.drawable.bank_login_logo);
        txtAlreadyReg = findViewById(R.id.txtAlreadyReg);
        txtAlreadyReg.setOnClickListener(this);
        firstName = findViewById(R.id.edFirstname);
        lastName = findViewById(R.id.edLastname);
        email = findViewById(R.id.edemail);
        password = findViewById(R.id.edpassword);
        phoneNumber = findViewById(R.id.edPhoneNumber);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword1);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtPhone = findViewById(R.id.txtPhoneNumber);

    }

    public void addUser(View view){

        stFirstname = firstName.getText().toString();
        stLastName = lastName.getText().toString();
        stEmail = email.getText().toString();
        stPassword = password.getText().toString();
        stPhoneNumber = phoneNumber.getText().toString();

        if (stFirstname.length() == 0 ){
            txtFirstName.setError("Please enter first name");
        }
        if (stLastName.length() == 0){
            txtLastName.setError("Please enter last name");
        }
        if (stEmail.length() == 0){
            txtEmail.setError("Please enter email");
        }
        if( stPhoneNumber.length() == 0){
            txtPhone.setError("Please enter the phone number");
        }
        if(stPassword.length()<=0){
            txtPassword.setError("Please enter the password");
        }
        else{
            txtFirstName.setErrorEnabled(false);
            txtLastName.setErrorEnabled(false);
            txtEmail.setErrorEnabled(false);
            txtPhone.setErrorEnabled(false);
            txtPassword.setErrorEnabled(false);
            createUser(view);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUp:
                addUser(view);
                break;
            case R.id.txtAlreadyReg:
                finishAfterTransition();
                break;
            default:
                break;
        }

    }

    public void createUser(View view){

        DocumentReference userDocRef = db.document("Users/"+stEmail);

        userDocRef.get().addOnSuccessListener(task->{
            if(task.exists()){
                Snackbar.make(view,"Email already exist!!\nPlease use another email.",Snackbar.LENGTH_LONG).show();
            }
            else {
                User user = new User(stFirstname,stLastName,stEmail,stPhoneNumber,stPassword,false);
                Account account = new Account(Random.createAccountNumber(),0.00, AccountType.Personal);

                userDocRef.set(user).addOnSuccessListener(task1 -> {
                    db.collection("Users").document(stEmail).collection("Accounts").document(AccountType.Personal.toString()).set(account).addOnSuccessListener(unused -> {
                        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                        startActivity(intent);
                        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", stEmail);
                        editor.apply();
                    }).addOnFailureListener(e -> Log.e("Firebase Error","Error in data!!!" + e));
                }).addOnFailureListener(e -> { Log.e("Firebase Error","Error in data!!!" + e);
                });
            }
        }).addOnFailureListener(e->{
        });
    }


}