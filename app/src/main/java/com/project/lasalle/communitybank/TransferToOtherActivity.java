package com.project.lasalle.communitybank;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import EnumClasses.TransactionType;
import Model.Account;
import Model.Payee;
import Model.Random;
import Model.Transaction;

public class TransferToOtherActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnSendMoney;
    TextInputEditText edAmount;
    ArrayAdapter<Payee> adapter;
    ArrayAdapter<Account> accountAdapter;
    String payeeEmail,account,name;
    double amount;
    ImageView imgBack;
    TextInputLayout txtAmount;
    AutoCompleteTextView accountSelect, payeeSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_transfer_to_other);
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        name = sharedPreferences.getString("username",null);
        init(name);

    }

    public void init(String name){
        btnSendMoney = findViewById(R.id.btnSend);
        edAmount = findViewById(R.id.edAmount);
        imgBack = findViewById(R.id.imgBack);
        txtAmount = findViewById(R.id.txtAmount);
        accountSelect = findViewById(R.id.accountAutoCompleteTextView);
        payeeSelect = findViewById(R.id.payeeAutoCompleteTextView);

        getInfoForSpinner(name);

        btnSendMoney.setOnClickListener(view -> {
            try {
                amount = Double.parseDouble(edAmount.getText().toString());
                String id = Random.createTransactionId();
                txtAmount.setErrorEnabled(false);
                sendMoney(id,amount,name,view);
            }catch (Exception e){
                Log.e("Error",e.getMessage());
                txtAmount.setErrorEnabled(true);
                txtAmount.setError("Enter valid amount");
            }
        });

        accountSelect.setOnItemClickListener((adapterView, view, i, l) -> account = adapterView.getItemAtPosition(i).toString());

        payeeSelect.setOnItemClickListener((adapterView, view, i, l) -> payeeEmail = adapterView.getItemAtPosition(i).toString());

        imgBack.setOnClickListener(view-> finish());
    }

    // Get all the information from database for the spinners
    public void getInfoForSpinner(String name){

        ArrayList<Payee> payees = new ArrayList<>();
        ArrayList<Account> accounts = new ArrayList<>();

        CollectionReference payeeColRef = db.collection("Users/"+name+"/Payees");

        payeeColRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot querySnapshot : task.getResult()){
                    Payee payee = querySnapshot.toObject(Payee.class);
                    payees.add(payee);
                }
                adapter = new ArrayAdapter<>(this, R.layout.drop_down_item,payees);
                payeeSelect.setAdapter(adapter);

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
                accountAdapter = new ArrayAdapter<>(this,R.layout.drop_down_item,accounts);
                accountSelect.setAdapter(accountAdapter);
            }
        }).addOnFailureListener(e -> {

        });
    }

    // Update the balance for both the user after sending the money
    public void sendMoney(String id,Double amount, String name,View view){

        DocumentReference accountDocRef = db.document("Users/"+name+"/Accounts/"+account);

        accountDocRef.get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               DocumentSnapshot documentSnapshot = task.getResult();
               Account account1 = documentSnapshot.toObject(Account.class);
               assert account1 != null;
               double balance = account1.getBalance();

               if (amount > balance){
                    txtAmount.setError("Amount exceed the balance");
               }else{
                   balance = balance - amount;
                   accountDocRef.update("balance",balance).addOnCompleteListener(task1 -> {
                       createTransactions(id,name,account,amount,TransactionType.Withdraw);
                        addMoneyToOtherUser(view,id);
                       Snackbar.make(view,"Money Send Successfully!!",Snackbar.LENGTH_SHORT).show();
                        edAmount.setText(null);
                        txtAmount.setErrorEnabled(false);
                   }).addOnFailureListener(e-> Log.e("Error",e.getMessage()));
               }
           }
        }).addOnFailureListener(e -> {

        });
    }

    public void addMoneyToOtherUser(View view,String id){

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
                        double balance = account1.getBalance();
                        Log.d("Amount", Double.toString(balance));
                        balance = balance + amount ;


                        docRef.update("balance",balance).addOnCompleteListener(task3-> createTransactions(id,email,account,amount,TransactionType.Deposit)).addOnFailureListener(e-> Log.e("Error",e.getMessage()));

                    }
                }).addOnFailureListener(e -> Log.d("Error",e.getMessage()));
            }
        }).addOnFailureListener(e-> Log.e("Error",e.getMessage()));
    }

    // Function to create transactions for both users
    public void createTransactions(String transactionId,String name, String type, Double amount, TransactionType transactionType){

        Transaction transaction = new Transaction(amount,transactionType, Calendar.getInstance().getTime());

        DocumentReference transactionDocRef = db.collection("Users/"+name+"/Accounts/"+type+"/Transactions").document(transactionId);

        transactionDocRef.set(transaction).addOnCompleteListener(task -> {

        }).addOnFailureListener(e -> Log.e("Error",e.getMessage()));
    }
}