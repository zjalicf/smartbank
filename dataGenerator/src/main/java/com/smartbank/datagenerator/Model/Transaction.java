package com.smartbank.datagenerator.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartbank.datagenerator.Enum.Status;
import com.smartbank.datagenerator.Enum.TransactionType;

import java.util.UUID;

public class Transaction {

    @JsonProperty
    private UUID transactionId;

    @JsonProperty
    private UUID requesterId;

    @JsonProperty
    private UUID receiverId; // null values indicates offline transaction, however status
                            // is needed to distinguish withdraw/deposit

    @JsonProperty
    private double amount;

    @JsonProperty
    private Status status;

    public Transaction(UUID transactionId, UUID requesterId, UUID receiverId, double amount, Status status, TransactionType value) {
        this.transactionId = transactionId;
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.amount = amount;
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
