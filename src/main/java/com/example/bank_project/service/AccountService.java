package com.example.bank_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank_project.model.Account;
import com.example.bank_project.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;


    @Transactional
    public void depositAmount(Long id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Valor inválido para depósito");
        }
        accountRepository.deposit(amount, id);
    }

    @Transactional
    public void withdrawAmount(Long id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Valor inválido para saque");
        }
        accountRepository.withdraw(amount, id);
    }

    @Transactional
    public int inactivateAccount(Long id) {
        int updatedRows = accountRepository.inactivateAccount(id);
        if (updatedRows == 0) {
            throw new IllegalArgumentException("Conta não encontrada");
        }
                return updatedRows;
    }

    public Optional<Account> findAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Account findAccountByCpf(String cpf) {
        return accountRepository.findByCpf(cpf);
    }

    public List<Account> listAllAccounts() {
        return accountRepository.findAll();
    }

    public void createAccount(Account account) {
        accountRepository.save(account);
    }
}
