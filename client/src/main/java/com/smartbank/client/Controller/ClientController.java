package com.smartbank.client.Controller;

import com.smartbank.client.Model.Transaction;
import com.smartbank.client.Repository.AccountRepository;
import com.smartbank.client.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/state")
    public String getState() {

        int randomAcc = ThreadLocalRandom.current().nextInt(0, 10000);
        return String.valueOf(accountRepository.findAll().get(randomAcc).getAmount());
    }

    @GetMapping("/last10")
    public List<Transaction> getLast10() {
        int randomAcc = ThreadLocalRandom.current().nextInt(0, 2);
        List<Transaction> transactionList = new ArrayList<>();
        for (Transaction t: transactionRepository.findAll()) {
            System.out.print(t.getRequesterId() + "==");
            System.out.println(accountRepository.findAll().get(randomAcc).getId());
            if (t.getRequesterId().equals(accountRepository.findAll().get(randomAcc).getId())) {
                transactionList.add(t);
            }
        }
        transactionList.sort((t1, t2) -> {
            if (t1.getTime() == null || t2.getTime() == null)
                return 0;
            return t1.getTime().compareTo(t2.getTime());
        });
        Collections.reverse(transactionList);
        System.out.println(transactionList.subList(0, 10));
        return transactionList.subList(0, 10);
    }
}
