package Model;

import com.google.firebase.firestore.FieldValue;

import EnumClasses.TransactionType;

public class Transaction {
    private double amount;
    TransactionType transactionType;
    private FieldValue timestamp;

    public Transaction() {
    }

    public Transaction(double amount, TransactionType transactionType, FieldValue timestamp) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public FieldValue getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(FieldValue timestamp) {
        this.timestamp = timestamp;
    }
}
