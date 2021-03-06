package com.project.lasalle.communitybank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import EnumClasses.TransactionType;
import Model.Account;
import Model.Random;
import Model.Transaction;

public class TransferToSelfActivity extends AppCompatActivity {


    AutoCompleteTextView withdrawAccount,depositAccount;
    ImageView imgBack;
    Button btnSendMoney;
    TextInputEditText edAmount;
    ArrayAdapter<Account> accountArrayAdapter, depositArrayAdapter;
    String stWithdrawAccount, stDepositAccount;
    TextInputLayout txtWithdraw, txtDeposit, txtAmount;
    double amount;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.white));
        setContentView(R.layout.activity_transfer_to_self);
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        String name = sharedPreferences.getString("username",null);
        init(name);
    }

    public void init(String name){

        withdrawAccount = findViewById(R.id.withdrawAccountAutoComplete);
        depositAccount = findViewById(R.id.depositAccountAutoComplete);
        btnSendMoney = findViewById(R.id.btnSendMoney);
        txtWithdraw = findViewById(R.id.withdrawAccountDropDown);
        txtDeposit = findViewById(R.id.depositAccountDropDown);
        txtAmount = findViewById(R.id.txtAmount);
        imgBack = findViewById(R.id.imgBackTra);
        edAmount = findViewById(R.id.edAmount1);

        loadSpinners(name);

        withdrawAccount.setOnItemClickListener((adapterView, view, i, l) -> stWithdrawAccount = adapterView.getItemAtPosition(i).toString());

        depositAccount.setOnItemClickListener((adapterView, view, i, l) -> stDepositAccount = adapterView.getItemAtPosition(i).toString());

        btnSendMoney.setOnClickListener(view -> {
            try {
                amount = Double.parseDouble(edAmount.getText().toString());
            }catch (Exception e){
                txtAmount.setErrorEnabled(true);
                txtAmount.setError("Please input a valid amount");
            }
            String id = Random.createTransactionId();
            try{
                if (stWithdrawAccount.equals(stDepositAccount)){
                    txtWithdraw.setErrorEnabled(true);
                    txtDeposit.setErrorEnabled(true);
                    txtWithdraw.setError("Choose different account");
                    txtDeposit.setError("Choose different account");
                }else{
                    if (amount == 0){
                        txtAmount.setErrorEnabled(true);
                        txtAmount.setError("Please input a valid amount");
                    }else{
                        txtDeposit.setErrorEnabled(false);
                        txtWithdraw.setErrorEnabled(false);
                        transferMoney(view,name,amount,id);
                    }
                }
            }catch (Exception e){

            }

        });

        imgBack.setOnClickListener(view -> finish());
    }

    public void loadSpinners(String name){
        ArrayList<Account> accounts = new ArrayList<>();
        CollectionReference accountDocRef = db.collection("Users/"+name+"/Accounts");

        accountDocRef.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot querySnapshot : task.getResult()) {
                Account account = querySnapshot.toObject(Account.class);
                accounts.add(account);

                accountArrayAdapter = new ArrayAdapter<>(this,R.layout.drop_down_item,accounts);
                depositArrayAdapter = new ArrayAdapter<>(this,R.layout.drop_down_item,accounts);
                withdrawAccount.setAdapter(accountArrayAdapter);
                depositAccount.setAdapter(depositArrayAdapter);
            }
        }).addOnFailureListener(e -> Log.e("Error",e.getMessage()));

    }

    public void transferMoney(View view, String name, double amount, String id){

        DocumentReference withdrawDocRef = db.document("Users/"+name+"/Accounts/"+stWithdrawAccount);

        withdrawDocRef.get().addOnCompleteListener(task ->{
            Account account = task.getResult().toObject(Account.class);
            double balance = account.getBalance();

            if (amount > balance){
                txtAmount.setErrorEnabled(true);
                txtAmount.setError("Amount exceed balance");
            }else{
                txtAmount.setErrorEnabled(false);
                balance = balance - amount;

                withdrawDocRef.update("balance",balance).addOnCompleteListener(task1->{
                    createTransactions(id,name,stWithdrawAccount,amount,TransactionType.Withdraw);
                    updateBalanceOnOtherAccount(view,name,amount,id);
                }).addOnFailureListener(e ->Log.e("Error",e.getMessage()));
            }

        }).addOnFailureListener(e -> Log.e("Error",e.getMessage()));
    }

    public void updateBalanceOnOtherAccount(View view,String name, double amount,String id){

        DocumentReference depositDocRef = db.document("Users/"+name+"/Accounts/"+stDepositAccount);

        depositDocRef.get().addOnCompleteListener(task -> {
            Account account = task.getResult().toObject(Account.class);
            double balance = account.getBalance();
            balance = balance + amount;

            depositDocRef.update("balance",balance).addOnCompleteListener(task1->{
                edAmount.setText(null);
                Snackbar.make(view,"Money send successfully!!",Snackbar.LENGTH_SHORT).show();
                createTransactions(id,name,stDepositAccount,amount,TransactionType.Deposit);
            }).addOnFailureListener(e ->Log.e("Error",e.getMessage()));

        }).addOnFailureListener(e ->Log.e("Error",e.getMessage()));
    }

    public void createTransactions(String transactionId, String name, String type, Double amount, TransactionType transactionType){

        Transaction transaction = new Transaction(amount,transactionType,  Calendar.getInstance().getTime());

        DocumentReference transactionDocRef = db.collection("Users/"+name+"/Accounts/"+type+"/Transactions").document(transactionId);

        transactionDocRef.set(transaction).addOnCompleteListener(task -> {

        }).addOnFailureListener(e -> Log.e("Error",e.getMessage()));
    }
}