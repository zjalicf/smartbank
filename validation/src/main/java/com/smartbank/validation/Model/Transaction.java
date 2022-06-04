package com.smartbank.validation.Model;

import com.datastax.driver.mapping.EnumType;
import com.datastax.driver.mapping.annotations.Enumerated;
import com.datastax.driver.mapping.annotations.UDT;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartbank.validation.Enum.Status;
import com.smartbank.validation.Enum.TransactionType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Element;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@UDT(keyspace = "smartbank", name = "transaction")
public class Transaction {

    @JsonProperty
    @Column("transaction_id")
    private UUID transactionId;

    @JsonProperty
    @Column("requester_id")
    private UUID requesterId;

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

    public Transaction(UUID transactionId, UUID requesterId, UUID receiverId, double amount, Status status,
                       TransactionType transactionType, Instant time) {

        this.transactionId = transactionId;
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = status;
        this.transactionType = transactionType;
        this.time = time;
    }

    public UUID getTransactionId() {
        return transactionId;
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
                "transactionId=" + transactionId +
                ", requesterId=" + requesterId +
                ", receiverId=" + receiverId +
                ", amount=" + amount +
                ", status=" + status +
                ", transactionType=" + transactionType +
                ", time = " + time +
                '}';
    }
}
