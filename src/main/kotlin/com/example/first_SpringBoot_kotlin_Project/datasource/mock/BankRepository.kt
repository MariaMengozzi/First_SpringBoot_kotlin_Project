package com.example.first_SpringBoot_kotlin_Project.datasource.mock

import com.example.first_SpringBoot_kotlin_Project.model.Bank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BankRepository:JpaRepository<Bank, String>