package com.smartbank.client.Model;

import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash
public class AmountUpdate {

    private UUID accountId;
    private double amount;

    public AmountUpdate() {}

    public AmountUpdate(UUID accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AmountUpdate{" +
                "accountId=" + accountId +
                ", amount=" + amount +
                '}';
    }
}
