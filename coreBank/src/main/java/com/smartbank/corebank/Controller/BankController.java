package com.smartbank.corebank.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
public class BankController {

    @Value("${bank_working.topic}")
    private String bankWorking;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public BankController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/start")
    public void openBank() {
        kafkaTemplate.send(bankWorking, "working");
    }

    @PostMapping("/close")
    public void closeBank() {
        kafkaTemplate.send(bankWorking, "stopped");
    }
}
