package com.smartbank.validation.Service.Impl;

import com.smartbank.validation.Enum.Status;
import com.smartbank.validation.Enum.TransactionType;
import com.smartbank.validation.KafkaSenders;
import com.smartbank.validation.Model.Account;
import com.smartbank.validation.Model.Transaction;
import com.smartbank.validation.Repository.AccountRepository;
import com.smartbank.validation.Service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    KafkaSenders kafkaSender;

    @Autowired
    AccountRepository accountRepository;

    /**
     * Main validation method for both online and offline transactions.
     *
     * Algorithm:
     * 1. We determine if the incoming transaction is offline or online by
     *    checking field requesterId for null
     *
     * 2. We check transaction type (WITHDRAW, DEPOSIT)
     *
     *    2a. If transaction is of type WITHDRAW:
     *        -> check current saldo in redis and amount of account in cassandra:
     *
     *        2aa. In case of saldo resulting < 0 after the transaction OR
     *             request amount < ammount on account after the transaction:
     *              -> set the status field to DECLINED,
     *              -> send transaction to TransactionResponseTopic
     *
     *        2ab. In case of saldo resulting > 0 after the transaction AND
     *             request amount < ammount on account after the transaction:
     *              -> set the status field to DECLINED,
     *              -> send transaction to TransactionResponseTopic
     *
     *        2ac. In case of saldo resulting > 0 after the transaction AND
     *             request amount > ammount on account after the transaction:
     *              -> set the status field to APPROVED,
     *              -> update saldo in redis,
     *              -> send SaldoUpdate event to proper KafkaTopic,
     *              -> send transaction to TransactionResponseTopic,
     *              -> send transaction to TransactionTopic
     *
     *    2b. If transaction is of type DEPOSIT:
     *        -> set the status field to APPROVED since no checks are needed,
     *        -> update saldo in redis,
     *        -> send SaldoUpdate event to proper KafkaTopic,
     *        -> send transaction to TransactionResponseTopic,
     *        -> send transaction to TransactionTopic
     *
     * @param  transaction  transaction recieved from KafkaListener (topic TransactionRequest)
     *
     */
    @Override
    public void validate(Transaction transaction) {

        double saldo = 9999999.0; // temp umesto redisa
        if (transaction.getReceiverId() == null) {
            if (transaction.getTransactionType().equals(TransactionType.WITHDRAW)) {

                Optional<Account> account = accountRepository.findById(transaction.getRequesterId());

                if (account.isPresent() && (account.get().getAmount() <= transaction.getAmount() || saldo - transaction.getAmount() <= 0)) {
                    System.out.println("Account ammount: " + account.get().getAmount());
                    System.out.println("Transaction ammount: " + transaction.getAmount());
                    System.out.println("error");
                    transaction.setStatus(Status.DECLINED);

                } else if (account.isPresent() && (account.get().getAmount() <= transaction.getAmount() && saldo - transaction.getAmount() >= 0)) {
                    System.out.println("Account ammount: " + account.get().getAmount());
                    System.out.println("Transaction ammount: " + transaction.getAmount());
                    System.out.println("error");
                    transaction.setStatus(Status.DECLINED);

                } else if (account.isPresent() && (account.get().getAmount() >= transaction.getAmount() && saldo - transaction.getAmount() >= 0)) {
                    System.out.println("Account ammount: " + account.get().getAmount());
                    System.out.println("Transaction ammount: " + transaction.getAmount());
                    System.out.println("moze");
                    transaction.setStatus(Status.APPROVED);
                    //menja se amount na acc
                }
            } else if (transaction.getTransactionType().equals(TransactionType.DEPOSIT)) {
                System.out.println("deposit - moze");
                transaction.setStatus(Status.APPROVED);
                //menja se amount na acc
            }
        } else {
            System.out.println("izgleda receiver null - online");
        }
        kafkaSender.sendTransactionResponse(transaction);
        kafkaSender.sendTransaction(transaction);
    }
}
