package com.smartbank.datagenerator.Service.Impl;

import com.smartbank.datagenerator.Enum.Status;
import com.smartbank.datagenerator.KafkaSenders;
import com.smartbank.datagenerator.Model.Account;
import com.smartbank.datagenerator.Model.Transaction;
import com.smartbank.datagenerator.Service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class DataGeneratorImpl implements DataGenerator {

    @Value("${OFFLINE_TRANSACTION_UPPER_LIMIT}")
    Integer limit;

    @Autowired
    KafkaSenders sender;

    @Override
    public void insertAccounts() {
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
