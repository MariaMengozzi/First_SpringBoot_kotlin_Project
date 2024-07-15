package com.example.first_SpringBoot_kotlin_Project.service

import com.example.first_SpringBoot_kotlin_Project.datasource.BankDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BankServiceTest{
    /*A Mock Object is an object that substitutes for a real object. In object-oriented programming,
    mock objects are simulated objects that mimic the behavior of real objects in controlled ways*/

    private val dataSource: BankDataSource = mockk(relaxed = true) //return a default value - in this case an empty list
    private val bankService = BankService(dataSource)
    @Test
    fun `should call its data source to retrieve banks`() {
        // when
        bankService.getBanks()
        // then
        verify(exactly = 1) {dataSource.retrieveBanks()}
    }
    
}