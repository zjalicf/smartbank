package com.smartbank.validation.Service.Impl;

import com.smartbank.validation.Config.CassandraConfig;
import com.smartbank.validation.Enum.Status;
import com.smartbank.validation.Enum.TransactionType;
import com.smartbank.validation.KafkaSenders;
import com.smartbank.validation.Model.Account;
import com.smartbank.validation.Model.Saldo;
import com.smartbank.validation.Model.Transaction;
import com.smartbank.validation.Repository.AccountRepository;
import com.smartbank.validation.Repository.SaldoRepository;
import com.smartbank.validation.Service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

@Service
public class ValidationServiceImpl implements ValidationService {

    private static Logger LOGGER = Logger.getLogger(String.valueOf(ValidationServiceImpl.class));

    @Autowired
    SaldoRepository saldoRepository;

    @Autowired
    KafkaSenders kafkaSender;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void validate(Transaction transaction) {

        double accountAmount = 0;
        double transactionAmount = 0;
        double currentSaldo = 0;

        Optional<Saldo> saldo = saldoRepository.findById("saldo");
        Optional<Account> account = accountRepository.findById(transaction.getRequesterId());

        if (transaction.getReceiverId() == null) {

            if (account.isPresent() && saldo.isPresent()) {
                accountAmount = account.get().getAmount();
                transactionAmount = transaction.getAmount();
                currentSaldo = saldo.get().getSaldo();
            } else {
                return; // handling
            }

            if (transaction.getTransactionType().equals(TransactionType.WITHDRAW)) {

                if (accountAmount >= transaction.getAmount() && currentSaldo - transactionAmount >= 0) {
                    LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));

                    LOGGER.log(Level.INFO, "Was amount is: " + account.get().getAmount());
                    account.get().setAmount(accountAmount - transactionAmount);
                    accountRepository.save(account.get());

                    LOGGER.log(Level.INFO, "Was saldo is: " + saldo.get().getSaldo());
                    saldo.get().setSaldo(currentSaldo - transactionAmount);
                    saldoRepository.save(saldo.get());

                    transaction.setStatus(Status.APPROVED);
                    kafkaSender.sendTransaction(transaction);

                    //temp check
                    Optional<Account> acc = accountRepository.findById(account.get().getId());
                    //
                    LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));
                    LOGGER.log(Level.INFO, "Now amount is: " + acc.get().getAmount());
                    System.out.println("----------------------------------------------------------");

                    //temp
                    Optional<Saldo> saldox = saldoRepository.findById("saldo");
                    //
                    LOGGER.log(Level.INFO, "Now saldo is: " + saldox.get().getSaldo());

                } else {
                    LOGGER.log(Level.INFO, "Account ammount: " + accountAmount);
                    LOGGER.log(Level.INFO, "Transaction ammount: " + transactionAmount);
                    LOGGER.log(Level.INFO, "declined");
                    transaction.setStatus(Status.DECLINED);
                }

            } else if (transaction.getTransactionType().equals(TransactionType.DEPOSIT)) {

                LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));

                LOGGER.log(Level.INFO, "Was amount is: " + account.get().getAmount());
                account.get().setAmount(accountAmount + transactionAmount);
                accountRepository.save(account.get());

                LOGGER.log(Level.INFO, "Was saldo is: " + saldo.get().getSaldo());
                saldo.get().setSaldo(currentSaldo + transactionAmount);
                saldoRepository.save(saldo.get());

                transaction.setStatus(Status.APPROVED);
                kafkaSender.sendTransaction(transaction);

                //temp check
                Optional<Account> acc = accountRepository.findById(account.get().getId());
                //
                LOGGER.log(Level.INFO, "Now amount is: " + acc.get().getAmount());
            }
        } else {
            System.out.println("izgleda receiver nije null - online");
        }
        kafkaSender.sendTransactionResponse(transaction);
    }
}
