package com.filip.smartbank.Model;

import java.util.UUID;

public class TransactionRequest {

    private UUID transactionId;
    private UUID requesterId;
    private UUID receiverId; //ako je null znam da je offline
    private double amount;

    public TransactionRequest(UUID transactionId, UUID requesterId, UUID receiverId, double amount) {
        this.transactionId = transactionId;
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.amount = amount;
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

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "transactionId=" + transactionId +
                ", requesterId=" + requesterId +
                ", receiverId=" + receiverId +
                ", amount=" + amount +
                '}';
    }
}
