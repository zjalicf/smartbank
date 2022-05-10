package com.filip.datagenerator.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.filip.datagenerator.Enum.Status;

import java.util.UUID;

public class Transaction {

    @JsonProperty
    private UUID transactionId;

    @JsonProperty
    private UUID requesterId;

    @JsonProperty
    private UUID receiverId; //ako je null znam da je offline

    @JsonProperty
    private double amount;

    @JsonProperty
    private Status status;

    public Transaction(UUID transactionId, UUID requesterId, UUID receiverId, double amount, Status status) {
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
