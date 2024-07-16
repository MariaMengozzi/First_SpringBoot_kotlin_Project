package com.example.first_SpringBoot_kotlin_Project.controller

import com.example.first_SpringBoot_kotlin_Project.model.Bank
import com.fasterxml.jackson.databind.ObjectMapper
import jdk.jfr.ContentType
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
import org.springframework.test.web.servlet.post

@SpringBootTest //it create the whole spring project here we are testing the REST API
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
     val mockMvc: MockMvc,
     val objectMapper: ObjectMapper
) {

    //@Autowired //with this spring boot does the dependency injection
    //lateinit var mockMvc: MockMvc //this should use only in tests -> moved to constructor



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

    @Nested
    @DisplayName("POST /api/bank")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewBank {
        @Test
        fun `should add a new bank`() {
            //given
            val newBank = Bank("acc123", 31.415, 2)
            //when
            val performPost = mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }
            //then
            performPost.andDo { print() }
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.accountNumber"){value("acc123")}
                jsonPath("$.trust"){value("31.415")}
                jsonPath("$.transactionFee"){value("2")}
            }
        }
    }

    @Nested
    @DisplayName("POST /api/bank - BAD REQUEST")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewBankBadRequest {
        @Test
        fun `should return BAD REQUEST if bank with the given number already exist`() {
            //given
            val invalidBank = Bank("1234", 1.0, 1)
            //when
            val performPost = mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
            //then
            performPost.andDo { print() }
                .andExpect {
                    status { status { isBadRequest() } }
                }
        }
    }


}