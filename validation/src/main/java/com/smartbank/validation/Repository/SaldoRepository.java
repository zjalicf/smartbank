package com.smartbank.validation.Repository;

import com.smartbank.validation.Model.Saldo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface SaldoRepository extends CrudRepository<Saldo, String> {
}
