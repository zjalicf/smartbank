package com.smartbank.validation.Model;

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

    @Column("id")
    public UUID getId() {
        return id;
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
