package com.smartbank.datagenerator;

import com.smartbank.datagenerator.Model.Account;
import com.smartbank.datagenerator.Service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class KafkaListeners {

    private static Thread offlineTxWork;
    private static final Logger logger = Logger.getLogger(String.valueOf(KafkaListeners.class));
    private static boolean workingBool = false;
    private static final String working = "working";
    private static List<Account> accountList = new LinkedList<>();

    @Autowired
    DataGenerator dataGenerator;

    @DependsOn("accountListener")
    @KafkaListener(
            topics = "bank_working",
            groupId = "groupId",
            containerFactory= "bankKafkaListenerContainerFactory"
    )
    void bankStatusListener(final String data) {

        logger.info("Listener says: " + data);

        if (offlineTxWork == null && working.equals(data)) {
            workingBool = true;
            offlineTxWork = new Thread(() -> {
                while (workingBool) {
                    try {
                        dataGenerator.generateOfflineTransaction();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            offlineTxWork.start();
        } else if (offlineTxWork != null && !working.equals(data)) {
            workingBool = false;
            try {
                offlineTxWork.join();
                logger.info("Stopped: offlineTxWork");
                offlineTxWork = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @KafkaListener (
            topics = "account",
            groupId = "groupId",
            containerFactory= "accountKafkaListenerContainerFactory"
    )
    void accountListener(Account account) {
        accountList.add(account);
    }
}