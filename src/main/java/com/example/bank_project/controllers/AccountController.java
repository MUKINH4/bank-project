package com.example.bank_project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank_project.dto.DepositRequest;
import com.example.bank_project.model.Account;
import com.example.bank_project.repository.AccountRepository;
import com.example.bank_project.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        try {
            accountRepository.save(account);
            return ResponseEntity.status(201).body("Conta criada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping
    public List<Account> listAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/id/{id}")
    public Optional<Account> listAccountById(@PathVariable Long id){
        return accountRepository.findById(id);
    }

    @GetMapping("/cpf/{cpf}")
    public Account listAccountByCpf(@PathVariable String cpf) {
        return accountRepository.findByCpf(cpf);
    }

    @PutMapping("/inactivate/{id}")
    public ResponseEntity<String> inactivateAccount(@PathVariable Long id) {
        int updatedRows = accountRepository.inactivateAccount(id);
        if (updatedRows > 0) {
            return ResponseEntity.ok("Conta desativada com sucesso!");
        }
        return ResponseEntity.status(400).body("Conta não encontrada");
    }

    @PutMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequest depositRequest) {
        accountService.depositAmount(depositRequest.getId(), depositRequest.getAmount());
        
        return ResponseEntity.ok("Depósito realizado com sucesso!");
    }
}
