package com.example.bank_project.dto;

import lombok.Data;

@Data
public class PixDTO {
    private Long receiver;
    private Long sender;
    private double amount;
}
