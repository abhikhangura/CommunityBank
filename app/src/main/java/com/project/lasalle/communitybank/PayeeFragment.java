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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Model.Payee;


public class PayeeFragment extends Fragment {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextInputEditText edPayeeName,edPayeeEmail;
    Button btnAddPayee;
    String payeeName,payeeEmail;
    TextInputLayout txtPayeeName,txtPayeeEmail;
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
        txtPayeeName = view.findViewById(R.id.txtPayeeName);
        txtPayeeEmail = view.findViewById(R.id.txtPayeeEmail);

        SharedPreferences sharedPreferences = this.requireActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("username",null);

        btnAddPayee.setOnClickListener(view1 -> {
            payeeName = edPayeeName.getText().toString();
            payeeEmail = edPayeeEmail.getText().toString();

            if (payeeName.length()<=0){
                txtPayeeName.setError("Please enter payee name");
                if (payeeEmail.length()<=0){
                    txtPayeeEmail.setError("Please enter payee Email");
                }
            }else{
                if (payeeEmail.length()<=0){
                    txtPayeeEmail.setError("Please enter payee Email");
                    txtPayeeName.setErrorEnabled(false);
                }else {
                    Payee payee = new Payee(payeeName,payeeEmail);
                    txtPayeeName.setErrorEnabled(false);
                    txtPayeeEmail.setErrorEnabled(false);

                    DocumentReference userDocRef = db.document("Users/"+payeeEmail);

                    DocumentReference payeeDocRef = db.document("Users/"+name+"/Payees/"+payeeName);

                    userDocRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){
                                payeeDocRef.set(payee).addOnCompleteListener(task1 -> {
                                    Snackbar.make(view,"Payee added Successfully",Snackbar.LENGTH_SHORT).show();
                                    edPayeeEmail.setText(null);
                                    edPayeeName.setText(null);
                                    edPayeeName.requestFocus();
                                }).addOnFailureListener(e -> {
                                    Log.e("Error in payee",e.getMessage());
                                });
                            }else {
                                txtPayeeEmail.setError("Please enter a valid user");
                            }
                        }
                        else {
                            txtPayeeEmail.setError("Please enter a valid user");
                        }
                    }).addOnFailureListener(e -> Log.e("Error",e.getMessage()));
                }
            }
        });
    }
}