package com.project.lasalle.communitybank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Model.Account;
import Model.RecyclerViewAccountAdapter;
import Model.RecyclerViewTransactions;
import Model.Transaction;

public class TransactionActivity extends AppCompatActivity {

    String accountNumber,accountType,accountBalance;
    TextView txtAccNum, txtAccType, txtAccBal;
    ImageView imgBack;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    RecyclerViewTransactions recyclerViewTransactions;
    ArrayList<Transaction> transactionArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_transaction);
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("username",null);
        init(name);
    }

    @SuppressLint("SetTextI18n")
    public void init(String name){
        accountNumber = getIntent().getStringExtra("AccountNumber");
        accountType = getIntent().getStringExtra("AccountType");
        accountBalance = getIntent().getStringExtra("AccountBalance");

        txtAccNum = findViewById(R.id.txtAccNum);
        txtAccType = findViewById(R.id.txtAccType);
        txtAccBal = findViewById(R.id.txtAccBal);
        imgBack = findViewById(R.id.imgBackTrans);

        txtAccType.setText(accountType);
        txtAccNum.setText(accountNumber);
        txtAccBal.setText("$"+ accountBalance);

        imgBack.setOnClickListener(view -> {
            finish();
        });

        transactionArrayList = new ArrayList<>();
        CollectionReference transColRef = db.collection("Users/"+name+"/Accounts/"+accountType+"/Transactions");

        transColRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Transaction transaction = document.toObject(Transaction.class);
                    transactionArrayList.add(transaction);
                    recyclerView = findViewById(R.id.recyclerTrans);
                    recyclerViewTransactions = new RecyclerViewTransactions(TransactionActivity.this,transactionArrayList);
                    recyclerView.setAdapter(recyclerViewTransactions);
                    recyclerView.setLayoutManager(new LinearLayoutManager(TransactionActivity.this));
                }
            }
        }).addOnFailureListener(e-> Log.e("Error",e.getMessage()));


    }
}