package com.smartbank.datagenerator.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class Account {

    @JsonProperty
    private UUID id;

    @JsonProperty
    private UUID clientId;

    @JsonProperty
    private double amount;

    @JsonProperty
    private List<Transaction> transactionList;

    @JsonProperty
    private boolean isActive;

    public Account() {}

    public Account(UUID id, UUID clientId, double amount, List<Transaction> transactionList, boolean isActive) {
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

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
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
