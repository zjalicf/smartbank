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
        double transactionAmount;
        double currentSaldo;

        Optional<Saldo> saldo = saldoRepository.findById("saldo");
        Optional<Account> requesterAccount = accountRepository.findById(transaction.getRequesterId());
        Optional<Account> receiverAccount = accountRepository.findById(transaction.getReceiverId());

        if (requesterAccount.isPresent() && receiverAccount.isPresent() && saldo.isPresent()) {
            requesterAccountId = requesterAccount.get().getId();
            receiverAccountId =  receiverAccount.get().getId();
            requesterAccountAmount = requesterAccount.get().getAmount();
            receiverAccountAmount = receiverAccount.get().getAmount();
            transactionAmount = transaction.getAmount();
            currentSaldo = saldo.get().getSaldo();
        } else {
            return;
        }

        if (transaction.getReceiverId() == null) {

            LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));
            if (TransactionType.WITHDRAW.equals(transaction.getTransactionType())) {

                if (requesterAccountAmount >= transaction.getAmount() && currentSaldo - transactionAmount >= 0) {
                    LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));
                    LOGGER.log(Level.INFO, "Account ammount: " + requesterAccountAmount);
                    LOGGER.log(Level.INFO, "Transaction ammount: " + transactionAmount);

                    transaction.setStatus(Status.APPROVED);
                    kafkaSender.sendTransaction(transaction);

                    requesterAccount.get().setAmount(requesterAccountAmount - transactionAmount);
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
                LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));
                LOGGER.log(Level.INFO, "Account ammount: " + requesterAccountAmount);
                LOGGER.log(Level.INFO, "Transaction ammount: " + transactionAmount);

                transaction.setStatus(Status.APPROVED);
                kafkaSender.sendTransaction(transaction);

                requesterAccount.get().setAmount(requesterAccountAmount + transactionAmount);
                accountRepository.save(requesterAccount.get());

                AmountUpdate amountUpdate = new AmountUpdate(requesterAccountId, requesterAccountAmount);
                kafkaSender.sendAmountUpdate(amountUpdate);

                saldo.get().setSaldo(currentSaldo + transactionAmount);
                saldoRepository.save(saldo.get());

                Saldo saldoUpdate = new Saldo("saldo", (currentSaldo + transactionAmount));
                kafkaSender.sendSaldoUpdate(saldoUpdate);
            }
        } else {

            LOGGER.log(Level.INFO, String.valueOf(transaction.getTransactionType()));
            if (requesterAccountAmount >= transaction.getAmount()) {

                transaction.setStatus(Status.APPROVED);
                kafkaSender.sendTransaction(transaction);

                requesterAccount.get().setAmount(requesterAccountAmount - transactionAmount);
                accountRepository.save(requesterAccount.get());

                receiverAccount.get().setAmount(receiverAccountAmount + transactionAmount);
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
