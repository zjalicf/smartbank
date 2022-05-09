package com.filip.datagenerator.Model;

import com.filip.datagenerator.Status;

import java.util.UUID;

public class Transaction {

    private UUID transactionId;
    private UUID requesterId;
    private UUID receiverId; //ako je null znam da je offline
    private double amount;
    private Status status;

    public Transaction(UUID transactionId, UUID requesterId, UUID receiverId, double amount, Status status) {
        this.transactionId = transactionId;
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = status;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(UUID requesterId) {
        this.requesterId = requesterId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "transactionId=" + transactionId +
                ", requesterId=" + requesterId +
                ", receiverId=" + receiverId +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}
