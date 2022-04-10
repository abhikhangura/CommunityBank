package com.project.lasalle.communitybank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import Model.Payee;


public class PayeeFragment extends Fragment {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText edPayeeName,edPayeeEmail;
    Button btnAddPayee;
    String payeeName,payeeEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payee, container, false);
        init(view);
        return view;
    }

    public void init(View view){
        edPayeeEmail = view.findViewById(R.id.edPayeeEmail);
        edPayeeName = view.findViewById(R.id.edPayeeName);
        btnAddPayee = view.findViewById(R.id.btnAddPayee);

        SharedPreferences sharedPreferences = this.requireActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("username",null);

        btnAddPayee.setOnClickListener(view1 -> {
            payeeName = edPayeeName.getText().toString();
            payeeEmail = edPayeeEmail.getText().toString();

            Payee payee = new Payee(payeeName,payeeEmail);

            db.collection("Users").document(name).collection("Payees").document(payeeName).set(payee).addOnCompleteListener(task -> {
                Snackbar.make(view,"Payee added Successfully",Snackbar.LENGTH_SHORT).show();
                edPayeeEmail.setText(null);
                edPayeeName.setText(null);
                edPayeeName.requestFocus();
            }).addOnFailureListener(e -> {
                Log.e("Error in payee",e.getMessage());
            });

        });
    }
}