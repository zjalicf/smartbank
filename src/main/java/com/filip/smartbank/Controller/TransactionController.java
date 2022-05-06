package com.filip.smartbank.Controller;

import com.filip.smartbank.Model.TransactionRequest;
import com.filip.smartbank.Service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Controller
@RequestMapping("/client")
@EnableAsync
public class TransactionController {

    @Value("${ONLINE_TRANSACTION_UPPER_LIMIT}")
    Integer limit;

    @Autowired
    DataGenerator dataGenerator;

//    @PostMapping("/offline-transaction")
//    public void sendOfflineTransaction() throws InterruptedException {
//
//        dataGenerator.generateOfflineTransaction();
//
//    }

    //@Scheduled(fixedRate = 1) //boze sacuvaj
    @PostMapping("/online-transaction")
    public void sendOnlineTransaction() throws InterruptedException {

        dataGenerator.generateOnlineTransaction();

        SecureRandom secureRandom = new SecureRandom();
        int randomMs = secureRandom.nextInt(limit); // 300

        Thread.sleep(randomMs);
        System.out.println(randomMs);
    }
}
