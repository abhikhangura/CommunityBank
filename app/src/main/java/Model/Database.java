package Model;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import EnumClasses.AccountType;

public class Database {

    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void addSavingAccount(String email,View view){
        Account account = new Account(Random.createAccountNumber(),0.00, AccountType.Savings);

        db.collection("Users").document(email).collection("Accounts").document(AccountType.Savings.toString()).set(account).addOnSuccessListener(unused ->
                Snackbar.make(view,"Account Created Successfully",Snackbar.LENGTH_LONG).show()
        ).addOnFailureListener(e->{
            Snackbar.make(view,"Error occur during account creation!!\n" +
                    "Try again later!!",Snackbar.LENGTH_LONG).show();
        });
    }

    public static void checkAccounts(String email, Button addSavingAccount) {

        ArrayList<Account> accounts = new ArrayList<Account>();
        db.collection("Users").document(email).collection("Accounts").get().addOnCompleteListener(task -> {
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



}
