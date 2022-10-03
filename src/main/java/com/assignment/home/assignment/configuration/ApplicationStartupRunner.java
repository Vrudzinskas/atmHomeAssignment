package com.assignment.home.assignment.configuration;

import com.assignment.home.assignment.entity.ATM;
import com.assignment.home.assignment.entity.BankAccount;
import com.assignment.home.assignment.repository.ATMRepository;
import com.assignment.home.assignment.repository.BankAccountRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class ApplicationStartupRunner implements CommandLineRunner
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    ATMRepository atmRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Value("${atm.identifier}")
    String atmIdentifier;

    @Override
    public void run(String... args) throws Exception
    {
        BankAccount bankAccount1 = new BankAccount().builder()
                .accountNumber("123456789")
                .accountPin("1234")
                .accountBalance(BigDecimal.valueOf(800.00))
                .accountOverdraft(BigDecimal.valueOf(200.00))
                .build();
        bankAccountRepository.save(bankAccount1);
        logger.info("Created: "+bankAccount1);


        BankAccount bankAccount2 = new BankAccount().builder()
                .accountNumber("987654321")
                .accountPin("4321")
                .accountBalance(BigDecimal.valueOf(1230.00))
                .accountOverdraft(BigDecimal.valueOf(150.00))
                .build();
        bankAccountRepository.save(bankAccount2);
        logger.info("Created: "+bankAccount2);

        ATM atm = new ATM().builder()
                .atmId(atmIdentifier)
                .fiftyEuroNotes(10)
                .twentyEuroNotes(30)
                .tenEuroNotes(30)
                .fiveEuroNotes(20)
                .build();

        atmRepository.save(atm);
        logger.info("Created: "+atm);

        logger.info("Application Started !!");
    }
}
