package com.example.first_SpringBoot_kotlin_Project.datasource.mock

import com.example.first_SpringBoot_kotlin_Project.datasource.BankDataSource
import com.example.first_SpringBoot_kotlin_Project.model.Bank
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource:BankDataSource {

    val banks = mutableListOf(
        Bank("1234", 3.14, 17),
        Bank("1010", 17.0, 0),
        Bank("5678", 0.0, 100),
    )

    override fun retrieveBanks(): Collection<Bank> = banks
    override fun retrieveBank(accountNumber: String): Bank =
        banks.firstOrNull { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")

    override fun createBank(bank: Bank): Bank {
        if (banks.any{it.accountNumber == bank.accountNumber}){
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} already exist.")
        }
        banks.add(bank)
        return bank
    }

}