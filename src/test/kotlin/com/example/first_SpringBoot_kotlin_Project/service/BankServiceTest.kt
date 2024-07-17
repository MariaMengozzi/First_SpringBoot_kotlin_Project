package com.example.first_SpringBoot_kotlin_Project.service
import com.example.first_SpringBoot_kotlin_Project.datasource.mock.BankRepository
import com.example.first_SpringBoot_kotlin_Project.model.Bank
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import kotlin.NoSuchElementException
import kotlin.jvm.optionals.getOrNull


class BankServiceTest {

    private val bankRepository = mockk<BankRepository>(relaxUnitFun = true)

    private val bankService = BankService(bankRepository)
    private val banks = mutableListOf(
        Bank("1234", 3.14, 17),
        Bank("1010", 17.0, 0),
        Bank("5678", 0.0, 100),
    )


    @Test
    fun `should create a new account`() {
        // Configura il comportamento del repository mock
        val accountNumber = "123456"
        val newBank = Bank(accountNumber, 100.0, 5)

        every { bankRepository.findByIdOrNull(accountNumber) } returns banks.find { it.accountNumber == accountNumber }
        every { bankRepository.save(newBank) } returns newBank

        // Chiama il metodo del servizio
        val createdAccount = bankService.addBank(newBank)

        // Verifica che l'account creato non sia nullo
        assertEquals(newBank, createdAccount)
    }

    @Test
    fun `should return an ILLEGAL ARGUMENT EXCEPTION - CREATE`() {
        every { bankRepository.findByIdOrNull(banks[0].accountNumber) } returns banks[0]

        assertThrows<IllegalArgumentException> { bankService.addBank(banks[0]) }
    }

    @Test
    fun `should return all the banks`() {
        every { bankRepository.findAll() } returns banks

        val fetchedBanks = bankService.getBanks()

        assertEquals(banks, fetchedBanks)
    }

    @Test
    fun `should return the bank with the account number`() {

        every { bankRepository.findByIdOrNull(banks[0].accountNumber)} returns banks.firstOrNull {it.accountNumber == banks[0].accountNumber}

        val fetchedBank = bankService.getBank(banks[0].accountNumber)

        assertEquals(banks[0], fetchedBank)
    }

    @Test
    fun `should return a NoSuchElementException - GET`() {
        val wrongNumber = "ase"
        every { bankRepository.findByIdOrNull(wrongNumber)} returns banks.firstOrNull {it.accountNumber == wrongNumber}

        assertThrows<NoSuchElementException> { bankService.getBank(wrongNumber) }
    }

    @Test
    fun `should update the bank with the account number`() {
        val bank = Bank("1234", 1.0, 2)

        every { bankRepository.findByIdOrNull(bank.accountNumber) } returns banks.find { it.accountNumber == bank.accountNumber }
        every { bankRepository.deleteById(bank.accountNumber)} returns banks.removeIf{it.accountNumber == bank.accountNumber}.let { }
        every { bankRepository.save(bank) } answers {
            banks.add(bank)
            bank
        }

        bankService.updateBank(bank)

        assertEquals(banks.firstOrNull {it.accountNumber == bank.accountNumber}, bank)

    }

    @Test
    fun `should return a NoSuchElementException - UPDATE`() {
        val wrongBank = Bank("aaao", 1.0, 2)
        every { bankRepository.findByIdOrNull(wrongBank.accountNumber)} returns banks.firstOrNull {it.accountNumber == wrongBank.accountNumber}

        assertThrows<NoSuchElementException> { bankService.updateBank(wrongBank) }
    }

    @Test
    fun `should delete the bank with the account number`() {
        val bank = Bank("abc1", 1.0, 2)
        banks.add(bank)
        assertTrue(banks.contains(bank))

        every { bankRepository.findByIdOrNull(bank.accountNumber)} returns banks.firstOrNull {it.accountNumber == bank.accountNumber}
        every { bankRepository.deleteById(bank.accountNumber)} answers {
            banks.removeIf{it.accountNumber == bank.accountNumber}
            Unit
        }

        bankService.deleteBank(bank.accountNumber)

        assertNull(banks.firstOrNull {it.accountNumber == bank.accountNumber})

    }

    @Test
    fun `should return a NoSuchElementException - DELETE`() {
        val wrongNumber = "ase"
        every { bankRepository.findByIdOrNull(wrongNumber)} returns banks.firstOrNull {it.accountNumber == wrongNumber}

        assertThrows<NoSuchElementException> { bankService.deleteBank(wrongNumber) }
    }

}