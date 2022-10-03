package com.assignment.home.assignment.controller;

import com.assignment.home.assignment.model.WithdrawRequest;
import com.assignment.home.assignment.model.WithdrawResponse;
import com.assignment.home.assignment.service.ATMService;
import com.assignment.home.assignment.service.BankAccountService;
import com.assignment.home.assignment.model.BalanceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1")
public class BankAccountController
{
    @Autowired
    ATMService atmService;

    @Autowired
    BankAccountService bankAccountService;

    @GetMapping("/balance")
    public ResponseEntity<Map<String,BigDecimal>> getBankAccountBalance (@Valid @RequestBody BalanceRequest balanceRequest)
    {
        String bankAccountNumber = balanceRequest.getBankAccountNumber();
        String bankAccountPin = balanceRequest.getBankAccountPin();

        bankAccountService.verifyBankAccount(bankAccountNumber, bankAccountPin);

        Map<String,BigDecimal> responseMap = new HashMap<>();
        BigDecimal bankAccountBalance = bankAccountService.getAccountBalance(bankAccountNumber, bankAccountPin);
        BigDecimal bankAccountOverdraft = bankAccountService.getAccountOverdraft(bankAccountNumber, bankAccountPin);
        BigDecimal balanceAndOverdraft = bankAccountService.getBalanceAndOverdraft(bankAccountNumber, bankAccountPin);
        BigDecimal maxAtmDispensableAmount = atmService.getMaxDispensableAmount();

        responseMap.put("bankAccountBalance", bankAccountBalance);
        responseMap.put("bankAccountOverdraft", bankAccountOverdraft);
        responseMap.put("balanceAndOverdraft", balanceAndOverdraft);
        responseMap.put("maxAtmDispensableAmount", maxAtmDispensableAmount);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawNotes (@Valid @RequestBody WithdrawRequest withdrawRequest)
    {
        String bankAccountNumber = withdrawRequest.getBankAccountNumber();
        String bankAccountPin = withdrawRequest.getBankAccountPin();
        BigDecimal withdrawAmount = withdrawRequest.getWithdrawAmount();

        bankAccountService.verifyBankAccount(bankAccountNumber, bankAccountPin);

        int minimumNoteAvailable = atmService.getSmallestNote();
        Map<String,String> responseMap = new HashMap<>();

        if (minimumNoteAvailable == 0)
        {
            responseMap.put("message","This ATM is out of cash");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        else if (withdrawAmount.compareTo(BigDecimal.valueOf(minimumNoteAvailable)) < 0)
        {
            responseMap.put("message","Minimum dispense amount is "+minimumNoteAvailable);
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        if(atmService.checkAmountAgainstMaxDispensableAmount(withdrawAmount))
        {
            if (withdrawAmount.compareTo(bankAccountService.getBalanceAndOverdraft(bankAccountNumber, bankAccountPin)) <= 0)
            {
                Map<String, Integer> cashMap = atmService.withdrawCalculateNotes(withdrawAmount);

                double amountToWithdraw = cashMap.get("fiftyEuroNotes")*50
                        + cashMap.get("twentyEuroNotes")*20
                        + cashMap.get("tenEuroNotes")*10
                        + cashMap.get("fiveEuroNotes")*5;

                if (bankAccountService.withdraw(bankAccountNumber, bankAccountPin, amountToWithdraw))
                {
                    if (atmService.dispense(cashMap))
                    {
                        WithdrawResponse response = new WithdrawResponse().builder()
                                .fiftyEuroNotes(cashMap.get("fiftyEuroNotes"))
                                .twentyEuroNotes(cashMap.get("twentyEuroNotes"))
                                .tenEuroNotes(cashMap.get("tenEuroNotes"))
                                .fiveEuroNotes(cashMap.get("fiveEuroNotes"))
                                .balance(bankAccountService.getAccountBalance(bankAccountNumber, bankAccountPin))
                                .overdraft(bankAccountService.getAccountOverdraft(bankAccountNumber, bankAccountPin))
                                .build();

                        return new ResponseEntity<>(response, HttpStatus.CREATED);
                    }
                    else
                    {
                        // refund bank account ?
                        responseMap.put("message","Failed to dispense cash");
                        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                else
                {
                    responseMap.put("message","Failed to update bank account, will not dispense");
                    return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            else
            {
                responseMap.put("message","Requested amount is larger than balance and overdraft");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }
        }
        else
        {
            responseMap.put("message","Not enough notes left in ATM to dispense amount");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
    }
}
