package com.example.first_SpringBoot_kotlin_Project.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
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
    
    @Test
    fun `should return all banks`() {
        // when/then
        mockMvc.get("/api/banks")
            .andDo{print()}
            .andExpect{
                status {isOk()}
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$[0].accountNumber"){ value("1234") }
            }
    }
}