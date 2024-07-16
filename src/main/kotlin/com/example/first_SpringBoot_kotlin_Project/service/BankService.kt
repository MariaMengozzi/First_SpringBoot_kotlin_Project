package com.example.first_SpringBoot_kotlin_Project.service

import com.example.first_SpringBoot_kotlin_Project.datasource.BankDataSource
import com.example.first_SpringBoot_kotlin_Project.model.Bank
import org.springframework.stereotype.Service

@Service
class BankService(private val dataSource: BankDataSource) {

    fun getBanks(): Collection<Bank> = dataSource.retrieveBanks()
    fun getBank(accountNumber: String): Bank = dataSource.retrieveBank(accountNumber)
    fun addBank(bank: Bank): Bank = dataSource.createBank(bank)

}