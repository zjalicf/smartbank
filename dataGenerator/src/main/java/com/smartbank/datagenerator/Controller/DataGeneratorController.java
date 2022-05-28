package com.smartbank.datagenerator.Controller;

import com.smartbank.datagenerator.Service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

@Component
@EnableScheduling
public class DataGeneratorController {

    @Autowired
    DataGenerator dataGenerator;

    @PostConstruct
    public void insertAccounts() throws ExecutionException, InterruptedException {
        dataGenerator.insertAccounts();
    }

    @DependsOn("insertAccounts")
    @PostConstruct
    public void sendOnlineTransaction() throws InterruptedException {
        dataGenerator.generateOnlineTransaction();
    }
}
