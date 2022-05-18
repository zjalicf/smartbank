package com.smartbank.validation;

import com.smartbank.validation.Model.Account;
import com.smartbank.validation.Model.Transaction;
import com.smartbank.validation.Repository.AccountRepository;
import com.smartbank.validation.Service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @Autowired
    ValidationService validationService;

    @Autowired
    AccountRepository accountService;

    @KafkaListener(
            topics = "transaction_request",
            groupId = "groupId",
            containerFactory= "transactionKafkaListenerContainerFactory"
    )
    void transactionRequestListener(Transaction transaction) {
//        validationService.validate(transaction);
    }

    @KafkaListener (
            topics = "account",
            groupId = "groupId",
            containerFactory= "accountKafkaListenerContainerFactory"
    )
    void accountListener(Account account) {
        accountService.save(account);
        System.out.println(accountService.findAll());
    }

    @KafkaListener(
            topics = "transaction",
            groupId = "groupId",
            containerFactory= "transactionKafkaListenerContainerFactory"
    )
    void transactionListener(Transaction transaction) {
        System.out.println("2. Received transaction done:" + transaction);
    }
}