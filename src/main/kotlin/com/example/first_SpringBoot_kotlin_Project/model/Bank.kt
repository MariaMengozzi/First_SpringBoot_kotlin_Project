package com.example.first_SpringBoot_kotlin_Project.model

//the data class automatically generate the equals, hashCode and to string methods
data class Bank(
    val accountNumber: String, //with val and var kotlin generate the getter/setter method
    val trust: Double,
    val transactionFee: Int
)