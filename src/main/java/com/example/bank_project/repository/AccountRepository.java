package com.example.bank_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.bank_project.model.Account;

import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByCpf(String cpf);

    @Modifying
    @Transactional
    @Query("UPDATE Account SET active = false WHERE id = ?1")
    int inactivateAccount(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance + ?1 WHERE id = ?2")
    int deposit(Long id, double amount);


    
}  
