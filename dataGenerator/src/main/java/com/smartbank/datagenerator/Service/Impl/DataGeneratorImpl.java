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

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
        while (n < 2) { //TODO: maxaccounts

            Random random = new Random();
            double amount = (5000) * random.nextDouble();
            Account a = new Account(UUID.randomUUID(), UUID.randomUUID(), amount, null, true);
            ACCOUNT_LIST.add(a);
            kafkaSender.sendAccount(a);
            n++;
        }
    }

    @Override
    @DependsOn("insertAccounts")
    public void generateOfflineTransaction() throws InterruptedException {

        int n = 0;
        while (n != 3) {

            List<TransactionType> typeList = new ArrayList<>();
            typeList.add(TransactionType.DEPOSIT);
            typeList.add(TransactionType.WITHDRAW);
            int randomType = ThreadLocalRandom.current().nextInt(0, 2);

            int sender = ThreadLocalRandom.current().nextInt(0, 2); //TODO:maxaccounts
            Account senderAcc = ACCOUNT_LIST.get(sender);
            double amount =  ThreadLocalRandom.current().nextDouble(0, 5000 + 1);

            Transaction transaction = new Transaction(UUID.randomUUID(), senderAcc.getId(), null,
                    rounder(amount), Status.WAITING, typeList.get(randomType), Instant.now()); /* randomly sets transaction to be deposit or withdraw*/
            n++;
            kafkaSender.sendTransaction(transaction);
        }
        Thread.sleep(10000);
    }

    @Override
    @DependsOn("insertAccounts")
    public void generateOnlineTransaction() throws InterruptedException {

//        while (true) {
//            int sender = ThreadLocalRandom.current().nextInt(0, maxAccounts);
//            int receiver =  ThreadLocalRandom.current().nextInt(0, maxAccounts);
//
//            List<TransactionType> typeList = new ArrayList<>();
//            typeList.add(TransactionType.DEPOSIT);
//            typeList.add(TransactionType.WITHDRAW);
//
//            int randomType = ThreadLocalRandom.current().nextInt(0, 2);
//            Account senderAcc = ACCOUNT_LIST.get(sender);
//            Account receiverAcc = ACCOUNT_LIST.get(receiver);
//
//            if (senderAcc.getId() != receiverAcc.getId()) {
//                double amount =  ThreadLocalRandom.current().nextDouble(0, 5000 + 1);
//                Transaction transaction = new Transaction(UUID.randomUUID(),
//                        senderAcc.getId(), receiverAcc.getId(), rounder(amount),
//                        Status.WAITING, typeList.get(randomType), Instant.now());
//                kafkaSender.sendTransaction(transaction);
//            }
//            Thread.sleep(onlineLimit); //busy waiting
        //popravi da moze 1-300
//        }
    }

    public double rounder(double amount) {
        amount = amount * 100;
        amount = Math.round(amount);
        amount = amount / 100;
        return amount;
    }
}
