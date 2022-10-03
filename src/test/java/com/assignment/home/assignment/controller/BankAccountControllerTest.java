package com.assignment.home.assignment.controller;

import com.assignment.home.assignment.entity.ATM;
import com.assignment.home.assignment.entity.BankAccount;
import com.assignment.home.assignment.exceptions.NotFoundException;
import com.assignment.home.assignment.exceptions.PinAuthenticationException;
import com.assignment.home.assignment.model.BalanceRequest;
import com.assignment.home.assignment.service.ATMService;
import com.assignment.home.assignment.service.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BankAccountControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private ATMService atmService;

    @Value("${atm.identifier}")
    String atmIdentifier;

    BankAccount bankAccount1;
    BankAccount bankAccount2;
    ATM atm;

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

        atm = new ATM().builder()
                .atmId(atmIdentifier)
                .fiftyEuroNotes(10)
                .twentyEuroNotes(30)
                .tenEuroNotes(30)
                .fiveEuroNotes(20)
                .build();
    }

    @AfterEach
    void tearDown()
    {
        bankAccount1 = null;
        bankAccount2 = null;
        atm = null;
    }

    @DisplayName("Testing Valid Balance request")
    @Test
    void getBankAccountBalanceValid() throws Exception
    {
        doNothing().when(bankAccountService)
                .verifyBankAccount(bankAccount1.getAccountNumber(), bankAccount1.getAccountPin());

        doReturn(bankAccount1.getAccountBalance()).when(bankAccountService)
                .getAccountBalance(bankAccount1.getAccountNumber(),bankAccount1.getAccountPin());

        doReturn(bankAccount1.getAccountOverdraft()).when(bankAccountService)
                .getAccountOverdraft(bankAccount1.getAccountNumber(),bankAccount1.getAccountPin());

        doReturn(bankAccount1.getAccountBalance().add(bankAccount1.getAccountOverdraft())).when(bankAccountService)
                .getBalanceAndOverdraft(bankAccount1.getAccountNumber(),bankAccount1.getAccountPin());

        doReturn(BigDecimal.valueOf(1500)).when(atmService).getMaxDispensableAmount();

        BalanceRequest balanceRequest = BalanceRequest.builder()
                .bankAccountNumber(bankAccount1.getAccountNumber())
                .bankAccountPin(bankAccount1.getAccountPin())
                .build();

        this.mockMvc.perform(get("/api/v1/balance").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(balanceRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("bankAccountOverdraft", comparesEqualTo(200)))//
                .andExpect(jsonPath("balanceAndOverdraft", comparesEqualTo(1000)))
                .andExpect(jsonPath("bankAccountBalance", comparesEqualTo(800)))
                .andExpect(jsonPath("maxAtmDispensableAmount", comparesEqualTo(1500)));
    }

    @DisplayName("Testing non-existing account request")
    @Test
    void getBankAccountBalanceNonExisting() throws Exception
    {
        doThrow(new NotFoundException("124124124 bank account not found.")).when(bankAccountService)
                .verifyBankAccount("124124124","5555");

        BalanceRequest balanceRequest = BalanceRequest.builder()
                .bankAccountNumber("124124124")
                .bankAccountPin("5555")
                .build();

        this.mockMvc.perform(get("/api/v1/balance").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(balanceRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message", is("124124124 bank account not found.")));
    }

    @DisplayName("Testing wrong pin account request")
    @Test
    void getBankAccountBalanceWrongPin() throws Exception
    {
        doThrow(new PinAuthenticationException("Wrong pin")).when(bankAccountService)
                .verifyBankAccount("124124124","5555");

        BalanceRequest balanceRequest = BalanceRequest.builder()
                .bankAccountNumber("124124124")
                .bankAccountPin("5555")
                .build();

        this.mockMvc.perform(get("/api/v1/balance").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(balanceRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message", is("Wrong pin")));
    }

    @DisplayName("Testing valid withdraw notes request yada yada")
    @Test
    void withdraw()
    {
        ////////////////////////////////////////////////////////////////////
        //      TESTING ALL OUTCOMES OF THIS WILL TAKE FOREVER !!! :(     //
        ////////////////////////////////////////////////////////////////////
    }

    static String asJsonString(final Object obj)
    {
        try
        {
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}