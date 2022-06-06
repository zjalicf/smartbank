package com.smartbank.client.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartbank.client.Enum.Status;
import com.smartbank.client.Enum.TransactionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;

import java.time.Instant;
import java.util.UUID;

public class Transaction {

    @JsonProperty
    @Column("id")
    private UUID id;

    @JsonProperty
    @Column("requester_id")
    private UUID requesterId; // weird bug it wont convert requesterId -> requester_id despite the @Column

    @JsonProperty
    @Column("receiver_id")
    private UUID receiverId; // null values indicates offline transaction, however status
    // is needed to distinguish withdraw/deposit
    @JsonProperty
    private double amount;

    @JsonProperty
    private Status status;

    @JsonProperty
    @Column("transaction_type")
    private TransactionType transactionType;

    @JsonProperty
    private Instant time;

    public Transaction() {}

    public Transaction(UUID id, UUID requesterId, UUID receiverId, double amount, Status status,
                       TransactionType transactionType, Instant time) {

        this.id = id;
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = status;
        this.transactionType = transactionType;
        this.time = time;
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

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
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
                ", time = " + time +
                '}';
    }
}
