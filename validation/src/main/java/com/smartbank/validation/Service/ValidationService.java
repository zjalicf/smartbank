package com.smartbank.validation.Service;

import com.smartbank.validation.Model.Account;
import com.smartbank.validation.Model.Transaction;

public interface ValidationService {

    void validate(Transaction transaction);
}
