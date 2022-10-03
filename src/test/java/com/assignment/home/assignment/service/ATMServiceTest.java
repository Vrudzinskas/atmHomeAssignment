package com.assignment.home.assignment.service;

import com.assignment.home.assignment.entity.ATM;
import com.assignment.home.assignment.exceptions.NotFoundException;
import com.assignment.home.assignment.repository.ATMRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ATMServiceTest
{
    @Autowired
    private ATMServiceImpl atmService;

    @MockBean
    private ATMRepository atmRepository;

    ATM atm;

    @BeforeEach
    void setUp()
    {
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
        atm = null;
    }

    @Value("${atm.identifier}")
    String atmIdentifier;

    @DisplayName("Testing for getMaxDispensableAmount method Success")
    @Test
    void getMaxDispensableAmountSuccess()
    {
        doReturn(Optional.of(atm)).when(atmRepository).findById(atmIdentifier);

        BigDecimal maxDispensableAmount = atmService.getMaxDispensableAmount();
        assertEquals(BigDecimal.valueOf(1500.0), maxDispensableAmount);
    }

    @DisplayName("Testing for getMaxDispensableAmount method Exception")
    @Test
    void getMaxDispensableAmountException()
    {
        doReturn(Optional.empty()).when(atmRepository).findById(atmIdentifier);

        Exception exception = assertThrows(NotFoundException.class, () ->
        {
            atmService.getMaxDispensableAmount();
        });
        assertEquals("Error connecting to database", exception.getMessage());
    }

    @DisplayName("Testing withdraw amount against available notes SUCCESS")
    @Test
    void checkAmountAgainstMaxDispensableAmountSuccess()
    {
        doReturn(Optional.of(atm)).when(atmRepository).findById(atmIdentifier);

        boolean correct1 = atmService.checkAmountAgainstMaxDispensableAmount(BigDecimal.valueOf(1500));
        boolean correct2 = atmService.checkAmountAgainstMaxDispensableAmount(BigDecimal.valueOf(1000));
        boolean wrong1 = atmService.checkAmountAgainstMaxDispensableAmount(BigDecimal.valueOf(1501));
        boolean wrong2 = atmService.checkAmountAgainstMaxDispensableAmount(BigDecimal.valueOf(10000));

        assertEquals(correct1, true);
        assertEquals(correct2, true);
        assertEquals(wrong1, false);
        assertEquals(wrong2, false);
    }

    @DisplayName("Testing withdraw amount against available notes Exception")
    @Test
    void checkAmountAgainstMaxDispensableAmountException()
    {
        doReturn(Optional.empty()).when(atmRepository).findById(atmIdentifier);

        Exception exception = assertThrows(NotFoundException.class, () ->
        {
            atmService.checkAmountAgainstMaxDispensableAmount(BigDecimal.valueOf(5));
        });
        assertEquals("Error connecting to database", exception.getMessage());
    }

    @DisplayName("Testing withdrawCalculateNotes SUCCESS")
    @Test
    void withdrawCalculateNotesSUCCESS()
    {
        doReturn(Optional.of(atm)).when(atmRepository).findById(atmIdentifier);

        BigDecimal five = BigDecimal.valueOf(5);
        BigDecimal nine = BigDecimal.valueOf(9);
        BigDecimal twentyThree = BigDecimal.valueOf(23);
        BigDecimal hundred = BigDecimal.valueOf(100);
        BigDecimal hundredSixtyEight = BigDecimal.valueOf(168);
        BigDecimal fiveHundred = BigDecimal.valueOf(500);
        BigDecimal euros1504 = BigDecimal.valueOf(1504);

        HashMap<String, Integer> fiveCashMap = new HashMap<>();
        fiveCashMap.put("fiftyEuroNotes", 0);
        fiveCashMap.put("twentyEuroNotes", 0);
        fiveCashMap.put("tenEuroNotes", 0);
        fiveCashMap.put("fiveEuroNotes", 1);
        assertEquals(fiveCashMap, atmService.withdrawCalculateNotes(five));

        HashMap<String, Integer> nineCashMap = new HashMap<>();
        nineCashMap.put("fiftyEuroNotes", 0);
        nineCashMap.put("twentyEuroNotes", 0);
        nineCashMap.put("tenEuroNotes", 0);
        nineCashMap.put("fiveEuroNotes", 1);
        assertEquals(nineCashMap, atmService.withdrawCalculateNotes(nine));

        HashMap<String, Integer> twentyThreeCashMap = new HashMap<>();
        twentyThreeCashMap.put("fiftyEuroNotes", 0);
        twentyThreeCashMap.put("twentyEuroNotes", 1);
        twentyThreeCashMap.put("tenEuroNotes", 0);
        twentyThreeCashMap.put("fiveEuroNotes", 0);
        assertEquals(twentyThreeCashMap, atmService.withdrawCalculateNotes(twentyThree));

        HashMap<String, Integer> hundredCashMap = new HashMap<>();
        hundredCashMap.put("fiftyEuroNotes", 2);
        hundredCashMap.put("twentyEuroNotes", 0);
        hundredCashMap.put("tenEuroNotes", 0);
        hundredCashMap.put("fiveEuroNotes", 0);
        assertEquals(hundredCashMap, atmService.withdrawCalculateNotes(hundred));

        HashMap<String, Integer> hundredSixtyEightCashMap = new HashMap<>();
        hundredSixtyEightCashMap.put("fiftyEuroNotes", 3);
        hundredSixtyEightCashMap.put("twentyEuroNotes", 0);
        hundredSixtyEightCashMap.put("tenEuroNotes", 1);
        hundredSixtyEightCashMap.put("fiveEuroNotes", 1);
        assertEquals(hundredSixtyEightCashMap, atmService.withdrawCalculateNotes(hundredSixtyEight));

        Map<String, Integer> fiveHundredCashMap = new HashMap<>();
        fiveHundredCashMap.put("fiftyEuroNotes", 10);
        fiveHundredCashMap.put("twentyEuroNotes", 0);
        fiveHundredCashMap.put("tenEuroNotes", 0);
        fiveHundredCashMap.put("fiveEuroNotes", 0);
        assertEquals(fiveHundredCashMap, atmService.withdrawCalculateNotes(fiveHundred));

        Map<String, Integer> euros1504CashMap = new HashMap<>();
        euros1504CashMap.put("fiftyEuroNotes", 10);
        euros1504CashMap.put("twentyEuroNotes", 30);
        euros1504CashMap.put("tenEuroNotes", 30);
        euros1504CashMap.put("fiveEuroNotes", 20);
        assertEquals(euros1504CashMap, atmService.withdrawCalculateNotes(euros1504));
    }

    @DisplayName("Testing withdrawCalculateNotes RuntimeException")
    @Test
    void withdrawCalculateNotesRuntimeException()
    {
        doReturn(Optional.of(atm)).when(atmRepository).findById(atmIdentifier);

        BigDecimal withdrawAmount = BigDecimal.valueOf(1505);
        Exception exception = assertThrows(RuntimeException.class, () ->
        {
            Map<String, Integer> cashmap = atmService.withdrawCalculateNotes(withdrawAmount);
        });
        assertEquals("Not enough notes to dispense.", exception.getMessage());
    }

    @DisplayName("Testing withdrawCalculateNotes NotFoundException")
    @Test
    void withdrawCalculateNotesNotFoundException()
    {
        doReturn(Optional.empty()).when(atmRepository).findById(atmIdentifier);

        BigDecimal withdrawAmount = BigDecimal.valueOf(1505);
        Exception exception = assertThrows(RuntimeException.class, () ->
        {
            Map<String, Integer> cashmap = atmService.withdrawCalculateNotes(withdrawAmount);
        });
        assertEquals("Error connecting to database", exception.getMessage());
    }

    @DisplayName("Testing valid cashMap dispense")
    @Test
    void dispenseValidCashmap()
    {
        HashMap<String, Integer> cashMap = new HashMap<>();
        cashMap.put("fiftyEuroNotes", 10);
        cashMap.put("twentyEuroNotes", 30);
        cashMap.put("tenEuroNotes", 30);
        cashMap.put("fiveEuroNotes", 20);

        doReturn(Optional.of(atm)).when(atmRepository).findById(atmIdentifier);
        doReturn(atm).when(atmRepository).save(atm);

        assertTrue(atmService.dispense(cashMap));
    }

    @DisplayName("Testing invalid cashMap dispense")
    @Test
    void dispenseInvalidCashmap()
    {
        HashMap<String, Integer> cashMap = new HashMap<>();
        cashMap.put("fiftyEuroNotes", 10);
        cashMap.put("twentyEuroNotes", 30);
        cashMap.put("tenEuroNotes", 30);
        cashMap.put("fiveEuroNotes", 21);

        doReturn(Optional.of(atm)).when(atmRepository).findById(atmIdentifier);
        doReturn(atm).when(atmRepository).save(atm);

        assertFalse(atmService.dispense(cashMap));
    }

    @DisplayName("Testing dispense NotFoundException")
    @Test
    void dispenseNotFoundException()
    {
        HashMap<String, Integer> cashMap = new HashMap<>();
        cashMap.put("fiftyEuroNotes", 10);
        cashMap.put("twentyEuroNotes", 30);
        cashMap.put("tenEuroNotes", 30);
        cashMap.put("fiveEuroNotes", 20);

        doReturn(Optional.empty()).when(atmRepository).findById(atmIdentifier);
        doReturn(atm).when(atmRepository).save(atm);

        Exception exception = assertThrows(RuntimeException.class, () ->
        {
            boolean flag  = atmService.dispense(cashMap);
        });
        assertEquals("Error connecting to database", exception.getMessage());
    }

    @DisplayName("Testing getSmallestNote")
    @Test
    void getSmallestNote()
    {
        doReturn(Optional.of(atm)).when(atmRepository).findById(atmIdentifier);
        assertEquals(atmService.getSmallestNote(), 5);

        ATM atm2 = new ATM().builder()
                .atmId(atmIdentifier)
                .fiftyEuroNotes(1)
                .twentyEuroNotes(0)
                .tenEuroNotes(0)
                .fiveEuroNotes(0)
                .build();
        doReturn(Optional.of(atm2)).when(atmRepository).findById(atmIdentifier);
        assertEquals(atmService.getSmallestNote(), 50);
    }

    @DisplayName("Testing getSmallestNote NotFoundException")
    @Test
    void getSmallestNoteNotFoundException()
    {
        doReturn(Optional.empty()).when(atmRepository).findById(atmIdentifier);

        Exception exception = assertThrows(RuntimeException.class, () ->
        {
            double note  = atmService.getSmallestNote();
        });
        assertEquals("Error connecting to database", exception.getMessage());
    }
}