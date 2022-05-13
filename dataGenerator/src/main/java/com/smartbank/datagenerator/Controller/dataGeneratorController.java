package com.smartbank.datagenerator.Controller;

import com.smartbank.datagenerator.Service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;

@Component
@EnableScheduling
public class dataGeneratorController {

    @Autowired
    DataGenerator dataGenerator;

    @PostConstruct
    public void insert() throws ExecutionException, InterruptedException {
        dataGenerator.insertAccounts();
    }

    @DependsOn("insert")
    @PostConstruct
    public void sendOnlineTransaction() throws InterruptedException {
        dataGenerator.generateOnlineTransaction();
    }
}
