package Model;

import com.google.protobuf.Timestamp;

import EnumClasses.TransactionType;

public class Transaction {
    private double amount;
    TransactionType transactionType;
    private Timestamp timestamp;

    public Transaction() {
    }

    public Transaction(double amount, TransactionType transactionType, Timestamp timestamp) {
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
