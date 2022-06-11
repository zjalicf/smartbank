package com.smartbank.validation;

import com.smartbank.validation.Model.AmountUpdate;
import com.smartbank.validation.Model.Saldo;
import com.smartbank.validation.Model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSenders {

    @Value("${transaction.topic}")
    private String transactionTopic;

    @Value("${transaction_response.topic}")
    private String transactionResponseTopic;

    @Value("${saldo_update.topic}")
    private String saldoUpdateTopic;

    @Value("${amount_update.topic}")
    private String amountUpdateTopic;

    @Autowired
    private KafkaTemplate<String, Transaction> transactionKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Saldo> saldoKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, AmountUpdate> amountUpdateKafkaTemplate;

    public void sendTransactionResponse(Transaction transaction) {
        transactionKafkaTemplate.send(transactionResponseTopic, transaction);
    }

    public void sendTransaction(Transaction transaction) {
        transactionKafkaTemplate.send(transactionTopic, transaction);
    }

    public void sendSaldoUpdate(Saldo saldo){
        saldoKafkaTemplate.send(saldoUpdateTopic, saldo);
    }

    public void sendAmountUpdate(AmountUpdate amountUpdate) {
        amountUpdateKafkaTemplate.send(amountUpdateTopic, amountUpdate);
    }
}
