package com.project.lasalle.communitybank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import EnumClasses.AccountType;
import Model.Account;
import Model.Payee;

public class TransferToOtherActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner spinnerAccount,spinnerPayee;
    Button btnSendMoney;
    EditText edAmount;
    ArrayAdapter<Payee> adapter;
    ArrayAdapter<Account> accountAdapter;

    String payeeEmail,account,name;
    Double amount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_to_other);
        init();

    }

    public void init(){
        btnSendMoney = findViewById(R.id.btnSend);
        edAmount = findViewById(R.id.edAmount);
        spinnerAccount = findViewById(R.id.spinnerAccount);
        spinnerPayee = findViewById(R.id.spinnerPayee);
        spinnerAccount.setOnItemSelectedListener(this);
        spinnerPayee.setOnItemSelectedListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        name = sharedPreferences.getString("username",null);

        ArrayList<Payee> payees = new ArrayList<Payee>();
        ArrayList<Account> accounts = new ArrayList<Account>();
        db.collection("Users").document(name).collection("Payees").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot querySnapshot : task.getResult()){
                    Payee payee = querySnapshot.toObject(Payee.class);
                    payees.add(payee);
                }
                adapter = new ArrayAdapter<Payee>(this, android.R.layout.simple_spinner_dropdown_item,payees);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerPayee.setAdapter(adapter);
            }
        }).addOnFailureListener(e -> {

        });


        db.collection("Users").document(name).collection("Accounts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Account account = documentSnapshot.toObject(Account.class);
                    accounts.add(account);
                }
                accountAdapter = new ArrayAdapter<Account>(this,android.R.layout.simple_spinner_dropdown_item,accounts);
                accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAccount.setAdapter(accountAdapter);
            }
        }).addOnFailureListener(e -> {

        });

        btnSendMoney.setOnClickListener(view -> {
            amount = Double.valueOf(edAmount.getText().toString());
            sendMoney(amount);

        });
    }

    public void sendMoney(Double amount){
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        name = sharedPreferences.getString("username",null);
        db.collection("Users").document(name).collection("Accounts").document(account).get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               DocumentSnapshot documentSnapshot = task.getResult();
               Account account1 = documentSnapshot.toObject(Account.class);
               Double balance = account1.getBalance();

               if (amount > balance){
                   Log.e("Low","Amount exceed balance");
               }else{
                   balance = balance - amount;
                   db.collection("Users").document(name).collection("Accounts").document(account).update("balance",balance).addOnCompleteListener(task1 -> {

                   }).addOnFailureListener(e->{
                      Log.e("Error",e.getMessage());
                   });
               }
           }
        }).addOnFailureListener(e -> {

        });

        db.collection("Users").document(name).collection("Payees").document(payeeEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                Payee payee = documentSnapshot.toObject(Payee.class);
                String email = payee.getPayeeEmail();
                Log.d("Email",email);
                db.collection("Users").document(email).collection("Accounts").document(account).get().addOnCompleteListener(task1 ->{
                    if (task1.isSuccessful()){
                        DocumentSnapshot documentSnapshot1 = task1.getResult();
                        Account account1 = documentSnapshot1.toObject(Account.class);
                        assert account1 != null;
                        Double balance = account1.getBalance();
                        Log.d("Amount",balance.toString());
                        balance = balance + amount ;
                        db.collection("Users").document(email).collection("Accounts").document(account).update("balance",balance).addOnCompleteListener(task3->{

                        }).addOnFailureListener(e->{
                           Log.e("Error",e.getMessage());
                        });

                    }
                }).addOnFailureListener(e->{
                    Log.d("Error",e.getMessage());
                });
            }
        }).addOnFailureListener(e->{
           Log.e("Error",e.getMessage());
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       switch (adapterView.getId()){
           case R.id.spinnerPayee:
               payeeEmail = adapterView.getItemAtPosition(i).toString();
               break;
           case R.id.spinnerAccount:
               account = adapterView.getItemAtPosition(i).toString();
               break;
       }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}