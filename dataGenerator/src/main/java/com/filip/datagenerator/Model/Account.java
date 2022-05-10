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
