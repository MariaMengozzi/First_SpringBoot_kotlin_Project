package com.example.first_SpringBoot_kotlin_Project.datasource

import com.example.first_SpringBoot_kotlin_Project.model.Bank

interface BankDataSource {
    fun retrieveBanks(): Collection<Bank>
    fun retrieveBank(accountNumber: String) : Bank
}