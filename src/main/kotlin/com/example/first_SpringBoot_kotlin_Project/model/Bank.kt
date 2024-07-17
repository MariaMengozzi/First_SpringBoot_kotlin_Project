package com.example.first_SpringBoot_kotlin_Project.model

import jakarta.persistence.*

//the data class automatically generate the equals, hashCode and to string methods
@Entity
@Table(name = "bank")
data class Bank(
    @Id
    @Column(name = "account_number")
    val accountNumber: String, //with val and var kotlin generate the getter/setter method
    @Column(name = "trust")
    val trust: Double,
    @Column(name = "transaction_fee")
    val transactionFee: Int
)