package com.smartbank.corebank;

import com.smartbank.corebank.Model.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class KafkaListeners {

    private static Logger LOGGER = Logger.getLogger(String.valueOf(KafkaListeners.class));

    @KafkaListener (
            topics = "transaction_response",
            groupId = "transaction_response",
            containerFactory= "transactionKafkaListenerContainerFactory"
    )
    void transactionResponseListener(Transaction transaction) {
        LOGGER.info("Your transaction with ID: " + transaction.getId() + " was " + transaction.getStatus());
    }
}
