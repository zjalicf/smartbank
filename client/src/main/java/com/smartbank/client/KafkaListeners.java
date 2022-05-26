package com.smartbank.client;

import com.smartbank.client.Model.Account;
import com.smartbank.client.Model.AmountUpdate;
import com.smartbank.client.Model.Saldo;
import com.smartbank.client.Model.Transaction;
import com.smartbank.client.Repository.AccountRepository;
import com.smartbank.client.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    KafkaListenerEndpointRegistry registry;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientService clientService;

    @KafkaListener (
            topics = "account",
            groupId = "account-client",
            containerFactory= "accountKafkaListenerContainerFactory"
    )
    void accountListener(Account account) {
        accountRepository.save(account);
        registry.getListenerContainer("transactionListener").start();
    }

    @KafkaListener (
            topics = "amount_update",
            groupId = "account_update-client",
            containerFactory= "amountUpdateKafkaListenerContainerFactory"
    )
    void amountUpdateListener(AmountUpdate amountUpdate) {
        clientService.updateAccountAmount(amountUpdate);
    }

    @KafkaListener (
            topics = "saldo_update",
            groupId = "saldo_update-client",
            containerFactory= "saldoUpdateKafkaListenerContainerFactory"
    )
    void saldoUpdateListener(Saldo saldo) {
        clientService.updateSaldo(saldo);
    }
}