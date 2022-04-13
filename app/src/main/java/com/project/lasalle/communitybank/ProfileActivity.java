package com.project.lasalle.communitybank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Model.User;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView txtName,txtEmail,txtPhone,txtLastName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_profile);
        init();
    }

    @SuppressLint("SetTextI18n")
    public void init(){
        imgBack = findViewById(R.id.imageBackProfile);
        txtName = findViewById(R.id.txtProfileName);
        txtLastName = findViewById(R.id.txtProfileLastName);
        txtEmail = findViewById(R.id.txtProfileEmail);
        txtPhone = findViewById(R.id.txtProfilePhone);

        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        String user = sharedPreferences.getString("username",null);

        DocumentReference userDocRef = db.document("Users/"+user);

        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()){
                    User user1 = snapshot.toObject(User.class);
                    Log.d("User",user1.getEmail());
                    assert user1 != null;
                    String name = user1.getFirstName();
                    String lastname = user1.getLastName();
                    String email = user1.getEmail();
                    String phone = user1.getPhoneNumber().toString();
                    txtName.setText(name);
                    txtLastName.setText(lastname);
                    txtEmail.setText(email);
                    txtPhone.setText(phone);
                }else {
                    Log.e("Error","Error loading data");
                }
            }
        }).addOnFailureListener(e -> Log.e("Error",e.getMessage()));
        imgBack.setOnClickListener(view -> {
            finish();
        });
    }
}