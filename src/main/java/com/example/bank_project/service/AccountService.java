package com.example.bank_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank_project.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;


    @Transactional
    public void depositAmount(Long id, double amount) {
        accountRepository.deposit(id, amount);
    }
}
