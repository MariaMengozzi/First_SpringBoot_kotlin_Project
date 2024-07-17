package com.example.first_SpringBoot_kotlin_Project.controller

import com.example.first_SpringBoot_kotlin_Project.model.Bank
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*

@SpringBootTest //it creates the whole spring project here we are testing the REST API
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
     val mockMvc: MockMvc,
     val objectMapper: ObjectMapper
) {

    //@Autowired //with this spring boot does the dependency injection
    //lateinit var mockMvc: MockMvc //this should use only in tests -> moved to constructor



    val baseUrl = "/api/banks"

    @Nested
    @DisplayName("GET /api/banks")
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
    @DisplayName("GET /api/banks/{accountNumber}")
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
                    /*alternatively we can create an object with these parameters and then compare the entire object
                    with the body content:
                    val bank = Bank("1234", 3.14, 17)
                    .... content{
                        { contentType(MediaType.APPLICATION_JSON) }
                        json(objectMapper.writeValueAsString(bank))
                    }
                    */
                }
        }
    }

    @Nested
    @DisplayName("GET /api/banks/{accountNumber} - NOT FOUND")
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
    @DisplayName("POST /api/banks")
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
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(newBank))
                }
                /*jsonPath("$.accountNumber"){value("acc123")} //we check the entire body object with the json in the content
                jsonPath("$.trust"){value("31.415")}
                jsonPath("$.transactionFee"){value("2")}*/
            }

            //we verify that the bank is correctly updated using the route get > api/banks/{accountNum}
            mockMvc.get("$baseUrl/${newBank.accountNumber}")
                .andExpect {
                    content {
                        json(objectMapper.writeValueAsString(newBank))
                    }
                }
        }
    }

    @Nested
    @DisplayName("POST /api/banks - BAD REQUEST")
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

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchExistingBank {
        @Test
        fun `should update an existing bank`() {
            //given
            val updatedBank = Bank("1234", 1.0, 1)
            //when
            val performPatch = mockMvc.patch(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedBank)
            }
            //then
            performPatch
                .andDo { print() }
                .andExpect {
                    status {
                        status { isOk() }
                        content {
                            contentType(MediaType.APPLICATION_JSON)
                            json(objectMapper.writeValueAsString(updatedBank)) //we are checking the input that is in the body instead checking all single object property
                        }

                    }
                }

            //we verify that the bank is correctly updated using the route get > api/banks/{accountNum}
            mockMvc.get("$baseUrl/${updatedBank.accountNumber}")
                .andExpect {
                    content {
                        json(objectMapper.writeValueAsString(updatedBank))
                    }
                }
        }
    }

    @Nested
    @DisplayName("PATCH /api/banks - NOT FOUND")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchNotExistingBank {
        @Test
        fun `should return NOT FOUND if no bank with the given number exist`() {
            //given
            val invalidBank = Bank("does_not_exist", 1.0, 1)
            //when
            val performPatch = mockMvc.patch(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
            //then
            performPatch.andDo { print() }
                .andExpect {
                    status { status { isNotFound() } }
                }
        }
    }

    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteRequest {
        @Test
        @DirtiesContext
        fun `should delete the bank with the given account number`() {
            //given

            //use a different accountNumber (es 5678) or add @DirtiesContext (for cleaning the context after this test) due to tests are run in a non-deterministic way
            val accountNumber = "1234"
            //when/then
            mockMvc.delete("$baseUrl/$accountNumber")
                .andDo{print()}
                .andExpect{
                    status {isNoContent()}
                }

            //check that is actually deleted
            mockMvc.get("$baseUrl/${accountNumber}")
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber} - NOT FOUND")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteRequestNotFound {
        @Test
        fun `should return NOT FOUND if no bank with the given number exist`() {
            //given
            val invalidAccountNumber = "does_not_exist"
            //when/then
            mockMvc.delete("$baseUrl/$invalidAccountNumber")
                .andDo{print()}
                .andExpect{
                    status {isNotFound()}
                }
        }
    }


}