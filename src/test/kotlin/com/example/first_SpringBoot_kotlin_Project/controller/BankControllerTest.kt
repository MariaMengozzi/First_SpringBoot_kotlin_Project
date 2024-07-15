package com.example.first_SpringBoot_kotlin_Project.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest //it create the whole spring project here we are testing the REST API
@AutoConfigureMockMvc
internal class BankControllerTest {

    @Autowired //with this spring boot does the dependency injection
    lateinit var mockMvc: MockMvc //this should use only in tests

    val baseUrl = "/api/banks"

    @Nested
    @DisplayName("getBanks()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks{
        @Test
        fun `should return all banks`() {
            // when/then
            mockMvc.get(baseUrl)
                .andDo{print()}
                .andExpect{
                    status {isOk()}
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].accountNumber"){ value("1234") }
                }
        }
    }

    @Nested
    @DisplayName("GetBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return the bank with the given account number`() {
            //given
            val accountNumber = "1234"
            //when/then
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo{print()}
                .andExpect{
                    status {isOk()}
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.trust"){ value("3.14") }
                    jsonPath("$.transactionFee"){ value("17") }
                }
        }
    }

    @Nested
    @DisplayName("GetNotExistingBank()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetNotExistingBank {
        @Test
        fun `should return NOT FOUND if the account number does not exist`() {
            //given
            val accountNumber = "does_not_exist"
            //when/then
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo{print()}
                .andExpect{
                    status {isNotFound()}
                }
        }
    }


}