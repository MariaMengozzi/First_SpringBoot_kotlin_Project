package com.example.first_SpringBoot_kotlin_Project.service

import com.example.first_SpringBoot_kotlin_Project.datasource.mock.BankRepository
import com.example.first_SpringBoot_kotlin_Project.model.Bank
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BankService(private val dataSource: BankRepository) {

    fun getBanks(): Collection<Bank> = dataSource.findAll()

    fun getBank(accountNumber: String): Bank = dataSource.findByIdOrNull(accountNumber)
        ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")

    fun addBank(bank: Bank): Bank =  dataSource.findByIdOrNull(bank.accountNumber)
        ?.let {
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} already exist.")
        } ?: dataSource.save(bank)

    fun updateBank(bank: Bank): Bank = dataSource.findByIdOrNull(bank.accountNumber)?.let {
        dataSource.deleteById(bank.accountNumber)
        dataSource.save(bank)
        } ?: throw NoSuchElementException("Could not find a bank with account number $bank.accountNumber")

    fun deleteBank(accountNumber: String): Unit = dataSource.findByIdOrNull(accountNumber)?.let { dataSource.deleteById(accountNumber) }
        ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")
}