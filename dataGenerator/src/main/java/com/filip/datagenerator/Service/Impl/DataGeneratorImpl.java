package com.filip.datagenerator.Service.Impl;

import com.filip.datagenerator.Enum.Status;
import com.filip.datagenerator.KafkaSenders;
import com.filip.datagenerator.Model.Account;
import com.filip.datagenerator.Model.Transaction;
import com.filip.datagenerator.Service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class DataGeneratorImpl implements DataGenerator {

    @Value("${OFFLINE_TRANSACTION_UPPER_LIMIT}")
    Integer limit;

    @Autowired
    KafkaSenders sender;

    @Override
    public void insertAccounts() throws ExecutionException {
        int n = 0;
        while (n < 21) { // TODO: 10001
            List<Transaction> tlist = new ArrayList<>();
            Random random = new Random(505000);
            double amount = 15000 + random.nextDouble();
            Account a = new Account(UUID.randomUUID(), UUID.randomUUID(), amount, tlist, true);
            sender.sendAccount(a);
            n++;
        }
    }

    @Override
    public void generateOfflineTransaction() throws InterruptedException {

        int n = 0;
        while (n != 3) {
            Transaction transaction = new Transaction(UUID.randomUUID(),
                    UUID.randomUUID(), null, 0.0, Status.WAITING);
            n++;
            sender.sendTransaction(transaction);
        }
        Thread.sleep(2000); // TODO: 500
    }

    @Override
    public void generateOnlineTransaction() {

        Transaction transaction = new Transaction(UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), 0.0, Status.WAITING);
        sender.sendTransaction(transaction);
    }
}
