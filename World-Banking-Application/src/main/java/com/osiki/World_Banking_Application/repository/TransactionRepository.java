package com.osiki.World_Banking_Application.repository;

import com.osiki.World_Banking_Application.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
