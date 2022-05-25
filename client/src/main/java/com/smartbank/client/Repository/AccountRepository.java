package com.smartbank.client.Repository;

import com.smartbank.client.Model.Account;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface AccountRepository extends CassandraRepository<Account, UUID> {
}
