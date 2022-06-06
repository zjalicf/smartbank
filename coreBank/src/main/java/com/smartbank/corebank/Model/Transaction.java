package com.smartbank.corebank.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartbank.corebank.Enum.Status;
import com.smartbank.corebank.Enum.TransactionType;

import java.util.UUID;

public class Transaction {

    @JsonProperty
    private UUID id;

    @JsonProperty
    private UUID requesterId;

    @JsonProperty
    private UUID receiverId; // null values indicates offline transaction, however status
                            // is needed to distinguish withdraw/deposit

    @JsonProperty
    private double amount;

    @JsonProperty
    private Status status;

    @JsonProperty
    private TransactionType transactionType;

    public Transaction() {}

    public Transaction(UUID id, UUID requesterId, UUID receiverId, double amount, Status status,
                       TransactionType transactionType) {

        this.id = id;
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = status;
        this.transactionType = transactionType;
    }

    public UUID getId() {
        return id;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "id=" + id +
                ", requesterId=" + requesterId +
                ", receiverId=" + receiverId +
                ", amount=" + amount +
                ", status=" + status +
                ", transactionType=" + transactionType +
                '}';
    }
}
