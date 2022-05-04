package com.filip.smartbank.Service.Impl;

import com.filip.smartbank.Model.TransactionRequest;
import com.filip.smartbank.Service.DataGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DataGeneratorImpl implements DataGenerator {

    @Override
    public void insertAccounts() {

    }

//        SecureRandom secureRandom = new SecureRandom();
//        int randomMs = secureRandom.nextInt(300);

    @Override
    public void generateOfflineTransaction() throws InterruptedException {

        List<TransactionRequest> requests = new ArrayList<>();
        int n = 0;
        while (n != 3) {
            TransactionRequest transactionRequest = new TransactionRequest(UUID.randomUUID(),
                    UUID.randomUUID(), UUID.randomUUID(), 0.0);
            requests.add(transactionRequest);
            n++;
        }
        System.out.println(requests);

    }

    @Override
    public void generateOnlineTransaction() {

    }
}
