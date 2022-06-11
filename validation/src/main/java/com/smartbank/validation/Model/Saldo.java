package com.smartbank.validation.Model;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("Saldo")
public class Saldo {

    private String id;
    private double saldo;

    public Saldo(String id, double saldo) {
        this.id = id;
        this.saldo = saldo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Saldo{" +
                "key=" + id +
                ", saldo=" + saldo +
                '}';
    }
}
