package com.smartbank.client.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Table
public class Account {

    @PrimaryKey
    @JsonProperty
    @Column("id")
    private UUID id;

    @JsonProperty
    @Column("client_id")
    private UUID clientId;

    @JsonProperty
    private double amount;

    @JsonProperty
    @Column("transaction_list")
    private Set<Transaction> transactionList;

    @JsonProperty
    @Column("active")
    private boolean isActive;

    public Account() {}

    public Account(UUID id, UUID clientId, double amount, Set<Transaction> transactionList, boolean isActive) {
        this.id = id;
        this.clientId = clientId;
        this.amount = amount;
        this.transactionList = transactionList;
        this.isActive = isActive;
    }

    public UUID getId() {
        return id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Set<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(Set<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", amount=" + amount +
                ", transactionList=" + transactionList +
                ", isActive=" + isActive +
                '}';
    }
}
