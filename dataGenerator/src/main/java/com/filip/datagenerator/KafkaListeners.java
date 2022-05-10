package com.filip.datagenerator;

import com.filip.datagenerator.Model.Account;
import com.filip.datagenerator.Service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private static Thread offlineTxWork;
    private static boolean working = false;

    @Autowired
    DataGenerator dataGenerator;

    @KafkaListener(
            topics = "bank_working",
            groupId = "groupId"
    )
    void bankStatusListener(final String data) {

        System.out.println("Listener says: " + data);

        if (offlineTxWork == null && "working".equals(data)) {
            working = true;
            offlineTxWork = new Thread(() -> {
                while (working) {
                    try {
                        dataGenerator.generateOfflineTransaction();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            offlineTxWork.start();
        } else if (offlineTxWork != null && !"working".equals(data)) {
            working = false;
            try {
                offlineTxWork.join();
                System.out.println("Stopped: offlineTxWork");
                offlineTxWork = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @KafkaListener(
            topics = "account",
            groupId = "groupId"
    )
    void accountListener(final Account account) {
        System.out.println("received account= " + account);
    }
}