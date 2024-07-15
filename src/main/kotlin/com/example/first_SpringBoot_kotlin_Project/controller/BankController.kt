package com.example.first_SpringBoot_kotlin_Project.controller

import com.example.first_SpringBoot_kotlin_Project.model.Bank
import com.example.first_SpringBoot_kotlin_Project.service.BankService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/banks")
class BankController(private val service: BankService) {
    @GetMapping
    fun getBanks() : Collection<Bank> = service.getBanks()
}