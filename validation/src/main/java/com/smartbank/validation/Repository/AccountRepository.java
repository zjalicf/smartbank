package com.smartbank.validation.Repository;

import com.smartbank.validation.Model.Account;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface AccountRepository extends CassandraRepository<Account, UUID> {
}
