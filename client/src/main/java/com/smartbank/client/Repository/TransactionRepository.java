package com.smartbank.client.Repository;

import com.smartbank.client.Model.Transaction;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends CassandraRepository<Transaction, UUID> {
}
