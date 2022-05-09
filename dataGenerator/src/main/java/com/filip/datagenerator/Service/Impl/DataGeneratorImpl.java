package com.filip.datagenerator.Service.Impl;

import com.filip.datagenerator.KafkaSenders;
import com.filip.datagenerator.Model.Account;
import com.filip.datagenerator.Model.Transaction;
import com.filip.datagenerator.Service.DataGenerator;
import com.filip.datagenerator.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class DataGeneratorImpl implements DataGenerator {

    @Value("${OFFLINE_TRANSACTION_UPPER_LIMIT}")
    Integer offlineLimit;

    @Value("${ONLINE_TRANSACTION_UPPER_LIMIT}")
    Integer limit;

    @Autowired
    KafkaSenders sender;

    @Override
    public void insertAccounts() throws ExecutionException, InterruptedException {
        List<Transaction> tlist = new ArrayList<>();
        sender.send(new Account(UUID.randomUUID(), UUID.randomUUID(), 500, tlist, true));
    }

    @Override
    public void generateOfflineTransaction() throws InterruptedException {

        List<Transaction> requests = new ArrayList<>();
        int n = 0;
        while (n != 3) {
            Transaction transaction = new Transaction(UUID.randomUUID(),
                    UUID.randomUUID(), null, 0.0, Status.WAITING);
            requests.add(transaction);
            n++;
        }
        Thread.sleep(2000); // treba 500 al je prebrzo
        System.out.println(requests);
    }

    @Override
    public void generateOnlineTransaction() throws InterruptedException {

        Transaction transaction = new Transaction(UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), 0.0, Status.WAITING);
        System.out.println(transaction);
    }
}
