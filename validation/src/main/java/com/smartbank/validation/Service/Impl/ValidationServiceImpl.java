package com.smartbank.validation.Service.Impl;

import com.smartbank.validation.Enum.Status;
import com.smartbank.validation.Enum.TransactionType;
import com.smartbank.validation.KafkaSenders;
import com.smartbank.validation.Model.Account;
import com.smartbank.validation.Model.AmountUpdate;
import com.smartbank.validation.Model.Saldo;
import com.smartbank.validation.Model.Transaction;
import com.smartbank.validation.Repository.AccountRepository;
import com.smartbank.validation.Repository.SaldoRepository;
import com.smartbank.validation.Service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
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

        UUID accountId;
        double accountAmount;
        double transactionAmount;
        double currentSaldo;

        Optional<Saldo> saldo = saldoRepository.findById("saldo");
        Optional<Account> account = accountRepository.findById(transaction.getRequesterId());

        if (transaction.getReceiverId() == null) {

            if (account.isPresent() && saldo.isPresent()) {
                accountId = account.get().getId();
                accountAmount = account.get().getAmount();
                transactionAmount = transaction.getAmount();
                currentSaldo = saldo.get().getSaldo();
            } else {
                return; // handling
            }

            LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));

            if (TransactionType.WITHDRAW.equals(transaction.getTransactionType())) {

                if (accountAmount >= transaction.getAmount() && currentSaldo - transactionAmount >= 0) {
                    LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));
                    LOGGER.log(Level.INFO, "Account ammount: " + accountAmount);
                    LOGGER.log(Level.INFO, "Transaction ammount: " + transactionAmount);

                    transaction.setStatus(Status.APPROVED);
                    kafkaSender.sendTransaction(transaction);

                    account.get().setAmount(accountAmount - transactionAmount);
                    accountRepository.save(account.get());

                    AmountUpdate amountUpdate = new AmountUpdate(accountId, accountAmount);
                    kafkaSender.sendAmountUpdate(amountUpdate);

                    saldo.get().setSaldo(currentSaldo - transactionAmount);
                    saldoRepository.save(saldo.get());

                    Saldo saldoUpdate = new Saldo("saldo", (currentSaldo - transactionAmount));
                    kafkaSender.sendSaldoUpdate(saldoUpdate);

                } else {
                    LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));
                    LOGGER.log(Level.INFO, "Account ammount: " + accountAmount);
                    LOGGER.log(Level.INFO, "Transaction ammount: " + transactionAmount);
                    LOGGER.log(Level.INFO, "declined");

                    transaction.setStatus(Status.DECLINED);
                }

            } else if (TransactionType.DEPOSIT.equals(transaction.getTransactionType())) {
                LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));
                LOGGER.log(Level.INFO, "Account ammount: " + accountAmount);
                LOGGER.log(Level.INFO, "Transaction ammount: " + transactionAmount);

                transaction.setStatus(Status.APPROVED);
                kafkaSender.sendTransaction(transaction);

                account.get().setAmount(accountAmount + transactionAmount);
                accountRepository.save(account.get());

                AmountUpdate amountUpdate = new AmountUpdate(accountId, accountAmount);
                kafkaSender.sendAmountUpdate(amountUpdate);

                saldo.get().setSaldo(currentSaldo + transactionAmount);
                saldoRepository.save(saldo.get());

                Saldo saldoUpdate = new Saldo("saldo", (currentSaldo + transactionAmount));
                kafkaSender.sendSaldoUpdate(saldoUpdate);
            }
        } else {
            System.out.println("izgleda receiver nije null - online");
        }
        kafkaSender.sendTransactionResponse(transaction);
    }

    //trebace update baza metoda
}
