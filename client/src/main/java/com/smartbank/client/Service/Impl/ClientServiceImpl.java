package com.smartbank.client.Service.Impl;

import com.smartbank.client.Enum.TransactionType;
import com.smartbank.client.Model.Account;
import com.smartbank.client.Model.AmountUpdate;
import com.smartbank.client.Model.Saldo;
import com.smartbank.client.Model.Transaction;
import com.smartbank.client.Repository.AccountRepository;
import com.smartbank.client.Repository.SaldoRepository;
import com.smartbank.client.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ClientServiceImpl implements ClientService {

    private static Logger LOGGER = Logger.getLogger(String.valueOf(ClientServiceImpl.class));

    @Autowired
    SaldoRepository saldoRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void processTransaction(Transaction transaction) {

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

            if (transaction.getTransactionType().equals(TransactionType.WITHDRAW)) {

                LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));

                LOGGER.log(Level.INFO, "Was amount is: " + account.get().getAmount());
                account.get().setAmount(accountAmount - transactionAmount);
                LOGGER.log(Level.INFO, "Now amount is: " + account.get().getAmount());
                accountRepository.save(account.get());

                LOGGER.log(Level.INFO, "Was saldo is: " + saldo.get().getSaldo());
                saldo.get().setSaldo(currentSaldo - transactionAmount);
                LOGGER.log(Level.INFO, "Now saldo is: " + account.get().getAmount());
                saldoRepository.save(saldo.get());

            } else if (transaction.getTransactionType().equals(TransactionType.DEPOSIT)) {

                LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));

                LOGGER.log(Level.INFO, "Was amount is: " + account.get().getAmount());
                account.get().setAmount(accountAmount + transactionAmount);
                LOGGER.log(Level.INFO, "Now amount is: " + account.get().getAmount());
                accountRepository.save(account.get());

                LOGGER.log(Level.INFO, "Was saldo is: " + saldo.get().getSaldo());
                saldo.get().setSaldo(currentSaldo + transactionAmount);
                LOGGER.log(Level.INFO, "Now saldo is: " + account.get().getAmount());
                saldoRepository.save(saldo.get());
            }
        } else {
            System.out.println("izgleda receiver nije null - online");
        }
    }

    @Override
    public void updateSaldo(Saldo saldo) {
        saldoRepository.save(saldo);
    }

    @Override
    public void updateAccountAmount(AmountUpdate amountUpdate) {
        Optional<Account> account = accountRepository.findById(amountUpdate.getAccountId());
        if (account.isPresent()) {
            account.get().setAmount(amountUpdate.getAmount());
            accountRepository.save(account.get());
        }
    }
}
