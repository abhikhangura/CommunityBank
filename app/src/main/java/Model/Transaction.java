package Model;

import com.google.firebase.firestore.FieldValue;

import java.util.Date;

import EnumClasses.TransactionType;

public class Transaction {
    private double amount;
    TransactionType transactionType;
   private Date date;

    public Transaction() {
    }

    public Transaction(double amount, TransactionType transactionType, Date date) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
