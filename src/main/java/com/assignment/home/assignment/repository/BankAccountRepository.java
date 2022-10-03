package com.assignment.home.assignment.repository;

import com.assignment.home.assignment.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String>
{
}
