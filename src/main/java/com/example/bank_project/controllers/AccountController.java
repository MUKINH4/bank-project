package com.example.bank_project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank_project.dto.PixDTO;
import com.example.bank_project.dto.TransactionRequest;
import com.example.bank_project.model.Account;
import com.example.bank_project.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        try {
            accountService.createAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).body("Conta criada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public List<Account> listAllAccounts() {
        return accountService.listAllAccounts();
    }

    @GetMapping("/id/{id}")
    public Optional<Account> listAccountById(@PathVariable Long id){
        return accountService.findAccountById(id);
    }

    @GetMapping("/cpf/{cpf}")
    public Account listAccountByCpf(@PathVariable String cpf) {
        return accountService.findAccountByCpf(cpf);
    }

    @PutMapping("/inactivate/{id}")
    public ResponseEntity<String> inactivateAccount(@PathVariable Long id) {
        int updatedRows = accountService.inactivateAccount(id);
        if (updatedRows > 0) {
            return ResponseEntity.ok("Conta desativada com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conta não encontrada");
    }

    @PutMapping("/deposit")
    public ResponseEntity<Optional<Account>> deposit(@RequestBody TransactionRequest depositRequest) {
        try {
            accountService.depositAmount(depositRequest.getId(), depositRequest.getAmount());
            return ResponseEntity.ok().body(accountService.findAccountById(depositRequest.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }

    @PutMapping("/withdraw")
    public ResponseEntity<Optional<Account>> withdraw(@RequestBody TransactionRequest depositRequest) {
        try {
            // Verifica se o saldo da conta é menor do que o valor solicitado para saque :)
            if (accountService.findAccountById(depositRequest.getId()).get().getBalance() < depositRequest.getAmount()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            accountService.withdrawAmount(depositRequest.getId(), depositRequest.getAmount());
            return ResponseEntity.ok().body(accountService.findAccountById(depositRequest.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/pix")
public ResponseEntity<Account> pix(@RequestBody PixDTO pixDto) {
    Optional<Account> originAccountOptional = accountService.findAccountById(pixDto.getSender());
    Optional<Account> destinationAccountOptional = accountService.findAccountById(pixDto.getReceiver());

    if (originAccountOptional.isEmpty() || destinationAccountOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Account originAccount = originAccountOptional.get();
    Account destinationAccount = destinationAccountOptional.get();

    if (originAccount.getBalance() < pixDto.getAmount()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    accountService.withdrawAmount(originAccount.getId(), pixDto.getAmount());
    accountService.depositAmount(destinationAccount.getId(), pixDto.getAmount());

    // Retorna a conta de origem atualizada
    Optional<Account> updatedOriginAccount = accountService.findAccountById(originAccount.getId());
    return updatedOriginAccount
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
}

    
}
