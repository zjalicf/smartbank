package com.filip.smartbank.Service;

import com.filip.smartbank.Model.TransactionRequest;

public interface DataGenerator {

    void insertAccounts();
    void generateOnlineTransaction() throws InterruptedException;
    void generateOfflineTransaction() throws InterruptedException;
}
