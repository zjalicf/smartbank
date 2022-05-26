package com.smartbank.client.Service;

import com.smartbank.client.Model.AmountUpdate;
import com.smartbank.client.Model.Saldo;
import com.smartbank.client.Model.Transaction;

public interface ClientService {

    void processTransaction(Transaction transaction);
    void updateSaldo(Saldo saldo);
    void updateAccountAmount(AmountUpdate amountUpdate);
}
