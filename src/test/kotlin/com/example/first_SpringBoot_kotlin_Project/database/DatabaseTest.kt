package com.example.first_SpringBoot_kotlin_Project.database
import com.example.first_SpringBoot_kotlin_Project.datasource.mock.BankRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class DatabaseTest {

    @Autowired
    private lateinit var bankRepository: BankRepository

    @Test
    fun `Test the database connection`() {
        // Verifica che il repository sia in grado di comunicare con il database
        bankRepository.findAll()
    }
}
