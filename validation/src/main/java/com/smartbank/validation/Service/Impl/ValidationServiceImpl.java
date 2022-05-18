package com.smartbank.validation.Service.Impl;

import com.smartbank.validation.KafkaSenders;
import com.smartbank.validation.Model.Transaction;
import com.smartbank.validation.Service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    KafkaSenders kafkaSender;

    @Override
    public void validate(Transaction transaction) {
        if (transaction.getReceiverId() == null) {

        } else {

        }
        kafkaSender.sendTransaction(transaction);
    }
}
