package com.filip.datagenerator;

import com.filip.datagenerator.Model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class KafkaSenders {

    @Autowired
    private KafkaTemplate<String, Account> kafkaTemplate;

    public void send(Account account) throws ExecutionException, InterruptedException {
        System.out.println("sending account= " + account);
        SendResult<String, Account> x = kafkaTemplate.send("account", account).get();
        System.out.println("1xew" + x);
    }
}
