package com.filip.datagenerator.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

public interface DataGenerator {

    @PostConstruct
    void insertAccounts() throws ExecutionException, InterruptedException;

    void generateOnlineTransaction() throws InterruptedException;
    void generateOfflineTransaction() throws InterruptedException;
}
