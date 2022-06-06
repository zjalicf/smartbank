package com.smartbank.client.Service.Impl;

import com.smartbank.client.Enum.TransactionType;
import com.smartbank.client.Model.Account;
import com.smartbank.client.Model.Transaction;
import com.smartbank.client.Repository.AccountRepository;
import com.smartbank.client.Repository.TransactionRepository;
import com.smartbank.client.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ClientServiceImpl implements ClientService {

    private static Logger LOGGER = Logger.getLogger(String.valueOf(ClientServiceImpl.class));

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void processTransaction(Transaction transaction) {

        double requesterAccountAmount;
        double receiverAccountAmount;
        double transactionAmount;

        Optional<Account> requesterAccount = accountRepository.findById(transaction.getRequesterId());

        if (requesterAccount.isPresent()) {
            requesterAccountAmount = requesterAccount.get().getAmount();
            transactionAmount = transaction.getAmount();
        } else {
            return; // handling
        }

        LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));

        if (transaction.getReceiverId() == null) {

            if (transaction.getTransactionType().equals(TransactionType.WITHDRAW)) {
                requesterAccount.get().setAmount(requesterAccountAmount - transactionAmount);
                accountRepository.save(requesterAccount.get());

                transactionRepository.save(transaction);
                LOGGER.log(Level.INFO, "transaction saved");

            } else if (transaction.getTransactionType().equals(TransactionType.DEPOSIT)) {

                requesterAccount.get().setAmount(requesterAccountAmount + transactionAmount);
                accountRepository.save(requesterAccount.get());

                transactionRepository.save(transaction);
                LOGGER.log(Level.INFO, "transaction saved");

            } else {
                Optional<Account> receiverAccount = accountRepository.findById(transaction.getReceiverId());

                if (receiverAccount.isPresent()) {
                    receiverAccountAmount = receiverAccount.get().getAmount();
                    transactionAmount = transaction.getAmount();
                } else {
                    return;
                }

                requesterAccount.get().setAmount(requesterAccountAmount - transactionAmount);
                accountRepository.save(requesterAccount.get());

                receiverAccount.get().setAmount(receiverAccountAmount + transactionAmount);
                accountRepository.save(receiverAccount.get());

                transactionRepository.save(transaction);
                LOGGER.log(Level.INFO, "transaction saved");
            }
        }
    }
}
