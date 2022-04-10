package com.project.lasalle.communitybank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

import EnumClasses.AccountType;
import Model.Account;
import Model.Random;
import Model.RecyclerViewAccountAdapter;
import Model.User;;

public class HomeFragment extends Fragment {

    String name;
    TextView txtName,txtMsg,txtMyAccounts;
    ImageView imgLogo;
    RecyclerView recyclerView;
    RecyclerViewAccountAdapter recyclerViewAccountAdapter;
    Button addSavingAccount;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences sharedPreferences = this.requireActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("username",null);
        init(view,name);
        return view;
    }
    

    public void init(View view,String name){

        txtName = view.findViewById(R.id.txtName);
        imgLogo = view.findViewById(R.id.imgLogo);
        imgLogo.setImageResource(R.drawable.logo);
        txtMsg = view.findViewById(R.id.txtMsg);
        txtMyAccounts = view.findViewById(R.id.txtMyAccounts);
        addSavingAccount = view.findViewById(R.id.btnAddSavingAccount);

        DocumentReference documentReference = db.document("Users/"+name);

        documentReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()){
                    User user = documentSnapshot.toObject(User.class);
                    assert user != null;
                    String fullName = "Welcome " + user.getFirstName();
                    txtName.setText(fullName);
                    if(!user.isStatus()){
                        txtMsg.setText("Your Account is under review.\n\tPlease come back later.");
                        addSavingAccount.setVisibility(View.GONE);
                        txtMyAccounts.setVisibility(View.GONE);

                    }
                    else{
                        getAllAccounts(view,name);
                        checkAccounts(name,addSavingAccount);

                    }
                }else{
                    Snackbar.make(view,"Error loading data!!!",Snackbar.LENGTH_LONG).show();
                }
            }else{
                Snackbar.make(view,"Error loading data!!!",Snackbar.LENGTH_LONG).show();
            }
        });

        addSavingAccount.setOnClickListener(view1 -> {
            addSavingAccount(name, view);
            getAllAccounts(view,name);
            checkAccounts(name,addSavingAccount);
        });

    }

    public void addSavingAccount(String email,View view){
        Account account = new Account(Random.createAccountNumber(),0.00, AccountType.Savings);

        DocumentReference docRef = db.document("Users/"+email+"/Accounts/"+account.getAccountType());

        docRef.set(account).addOnSuccessListener(unused ->
                Snackbar.make(view,"Account Created Successfully",Snackbar.LENGTH_LONG).show()
        ).addOnFailureListener(e->{
            Snackbar.make(view,"Error occur during account creation!!\n" +
                    "Try again later!!",Snackbar.LENGTH_LONG).show();
        });
    }

    public void checkAccounts(String email, Button addSavingAccount) {

        ArrayList<Account> accounts = new ArrayList<Account>();

        CollectionReference collectionReference = db.collection("Users/"+email+"/Accounts");

        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Account account = documentSnapshot.toObject(Account.class);
                    accounts.add(account);
                    for (Account acc : accounts) {
                        if (acc.getAccountType().toString().equals("Savings")) {
                            addSavingAccount.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }).addOnFailureListener(e -> {

        });
    }

    public void getAllAccounts(View view, String name){
        ArrayList<Account> accountList = new ArrayList<Account>();

        CollectionReference collectionReference = db.collection("Users/"+name+"/Accounts");

        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Account account = document.toObject(Account.class);
                    accountList.add(account);
                    recyclerView = view.findViewById(R.id.recyclerViewAccount);
                    recyclerView.setHasFixedSize(true);
                    recyclerViewAccountAdapter = new RecyclerViewAccountAdapter(getActivity(),accountList);
                    recyclerView.setAdapter(recyclerViewAccountAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }
        }).addOnFailureListener(e->{
            Log.e("Error",e.getMessage());
        });
    }
}