package com.project.lasalle.communitybank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imgLogo;
    Button btnLogin;
    EditText edEmail, edPassword;
    String email, password;
    TextView txtNotReg;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){
        imgLogo = findViewById(R.id.imgLogo);
        imgLogo.setImageResource(R.drawable.logo);
        btnLogin = findViewById(R.id.btnLogIn);
        btnLogin.setOnClickListener(this);
        txtNotReg = findViewById(R.id.txtNotReg);
        txtNotReg.setOnClickListener(this);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);

        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        String name = sharedPreferences.getString("username",null);

        if (name!=null){
            edEmail.setText(name);
        }

    }

    public void verifyUser(View view,String email, String password){

        DocumentReference userDocRef = db.collection("Users").document(email);

        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()){
                   User user = documentSnapshot.toObject(User.class);
                    assert user != null;
                    if(user.getEmail().equals(email) && user.getPassword().equals(password)){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", user.getEmail());
                        editor.apply();
                    }else {
                        Snackbar.make(view,"Password is invalid!!!",Snackbar.LENGTH_LONG).show();
                    }
                }
                else{
                   Snackbar.make(view,"Invalid Username!!",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btnLogIn:
                email = edEmail.getText().toString();
                password = edPassword.getText().toString();
                verifyUser(view,email,password);
                edPassword.setText(null);
                break;
            case R.id.txtNotReg:
                Intent in = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(in);
                break;
            default:
                break;
        }
    }
}