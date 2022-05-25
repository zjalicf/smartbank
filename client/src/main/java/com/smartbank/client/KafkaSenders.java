package com.smartbank.client;

import com.smartbank.client.Model.AmountUpdate;
import com.smartbank.client.Model.Saldo;
import com.smartbank.client.Model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaSenders {

    @Value("${saldo_update.topic}")
    private String saldoUpdateTopic;

    @Value("${amount_update.topic}")
    private String amountUpdateTopic;

    @Autowired
    private KafkaTemplate<String, Optional<Saldo>> saldoKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Optional<AmountUpdate>> amountUpdateTemplate;

    public void sendSaldoUpdate(Optional<Saldo> saldo) {
        saldoKafkaTemplate.send(saldoUpdateTopic, saldo);
    }

    public void amountUpdateTopic(Optional<AmountUpdate> amountUpdate) {
        amountUpdateTemplate.send(amountUpdateTopic, amountUpdate);
    }
}
