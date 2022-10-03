package com.assignment.home.assignment.service;

import java.math.BigDecimal;
import java.util.Map;

public interface ATMService
{
    BigDecimal getMaxDispensableAmount();
    boolean checkAmountAgainstMaxDispensableAmount(BigDecimal withdrawAmount);
    Map<String, Integer> withdrawCalculateNotes (BigDecimal withdrawAmount);
    boolean dispense(Map<String, Integer> cashMap);
    int getSmallestNote();
}
