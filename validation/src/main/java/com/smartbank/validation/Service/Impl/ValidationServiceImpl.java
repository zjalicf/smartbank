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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ValidationServiceImpl implements ValidationService {

    private static Logger LOGGER = Logger.getLogger(String.valueOf(ValidationServiceImpl.class));

    @Autowired
    private SaldoRepository saldoRepository;

    @Autowired
    private KafkaSenders kafkaSender;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void validate(Transaction transaction) {

        UUID requesterAccountId;
        UUID receiverAccountId;
        double requesterAccountAmount;
        double receiverAccountAmount;
        Set<Transaction> requesterTransactionSet;
        Set<Transaction> receiverTransactionSet;
        double transactionAmount;
        double currentSaldo;

        Optional<Saldo> saldo = saldoRepository.findById("saldo");
        Optional<Account> requesterAccount = accountRepository.findById(transaction.getRequesterId());

        if (requesterAccount.isPresent() && saldo.isPresent()) {
            requesterAccountId = requesterAccount.get().getId();
            requesterAccountAmount = requesterAccount.get().getAmount();
            requesterTransactionSet = requesterAccount.get().getTransactionList();
            transactionAmount = transaction.getAmount();
            currentSaldo = saldo.get().getSaldo();
        } else {
            return;
        }

        LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));

        if (transaction.getReceiverId() == null) {

            if (TransactionType.WITHDRAW.equals(transaction.getTransactionType())) {

                if (requesterAccountAmount >= transaction.getAmount() && currentSaldo - transactionAmount >= 0) {
                    LOGGER.log(Level.INFO, "Account ammount: " + requesterAccountAmount);
                    LOGGER.log(Level.INFO, "Transaction ammount: " + transactionAmount);
                    LOGGER.log(Level.INFO, "accepted");

                    transaction.setStatus(Status.APPROVED);
                    kafkaSender.sendTransaction(transaction);

                    requesterAccount.get().setAmount(requesterAccountAmount - transactionAmount);
                    if (requesterTransactionSet == null) {
                        requesterTransactionSet = new HashSet<>();
                    }
                    requesterTransactionSet.add(transaction);
                    requesterAccount.get().setTransactionList(requesterTransactionSet); // da vidimo
                    accountRepository.save(requesterAccount.get());

                    AmountUpdate amountUpdate = new AmountUpdate(requesterAccountId, requesterAccountAmount);
                    kafkaSender.sendAmountUpdate(amountUpdate);

                    saldo.get().setSaldo(currentSaldo - transactionAmount);
                    saldoRepository.save(saldo.get());

                    Saldo saldoUpdate = new Saldo("saldo", (currentSaldo - transactionAmount));
                    kafkaSender.sendSaldoUpdate(saldoUpdate);

                } else {
                    LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));
                    LOGGER.log(Level.INFO, "Account ammount: " + requesterAccountAmount);
                    LOGGER.log(Level.INFO, "Transaction ammount: " + transactionAmount);
                    LOGGER.log(Level.INFO, "declined");

                    transaction.setStatus(Status.DECLINED);
                }

            } else if (TransactionType.DEPOSIT.equals(transaction.getTransactionType())) {

                LOGGER.log(Level.INFO, "Account ammount: " + requesterAccountAmount);
                LOGGER.log(Level.INFO, "Transaction ammount: " + transactionAmount);
                LOGGER.log(Level.INFO, "accepted");

                transaction.setStatus(Status.APPROVED);
                kafkaSender.sendTransaction(transaction);

                requesterAccount.get().setAmount(requesterAccountAmount + transactionAmount);
                if (requesterTransactionSet == null) {
                    requesterTransactionSet = new HashSet<>();
                }
                requesterTransactionSet.add(transaction);
                requesterAccount.get().setTransactionList(requesterTransactionSet); // da vidimo
                accountRepository.save(requesterAccount.get());

                AmountUpdate amountUpdate = new AmountUpdate(requesterAccountId, requesterAccountAmount);
                kafkaSender.sendAmountUpdate(amountUpdate);

                saldo.get().setSaldo(currentSaldo + transactionAmount);
                saldoRepository.save(saldo.get());

                Saldo saldoUpdate = new Saldo("saldo", (currentSaldo + transactionAmount));
                kafkaSender.sendSaldoUpdate(saldoUpdate);
            }
        } else {
            Optional<Account> receiverAccount = accountRepository.findById(transaction.getReceiverId());

            if (receiverAccount.isPresent()) {
                receiverAccountId =  receiverAccount.get().getId();
                receiverAccountAmount = receiverAccount.get().getAmount();
                receiverTransactionSet = receiverAccount.get().getTransactionList();
                transactionAmount = transaction.getAmount();
            } else {
                return;
            }

            if (requesterAccountAmount >= transaction.getAmount()) {

                LOGGER.log(Level.INFO, "Account ammount: " + requesterAccountAmount);
                LOGGER.log(Level.INFO, "Transaction ammount: " + transactionAmount);
                LOGGER.log(Level.INFO, "accepted");

                transaction.setStatus(Status.APPROVED);
                kafkaSender.sendTransaction(transaction);

                requesterAccount.get().setAmount(requesterAccountAmount - transactionAmount);
                if (requesterTransactionSet == null) {
                    requesterTransactionSet = new HashSet<>();
                }
                requesterTransactionSet.add(transaction);
                requesterAccount.get().setTransactionList(requesterTransactionSet); // da vidimo
                accountRepository.save(requesterAccount.get());

                receiverAccount.get().setAmount(receiverAccountAmount + transactionAmount);
                if (receiverTransactionSet == null) {
                    receiverTransactionSet = new HashSet<>();
                }
                receiverTransactionSet.add(transaction);
                receiverAccount.get().setTransactionList(receiverTransactionSet); // da vidimo
                accountRepository.save(receiverAccount.get());

                AmountUpdate amountUpdateRequester = new AmountUpdate(requesterAccountId, requesterAccountAmount);
                kafkaSender.sendAmountUpdate(amountUpdateRequester);

                AmountUpdate amountUpdateReceiver = new AmountUpdate(receiverAccountId, receiverAccountAmount);
                kafkaSender.sendAmountUpdate(amountUpdateReceiver);
            } else {
                transaction.setStatus(Status.DECLINED);
            }
        }
        kafkaSender.sendTransactionResponse(transaction);
    }
}