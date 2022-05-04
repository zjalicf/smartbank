package com.filip.smartbank.Controller;

import com.filip.smartbank.Model.TransactionRequest;
import com.filip.smartbank.Service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Controller
@RequestMapping("/client")
@EnableAsync
public class TransactionController {

    @Autowired
    DataGenerator dataGenerator;

    @PostMapping()
    @Async
    @Scheduled(fixedRate = 2000)
    public void sendOfflineTransaction() throws InterruptedException {

        boolean bankWorking = true; // za sada
        // trebace neki if closed = false

        if (bankWorking) {
            dataGenerator.generateOfflineTransaction();
//            Thread.sleep(2000);
        }
    }
}
