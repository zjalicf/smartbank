package com.smartbank.validation.Service.Impl;

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
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        double accountAmount;
        double transactionAmount;
        double currentSaldo;

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

            LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));

            if (transaction.getTransactionType().equals(TransactionType.WITHDRAW)) {

                if (accountAmount >= transaction.getAmount() && currentSaldo - transactionAmount >= 0) {
                    LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));
                    LOGGER.log(Level.INFO, "Was amount is: " + account.get().getAmount());
                    LOGGER.log(Level.INFO, "Was saldo is: " + saldo.get().getSaldo());

                    transaction.setStatus(Status.APPROVED);
                    kafkaSender.sendTransaction(transaction);

                } else {
                    LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));
                    LOGGER.log(Level.INFO, "Account ammount: " + accountAmount);
                    LOGGER.log(Level.INFO, "Transaction ammount: " + transactionAmount);
                    LOGGER.log(Level.INFO, "declined");
                    transaction.setStatus(Status.DECLINED);
                }

            } else if (transaction.getTransactionType().equals(TransactionType.DEPOSIT)) {


                LOGGER.log(Level.INFO, "Was amount is: " + account.get().getAmount());
                LOGGER.log(Level.INFO, "Was saldo is: " + saldo.get().getSaldo());

                transaction.setStatus(Status.APPROVED);
                kafkaSender.sendTransaction(transaction);
            }
        } else {
            System.out.println("izgleda receiver nije null - online");
        }
        kafkaSender.sendTransactionResponse(transaction);
    }
}
