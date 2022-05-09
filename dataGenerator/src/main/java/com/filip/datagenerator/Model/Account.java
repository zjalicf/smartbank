package com.filip.datagenerator.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class Account {

    @JsonProperty
    private UUID accountId;

    @JsonProperty
    private UUID clientId;

    @JsonProperty
    private double amount;

    @JsonProperty
    private List<Transaction> tList;

    @JsonProperty
    private boolean isActive;

    public Account() {}

    public Account(UUID accountId, UUID clientId, double amount, List<Transaction> tList, boolean isActive) {
        this.accountId = accountId;
        this.clientId = clientId;
        this.amount = amount;
        this.tList = tList;
        this.isActive = isActive;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
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

    public List<Transaction> gettList() {
        return tList;
    }

    public void settList(List<Transaction> tList) {
        this.tList = tList;
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
                "accountId=" + accountId +
                ", clientId=" + clientId +
                ", amount=" + amount +
                ", tList=" + tList +
                ", isActive=" + isActive +
                '}';
    }
}
