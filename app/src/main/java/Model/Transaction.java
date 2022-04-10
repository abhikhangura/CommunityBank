package Model;

import com.google.type.DateTime;


import EnumClasses.TransactionType;

public class Transaction {
    private double amount;
    TransactionType transactionType;
    private DateTime dateTime;

    public Transaction() {
    }

    public Transaction(double amount, TransactionType transactionType, DateTime dateTime) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.dateTime = dateTime;
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

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
