package com.assignment.home.assignment.service;

import com.assignment.home.assignment.entity.BankAccount;
import com.assignment.home.assignment.exceptions.NotFoundException;
import com.assignment.home.assignment.exceptions.PinAuthenticationException;
import com.assignment.home.assignment.repository.BankAccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BankAccountServiceTest
{
    @Autowired
    private BankAccountServiceImpl bankAccountService;

    @MockBean
    private BankAccountRepository bankAccountRepository;

    BankAccount bankAccount1;
    BankAccount bankAccount2;

    @BeforeEach
    void setUp()
    {
        bankAccount1 = new BankAccount().builder()
                .accountNumber("123456789")
                .accountPin("1234")
                .accountBalance(BigDecimal.valueOf(800))
                .accountOverdraft(BigDecimal.valueOf(200))
                .build();

        bankAccount2 = new BankAccount().builder()
                .accountNumber("987654321")
                .accountPin("4321")
                .accountBalance(BigDecimal.valueOf(1230))
                .accountOverdraft(BigDecimal.valueOf(150))
                .build();
    }

    @AfterEach
    void tearDown()
    {
        bankAccount1=null;
        bankAccount2=null;
    }

    @DisplayName("Test verifyBankAccount valid account and pin")
    @Test
    void verifyBankAccountValid()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());
        doReturn(Optional.of(bankAccount2)).when(bankAccountRepository).findById(bankAccount2.getAccountNumber());

        assertDoesNotThrow(() ->
        {
            bankAccountService.verifyBankAccount(bankAccount1.getAccountNumber(), bankAccount1.getAccountPin());
            bankAccountService.verifyBankAccount(bankAccount2.getAccountNumber(), bankAccount2.getAccountPin());
        });
    }

    @DisplayName("Test verifyBankAccount NotFoundException")
    @Test
    void verifyBankAccountNotFoundException()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());
        doReturn(Optional.of(bankAccount2)).when(bankAccountRepository).findById(bankAccount2.getAccountNumber());

        Exception notFoundException = assertThrows(NotFoundException.class, () ->
        {
            bankAccountService.verifyBankAccount("banana","1234");
        });
        assertEquals("banana bank account not found.", notFoundException.getMessage());
    }

    @DisplayName("Test verifyBankAccount PinAuthenticationException")
    @Test
    void verifyBankAccountPinAuthenticationException()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());
        doReturn(Optional.of(bankAccount2)).when(bankAccountRepository).findById(bankAccount2.getAccountNumber());

        Exception pinAuthenticationException = assertThrows(PinAuthenticationException.class, () ->
        {
            bankAccountService.verifyBankAccount(bankAccount1.getAccountNumber(),"banana");
        });
        assertEquals("Wrong pin", pinAuthenticationException.getMessage());

        Exception pinAuthenticationException2 = assertThrows(PinAuthenticationException.class, () ->
        {
            bankAccountService.verifyBankAccount(bankAccount1.getAccountNumber(),bankAccount2.getAccountPin());
        });
        assertEquals("Wrong pin", pinAuthenticationException2.getMessage());
    }

    @DisplayName("Test getAccountBalance valid bank account and pin")
    @Test
    void getAccountBalanceValid()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());

        assertEquals(BigDecimal.valueOf(800), bankAccountService.getAccountBalance("123456789", "1234"));
    }

    @DisplayName("Test getAccountBalance NotFoundException")
    @Test
    void getAccountBalanceNotFoundException()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());

        Exception notFoundException = assertThrows(NotFoundException.class, () ->
        {
            bankAccountService.getAccountBalance("banana","1234");
        });
        assertEquals("banana bank account not found.", notFoundException.getMessage());
    }

    @DisplayName("Test getAccountBalance PinAuthenticationException")
    @Test
    void getAccountBalancePinAuthenticationException()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());

        Exception pinAuthenticationException = assertThrows(PinAuthenticationException.class, () ->
        {
            bankAccountService.getAccountBalance("123456789","4321");
        });
        assertEquals("Wrong pin", pinAuthenticationException.getMessage());
    }

    @DisplayName("Test getAccountOverdraft valid bank account and pin")
    @Test
    void getAccountOverdraftValid()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());

        assertEquals(BigDecimal.valueOf(200), bankAccountService.getAccountOverdraft(String.valueOf(123456789), String.valueOf(1234)));
    }

    @DisplayName("Test getAccountOverdraft NotFoundException")
    @Test
    void getAccountOverdraftNotFoundException()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());

        Exception notFoundException = assertThrows(NotFoundException.class, () ->
        {
            bankAccountService.getAccountOverdraft("banana","1234");
        });
        assertEquals("banana bank account not found.", notFoundException.getMessage());
    }

    @DisplayName("Test getAccountOverdraft PinAuthenticationException")
    @Test
    void getAccountOverdraftPinAuthenticationException()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());

        Exception pinAuthenticationException = assertThrows(PinAuthenticationException.class, () ->
        {
            bankAccountService.getAccountOverdraft("123456789","4321");
        });
        assertEquals("Wrong pin", pinAuthenticationException.getMessage());
    }


    @DisplayName("Test getBalanceAndOverdraft valid bank account and pin")
    @Test
    void getBalanceAndOverdraftValid()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());

        assertEquals(BigDecimal.valueOf(1000), bankAccountService.getBalanceAndOverdraft("123456789", "1234"));
    }

    @DisplayName("Test getBalanceAndOverdraft NotFoundException")
    @Test
    void getBalanceAndOverdraftNotFoundException()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());

        Exception notFoundException = assertThrows(NotFoundException.class, () ->
        {
            bankAccountService.getBalanceAndOverdraft("banana","1234");
        });
        assertEquals("banana bank account not found.", notFoundException.getMessage());
    }

    @DisplayName("Test getBalanceAndOverdraft PinAuthenticationException")
    @Test
    void getBalanceAndOverdraftPinAuthenticationException()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());

        Exception pinAuthenticationException = assertThrows(PinAuthenticationException.class, () ->
        {
            bankAccountService.getBalanceAndOverdraft("123456789","4321");
        });
        assertEquals("Wrong pin", pinAuthenticationException.getMessage());
    }

    @DisplayName("Test withdraw VALID amount with valid bank account and pin")
    @Test
    void withdrawValid()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());
        doReturn(Optional.of(bankAccount2)).when(bankAccountRepository).findById(bankAccount2.getAccountNumber());

        doReturn(bankAccount1).when(bankAccountRepository).save(bankAccount1);
        doReturn(bankAccount2).when(bankAccountRepository).save(bankAccount2);

        assertTrue(bankAccountService.withdraw("123456789","1234", 800));
        assertTrue(bankAccountService.withdraw("123456789","1234", 200));
        assertTrue(bankAccountService.withdraw("987654321","4321", 1230+150));
    }

    @DisplayName("Test withdraw INVALID amount with valid bank account and pin")
    @Test
    void withdrawInvalid()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());
        doReturn(Optional.of(bankAccount2)).when(bankAccountRepository).findById(bankAccount2.getAccountNumber());

        doReturn(bankAccount1).when(bankAccountRepository).save(bankAccount1);
        doReturn(bankAccount2).when(bankAccountRepository).save(bankAccount2);

        assertTrue(bankAccountService.withdraw("123456789","1234", 800));
        assertFalse(bankAccountService.withdraw("123456789","1234", 201));
        assertFalse(bankAccountService.withdraw("987654321","4321", 1230+151));
    }

    @DisplayName("Test withdraw NotFoundException")
    @Test
    void withdrawNotFoundException()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());

        Exception notFoundException = assertThrows(NotFoundException.class, () ->
        {
            bankAccountService.withdraw("banana","1234", 5);
        });
        assertEquals("banana bank account not found.", notFoundException.getMessage());
    }

    @DisplayName("Test withdraw PinAuthenticationException")
    @Test
    void withdrawPinAuthenticationException()
    {
        doReturn(Optional.of(bankAccount1)).when(bankAccountRepository).findById(bankAccount1.getAccountNumber());

        Exception pinAuthenticationException = assertThrows(PinAuthenticationException.class, () ->
        {
            bankAccountService.withdraw("123456789","4321", 5);
        });
        assertEquals("Wrong pin", pinAuthenticationException.getMessage());
    }
}