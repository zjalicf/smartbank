package com.filip.datagenerator;

import com.filip.datagenerator.Service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

@Component
public class testSend {

    @Autowired
    DataGenerator dataGenerator;

    @PostConstruct
    public void test() throws ExecutionException, InterruptedException {
        dataGenerator.insertAccounts();
    }
}
