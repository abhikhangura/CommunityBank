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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

import Model.Account;
import Model.Database;
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
        init(view);
        return view;
    }
    

    public void init(View view){

        txtName = view.findViewById(R.id.txtName);
        imgLogo = view.findViewById(R.id.imgLogo);
        imgLogo.setImageResource(R.drawable.logo);
        txtMsg = view.findViewById(R.id.txtMsg);
        txtMyAccounts = view.findViewById(R.id.txtMyAccounts);
        addSavingAccount = view.findViewById(R.id.btnAddSavingAccount);


        SharedPreferences sharedPreferences = this.requireActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("username",null);

        DocumentReference documentReference = db.document("Users/"+name);

        documentReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()){
                    User user = documentSnapshot.toObject(User.class);
                    assert user != null;
                    String fullName = user.getFirstName() +" "+ user.getLastName();
                    txtName.setText(fullName);
                    if(!user.isStatus()){
                        txtMsg.setText("Your Account is under review.\n\tPlease come back later.");
                        addSavingAccount.setVisibility(View.GONE);
                        txtMyAccounts.setVisibility(View.GONE);

                    }
                    else{
                        getAllAccounts(view,name);
                        Database.checkAccounts(name,addSavingAccount);

                    }
                }else{
                    Snackbar.make(view,"Error loading data!!!",Snackbar.LENGTH_LONG).show();
                }
            }else{
                Snackbar.make(view,"Error loading data!!!",Snackbar.LENGTH_LONG).show();
            }
        });

        addSavingAccount.setOnClickListener(view1 -> {
            Database.addSavingAccount(name, view);
            getAllAccounts(view,name);
            Database.checkAccounts(name,addSavingAccount);
        });

    }

    public void getAllAccounts(View view, String name){
        ArrayList<Account> accountList = new ArrayList<Account>();
        db.collection("Users").document(name).collection("Accounts").get().addOnCompleteListener(task -> {
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