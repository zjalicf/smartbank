package com.smartbank.datagenerator.Service.Impl;

import com.smartbank.datagenerator.Enum.Status;
import com.smartbank.datagenerator.Enum.TransactionType;
import com.smartbank.datagenerator.KafkaSenders;
import com.smartbank.datagenerator.Model.Account;
import com.smartbank.datagenerator.Model.Transaction;
import com.smartbank.datagenerator.Service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class DataGeneratorImpl implements DataGenerator {

    @Value("${ONLINE_TRANSACTION_UPPER_LIMIT}")
    Integer onlineLimit;

    @Value("${OFFLINE_TRANSACTION_UPPER_LIMIT}")
    Integer offlineLimit;

    @Value("${MAX_ACCOUNTS}")
    Integer maxAccounts;

    @Autowired
    KafkaSenders kafkaSender;

    private static List<Account> ACCOUNT_LIST = new LinkedList<>();

    @Override
    public void insertAccounts() {
        int n = 0;
        while (n < maxAccounts) {
            List<Transaction> transationList = new ArrayList<>();
            Random random = new Random();
            double amount = (5000) * random.nextDouble();
            Account a = new Account(UUID.randomUUID(), UUID.randomUUID(), amount, transationList, true);
            ACCOUNT_LIST.add(a);
            kafkaSender.sendAccount(a);
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
                kafkaSender.sendTransaction(transaction);
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(onlineRunnable, 0, offlineLimit, TimeUnit.MILLISECONDS);
    }

    @Override
    @DependsOn("insertAccounts")
    public void generateOnlineTransaction() {

        System.out.println("online disabled");
//        Runnable onlineRunnable = () -> {
//            int sender = ThreadLocalRandom.current().nextInt(0, maxAccounts + 1);
//            int receiver =  ThreadLocalRandom.current().nextInt(0, maxAccounts + 1);
//
//            Account senderAcc = ACCOUNT_LIST.get(sender);
//            Account receiverAcc = ACCOUNT_LIST.get(receiver);
//            if (senderAcc.getAccountId() != receiverAcc.getAccountId()) {
//                double amount =  ThreadLocalRandom.current().nextDouble(0, 5000 + 1);
//                Transaction transaction = new Transaction(UUID.randomUUID(),
//                        senderAcc.getAccountId(), receiverAcc.getAccountId(), rounder(amount), Status.WAITING, null);
//                kafkaSender.sendTransaction(transaction);
//            }
//        };
//
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//        executor.scheduleAtFixedRate(onlineRunnable, 0, onlineLimit, TimeUnit.MILLISECONDS);
    }

    public double rounder(double amount) {
        amount = amount * 100;
        amount = Math.round(amount);
        amount = amount / 100;
        return amount;
    }
}
