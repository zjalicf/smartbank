package com.smartbank.datagenerator;

import com.smartbank.datagenerator.Model.Account;
import com.smartbank.datagenerator.Service.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class KafkaListeners {

    private static Thread OFFLINE_TX_WORK;
    private static Logger LOGGER = Logger.getLogger(String.valueOf(KafkaListeners.class));
    private static boolean WORKING_CHECK = false;
    private static String WORKING = "working";
    private static List<Account> ACCOUNT_LIST = new LinkedList<>();

    @Autowired
    DataGenerator dataGenerator;

    @DependsOn("accountListener")
    @KafkaListener(
            topics = "bank_working",
            groupId = "groupId",
            containerFactory= "bankKafkaListenerContainerFactory"
    )
    void bankStatusListener(final String data) {

        LOGGER.info("Listener says: " + data);

        if (OFFLINE_TX_WORK == null && WORKING.equals(data)) {
            WORKING_CHECK = true;
            OFFLINE_TX_WORK = new Thread(() -> {
                while (WORKING_CHECK) {
                    try {
                        dataGenerator.generateOfflineTransaction();
                        LOGGER.info("Transaction sent");
                    } catch (InterruptedException e) {
                        LOGGER.log(Level.SEVERE, "Could not generate offline transaction", e);
                    }
                }
            });
            OFFLINE_TX_WORK.start();
        } else if (OFFLINE_TX_WORK != null && !WORKING.equals(data)) {
            WORKING_CHECK = false;
            try {
                OFFLINE_TX_WORK.join();
                LOGGER.info("Stopped: offlineTxWork");
                OFFLINE_TX_WORK = null;
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Could not join OFFLINE_TX_WORK thread", e);
            }
        }
    }

    @KafkaListener (
            topics = "account",
            groupId = "groupId",
            containerFactory= "accountKafkaListenerContainerFactory"
    )
    void accountListener(Account account) {
        ACCOUNT_LIST.add(account);
    }
}