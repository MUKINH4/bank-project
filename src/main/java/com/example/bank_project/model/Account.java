package com.example.bank_project.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

enum AccountType {
    POUPANCA,
    CORRENTE,
    SALARIO
}
@Data
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private int agency;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String cpf;

    private Date date = new Date();
    private double balance;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    private boolean active = true;

    public void setBalance(double balance) {
        if (this.balance < 0) {
            this.balance = 0;
        }
        this.balance = balance;
    }

}
