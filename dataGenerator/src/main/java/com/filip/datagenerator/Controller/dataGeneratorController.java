package com.filip.datagenerator.Controller;

import com.filip.datagenerator.Service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;

@Component
public class dataGeneratorController {

    @Autowired
    DataGenerator dataGenerator;

    @Value("${ONLINE_TRANSACTION_UPPER_LIMIT}")
    Integer limit;

    @PostConstruct
    public void test() throws ExecutionException, InterruptedException {
        dataGenerator.insertAccounts();
    }

    @Scheduled(fixedRate = 1) //boze sacuvaj
    @PostMapping("/online-transaction")
    public void sendOnlineTransaction() throws InterruptedException {

        dataGenerator.generateOnlineTransaction();

        SecureRandom secureRandom = new SecureRandom();
        int randomMs = secureRandom.nextInt(limit); // 300

        Thread.sleep(randomMs);
    }
}
