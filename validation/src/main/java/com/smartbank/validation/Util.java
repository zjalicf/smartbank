package com.smartbank.validation;

import com.smartbank.validation.Model.Saldo;
import com.smartbank.validation.Repository.SaldoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Util {

    @Autowired
    SaldoRepository saldoRepository;

    @PostConstruct
    public void setSaldo() {
        Saldo saldo = new Saldo("saldo", 50000);
        saldoRepository.save(saldo);
    }
}
