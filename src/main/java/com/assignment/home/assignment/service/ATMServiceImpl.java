package com.assignment.home.assignment.service;

import com.assignment.home.assignment.entity.ATM;
import com.assignment.home.assignment.exceptions.NotFoundException;
import com.assignment.home.assignment.repository.ATMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ATMServiceImpl implements ATMService
{
    @Autowired
    private ATMRepository atmRepository;

    @Value("${atm.identifier}")
    String atmIdentifier;

    public BigDecimal getMaxDispensableAmount()
    {
        Optional<ATM> atmOptional = atmRepository.findById(atmIdentifier);
        if (atmOptional.isEmpty()) throw new NotFoundException("Error connecting to database");
        ATM atm = atmOptional.get();
        return BigDecimal.valueOf(Double.valueOf ((atm.getFiftyEuroNotes()*50.00+ atm.getTwentyEuroNotes()*20.00
                                        + atm.getTenEuroNotes()*10.00 + atm.getFiveEuroNotes()*5.00)));

    }

    public boolean checkAmountAgainstMaxDispensableAmount(BigDecimal withdrawAmount)
    {
        BigDecimal maxDispensableAmount = getMaxDispensableAmount();
        if (withdrawAmount.compareTo(maxDispensableAmount)>0)
            return false;
        else
            return true;
    }

    public Map<String, Integer> withdrawCalculateNotes (BigDecimal withdrawAmount)
    {
        Optional<ATM> atmOptional = atmRepository.findById(atmIdentifier);
        if (atmOptional.isEmpty()) throw new NotFoundException("Error connecting to database");
        ATM atm = atmOptional.get();

        HashMap<String, Integer> cashMapDatabase = new HashMap<>();
        cashMapDatabase.put("fiftyEuroNotes", atm.getFiftyEuroNotes());
        cashMapDatabase.put("twentyEuroNotes", atm.getTwentyEuroNotes());
        cashMapDatabase.put("tenEuroNotes", atm.getTenEuroNotes());
        cashMapDatabase.put("fiveEuroNotes", atm.getFiveEuroNotes());

        HashMap<String, Integer> cashMapReturned = new HashMap<>();
        cashMapReturned.put("fiftyEuroNotes", 0);
        cashMapReturned.put("twentyEuroNotes", 0);
        cashMapReturned.put("tenEuroNotes", 0);
        cashMapReturned.put("fiveEuroNotes", 0);

        while (withdrawAmount.compareTo(BigDecimal.valueOf(0)) > 0)
        {
            if (withdrawAmount.compareTo(BigDecimal.valueOf(50)) >= 0 && (cashMapDatabase.get("fiftyEuroNotes") > 0))
            {
                cashMapDatabase.put("fiftyEuroNotes", cashMapDatabase.get("fiftyEuroNotes")-1);
                cashMapReturned.put("fiftyEuroNotes", cashMapReturned.get("fiftyEuroNotes")+1);
                withdrawAmount = withdrawAmount.subtract(BigDecimal.valueOf(50));
            }
            else if (withdrawAmount.compareTo(BigDecimal.valueOf(20)) >= 0 && (cashMapDatabase.get("twentyEuroNotes") > 0))
            {
                cashMapDatabase.put("twentyEuroNotes", cashMapDatabase.get("twentyEuroNotes")-1);
                cashMapReturned.put("twentyEuroNotes", cashMapReturned.get("twentyEuroNotes")+1);
                withdrawAmount = withdrawAmount.subtract(BigDecimal.valueOf(20));
            }
            else if (withdrawAmount.compareTo(BigDecimal.valueOf(10)) >= 0 && (cashMapDatabase.get("tenEuroNotes") > 0))
            {
                cashMapDatabase.put("tenEuroNotes", cashMapDatabase.get("tenEuroNotes")-1);
                cashMapReturned.put("tenEuroNotes", cashMapReturned.get("tenEuroNotes")+1);
                withdrawAmount = withdrawAmount.subtract(BigDecimal.valueOf(10));
            }
            else if (withdrawAmount.compareTo(BigDecimal.valueOf(5)) >= 0 && (cashMapDatabase.get("fiveEuroNotes") > 0))
            {
                cashMapDatabase.put("fiveEuroNotes", cashMapDatabase.get("fiveEuroNotes")-1);
                cashMapReturned.put("fiveEuroNotes", cashMapReturned.get("fiveEuroNotes")+1);
                withdrawAmount = withdrawAmount.subtract(BigDecimal.valueOf(5));
            }
            else if (withdrawAmount.compareTo(BigDecimal.valueOf(5)) < 0)
            {
                break;
            }
            else
            {
                throw new RuntimeException("Not enough notes to dispense.");
            }
        }
        return cashMapReturned;
    }

    public boolean dispense(Map<String, Integer> cashMap)
    {
        Optional<ATM> atmOptional = atmRepository.findById(atmIdentifier);
        if (atmOptional.isEmpty()) throw new NotFoundException("Error connecting to database");
        ATM atm = atmOptional.get();

        if ((atm.getFiftyEuroNotes()-cashMap.get("fiftyEuroNotes")<0)
            || atm.getTwentyEuroNotes()-cashMap.get("twentyEuroNotes")<0
            || atm.getTenEuroNotes()-cashMap.get("tenEuroNotes")<0
            || atm.getFiveEuroNotes()-cashMap.get("fiveEuroNotes")<0)
            return false;

        atm.setFiftyEuroNotes(atm.getFiftyEuroNotes()-cashMap.get("fiftyEuroNotes"));
        atm.setTwentyEuroNotes(atm.getTwentyEuroNotes()-cashMap.get("twentyEuroNotes"));
        atm.setTenEuroNotes(atm.getTenEuroNotes()-cashMap.get("tenEuroNotes"));
        atm.setFiveEuroNotes(atm.getFiveEuroNotes()-cashMap.get("fiveEuroNotes"));

        atmRepository.save(atm);
        return true;
    }

    public int getSmallestNote()
    {
        Optional<ATM> atmOptional = atmRepository.findById(atmIdentifier);
        if (atmOptional.isEmpty()) throw new NotFoundException("Error connecting to database");
        ATM atm = atmOptional.get();

        if (atm.getFiveEuroNotes()>0)
        {
            return 5;
        }
        else if (atm.getTenEuroNotes()>0)
        {
            return 10;
        }
        else if (atm.getTwentyEuroNotes()>0)
        {
            return 20;
        }
        else if (atm.getFiftyEuroNotes()>0)
        {
            return 50;
        }
        else return 0;
    }
}
