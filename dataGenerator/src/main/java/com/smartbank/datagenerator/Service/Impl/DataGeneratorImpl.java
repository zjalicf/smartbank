package com.smartbank.datagenerator.Service.Impl;

import com.smartbank.datagenerator.Enum.Status;
import com.smartbank.datagenerator.Enum.TransactionType;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class DataGeneratorImpl implements DataGenerator {

    @Value("${ONLINE_TRANSACTION_UPPER_LIMIT}")
    Integer onlineLimit;

    @Value("${OFFLINE_TRANSACTION_UPPER_LIMIT}")
    Integer offlineLimit;

    @Autowired
    KafkaSenders sender;

    @Override
    public void insertAccounts() {
        int n = 0;
        while (n < 10001) {
            List<Transaction> tlist = new ArrayList<>();
            Random random = new Random(405000);
            double amount = 15000 + random.nextDouble();
            Account a = new Account(UUID.randomUUID(), UUID.randomUUID(), amount, tlist, true);
            sender.sendAccount(a);
            n++;
        }
    }

    @Override
    public void generateOfflineTransaction() {

        Runnable onlineRunnable = () -> {
            int n = 0;
            while (n != 3) {
                Random random = new Random(1);
                Transaction transaction = new Transaction(UUID.randomUUID(), UUID.randomUUID(), null,
                        0.0, Status.WAITING, TransactionType.values()[random.nextInt()]); /* randomly sets transaction to be deposit or withdraw*/
                n++;
                sender.sendTransaction(transaction);
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(onlineRunnable, 0, offlineLimit, TimeUnit.MILLISECONDS);
    }

    @Override
    public void generateOnlineTransaction() {

        Runnable onlineRunnable = () -> {
            Transaction transaction = new Transaction(UUID.randomUUID(),
                    UUID.randomUUID(), UUID.randomUUID(), 0.0, Status.WAITING, null);
            sender.sendTransaction(transaction);
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(onlineRunnable, 0, onlineLimit, TimeUnit.MILLISECONDS);
    }
}
