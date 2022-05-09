package com.filip.datagenerator.Service;

import java.util.concurrent.ExecutionException;

public interface DataGenerator {

    void insertAccounts() throws ExecutionException, InterruptedException;
    void generateOnlineTransaction() throws InterruptedException;
    void generateOfflineTransaction() throws InterruptedException;
}
