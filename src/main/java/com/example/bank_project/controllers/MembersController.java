package com.example.bank_project.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class MembersController {
    
    @GetMapping
    public String mostrarMembros(){
        return "Samuel Heitor - RM 556731 \nLucas Nicolini - RM 557613";
    }

}
