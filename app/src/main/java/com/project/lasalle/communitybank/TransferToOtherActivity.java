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

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.google.protobuf.Timestamp;

import java.util.ArrayList;

import EnumClasses.TransactionType;
import Model.Account;
import Model.Payee;
import Model.Transaction;

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
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        name = sharedPreferences.getString("username",null);
        init(name);

    }

    public void init(String name){
        btnSendMoney = findViewById(R.id.btnSend);
        edAmount = findViewById(R.id.edAmount);
        spinnerAccount = findViewById(R.id.spinnerAccount);
        spinnerPayee = findViewById(R.id.spinnerPayee);
        spinnerAccount.setOnItemSelectedListener(this);
        spinnerPayee.setOnItemSelectedListener(this);

        getInfoForSpinner(name);

        btnSendMoney.setOnClickListener(view -> {
            amount = Double.valueOf(edAmount.getText().toString());
            sendMoney(amount,name,view);

        });
    }

    // Get all the information from database for the spinners
    public void getInfoForSpinner(String name){
        ArrayList<Payee> payees = new ArrayList<Payee>();
        ArrayList<Account> accounts = new ArrayList<Account>();

        CollectionReference payeeColRef = db.collection("Users/"+name+"/Payees");

        payeeColRef.get().addOnCompleteListener(task -> {
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


        CollectionReference accountColRef = db.collection("Users/"+name+"/Accounts");

        accountColRef.get().addOnCompleteListener(task -> {
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
    }

    // Update the balance for both the user after sending the money
    public void sendMoney(Double amount, String name,View view){

        DocumentReference accountDocRef = db.document("Users/"+name+"/Accounts/"+account);

        accountDocRef.get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               DocumentSnapshot documentSnapshot = task.getResult();
               Account account1 = documentSnapshot.toObject(Account.class);
               assert account1 != null;
               Double balance = account1.getBalance();

               if (amount > balance){
                   Log.e("Low","Amount exceed balance");
               }else{
                   balance = balance - amount;
                   accountDocRef.update("balance",balance).addOnCompleteListener(task1 -> {

                   }).addOnFailureListener(e->{
                      Log.e("Error",e.getMessage());
                   });
               }
           }
        }).addOnFailureListener(e -> {

        });

        DocumentReference payeeDocRef = db.document("Users/"+name+"/Payees/"+payeeEmail);

        payeeDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                Payee payee = documentSnapshot.toObject(Payee.class);
                assert payee != null;
                String email = payee.getPayeeEmail();

                DocumentReference docRef = db.document("Users/"+email+"/Accounts/"+account);

                docRef.get().addOnCompleteListener(task1 ->{
                    if (task1.isSuccessful()){
                        DocumentSnapshot documentSnapshot1 = task1.getResult();
                        Account account1 = documentSnapshot1.toObject(Account.class);
                        assert account1 != null;
                        Double balance = account1.getBalance();
                        Log.d("Amount",balance.toString());
                        balance = balance + amount ;


                        docRef.update("balance",balance).addOnCompleteListener(task3->{

                        }).addOnFailureListener(e->{
                           Log.e("Error",e.getMessage());
                        });

                    }
                }).addOnFailureListener(e -> {
                    Log.d("Error",e.getMessage());
                });
            }
        }).addOnFailureListener(e->{
           Log.e("Error",e.getMessage());
        });
    }

    // Function to create transactions for both users
    public void createTransactions(String name, String type, Double amount, TransactionType transactionType,View view){

        long millis = System.currentTimeMillis();

        Timestamp timestamp =
                Timestamp.newBuilder()
                        .setSeconds(millis / 1000)
                        .setNanos((int)(millis % 1000 * 1_000_000))
                        .build();

        Transaction transaction = new Transaction(amount,transactionType,timestamp);

        DocumentReference transactionDocRef = db.document("Users/"+name+"/Accounts/"+type+"/Transactions/");

        transactionDocRef.set(transaction).addOnCompleteListener(task->{
            Snackbar.make(view,"Money Send Successfully!!",Snackbar.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
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