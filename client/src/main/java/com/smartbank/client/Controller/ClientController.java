package com.smartbank.client.Controller;

import com.smartbank.client.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/state")
    public String getState() {

        int randomAcc = ThreadLocalRandom.current().nextInt(0, 10);
        return String.valueOf(accountRepository.findAll().get(randomAcc).getAmount());
    }
}
