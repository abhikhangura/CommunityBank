package com.project.lasalle.communitybank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

import Model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imgLogo;
    Button btnLogin;
    TextInputEditText edEmail, edPassword;
    String email, password;
    TextView txtNotReg,txtTitle;
    TextInputLayout txtEmail,txtPass;
    ProgressBar progressBar;
    int counter =0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onResume() {
        super.onResume();
        txtEmail.setErrorEnabled(false);
        txtPass.setErrorEnabled(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){
        imgLogo = findViewById(R.id.imgLogo);
        imgLogo.setImageResource(R.drawable.bank_login_logo);
        btnLogin = findViewById(R.id.btnLogIn);
        btnLogin.setOnClickListener(this);
        txtNotReg = findViewById(R.id.txtNotReg);
        txtNotReg.setOnClickListener(this);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        txtEmail = findViewById(R.id.txtUsername);
        txtPass = findViewById(R.id.txtPassword);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        txtTitle = findViewById(R.id.loginTitle);


    }

    public void verifyUser(View view, String email, String password){

        DocumentReference userDocRef = db.document("Users/"+email);

        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()){
                   User user = documentSnapshot.toObject(User.class);
                    assert user != null;
                    if(user.getEmail().equals(email) && user.getPassword().equals(password)){


                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                    counter++;
                                    progressBar.setProgress(counter);

                                    if (counter == 40){
                                        timer.cancel();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username", user.getEmail());
                                        editor.apply();
                                    }
                            }
                        };

                        timer.schedule(timerTask,40,40);

                    }else {
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(view,"Password is invalid!!!",Snackbar.LENGTH_LONG).show();
                    }
                }
                else{
                    progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.VISIBLE);
                if (email.length()<=0){
                    txtEmail.setErrorEnabled(true);
                    txtEmail.setError("Please enter the email");
                    progressBar.setVisibility(View.GONE);
                    if (password.length()<=0){
                        txtPass.setErrorEnabled(true);
                        txtPass.setError("Please enter the password");
                        progressBar.setVisibility(View.GONE);
                    }
                }else{
                    if (password.length()<=0){
                        txtPass.setError("Please enter the password");
                        txtEmail.setErrorEnabled(false);
                        progressBar.setVisibility(View.GONE);
                    }else {
                        txtEmail.setErrorEnabled(false);
                        txtPass.setErrorEnabled(false);
                        verifyUser(view,email,password);
                        edPassword.setText(null);
                    }
                }
                break;
            case R.id.txtNotReg:
                Intent in = new Intent(LoginActivity.this,SignUpActivity.class);

                    Pair[] pair = new Pair[4];
                    pair[0] = new Pair(imgLogo,"imageTrans");
                    pair[1] = new Pair(txtEmail,"txtEmailTrans");
                    pair[2] = new Pair(txtPass,"txtPassTrans");
                    pair[3] = new Pair(btnLogin,"btnTrans");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, pair);
                startActivity(in, activityOptions.toBundle());
                break;
            default:
                break;
        }
    }
}