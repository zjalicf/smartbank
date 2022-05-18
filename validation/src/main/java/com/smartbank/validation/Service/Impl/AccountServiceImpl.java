//package com.smartbank.validation.Service.Impl;
//
//import com.smartbank.validation.Model.Account;
//import com.smartbank.validation.Repository.AccountRepository;
//import com.smartbank.validation.Service.AccountService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class AccountServiceImpl implements AccountService {
//
//    @Autowired
//    AccountRepository accountRepository;
//
//    @Override
//    public List<Account> getAll() {
//        return new ArrayList<>(accountRepository.findAll());
//    }
//
//    @Override
//    public Account save(Account account) {
//        return accountRepository.save(account);
//    }
//}
