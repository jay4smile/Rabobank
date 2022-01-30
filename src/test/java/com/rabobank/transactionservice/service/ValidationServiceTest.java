package com.rabobank.transactionservice.service;

import com.rabobank.transactionservice.domain.Status;
import com.rabobank.transactionservice.domain.Transaction;
import com.rabobank.transactionservice.domain.ValidationStatus;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ValidationServiceTest {

    @InjectMocks
    @Autowired
    ValidationService validationService;

    @Test
    public void testSuccessFullTransaction() throws Exception{
        Transaction transaction = new Transaction();
        transaction.setTransactionReference(BigInteger.valueOf(121212L));
        transaction.setAccountNumber("12121121");
        transaction.setDescription("Test Transaction");
        transaction.setEndBalance(new BigDecimal(1122));
        transaction.setMutation(new BigDecimal(1));
        transaction.setStartBalance(new BigDecimal(1121));
        TransactionSet.INSTANCE.getTransactionSet().clear();
        ValidationStatus status = validationService.validateService(transaction);

        assertEquals(Status.SUCCESSFUL, status.getResult());
    }

    @Test
    public void testFailedFullTransactionWithInvalidMutation() throws Exception{
        TransactionSet.INSTANCE.getTransactionSet().clear();
        Transaction transaction = new Transaction();
        transaction.setTransactionReference(BigInteger.valueOf(121212L));
        transaction.setAccountNumber("12121121");
        transaction.setDescription("Test Transaction");
        transaction.setEndBalance(new BigDecimal(112));
        transaction.setMutation(new BigDecimal(1));
        transaction.setStartBalance(new BigDecimal(1121));

        ValidationStatus validationStatus =    validationService.validateService(transaction);


        assertEquals(Status.INCORRECT_END_BALANCE,validationStatus.getResult());
        assertEquals("12121121",validationStatus.getErrorRecords().get(0).getAccountNumber());
    }

    @Test
    public void testFailedFullTransactionWithDuplicateTransaction() throws Exception{
        Transaction transaction = new Transaction();
        transaction.setTransactionReference(BigInteger.valueOf(121212L));
        transaction.setAccountNumber("12121121");
        transaction.setDescription("Test Transaction");
        transaction.setEndBalance(new BigDecimal(1122));
        transaction.setMutation(new BigDecimal(1));
        transaction.setStartBalance(new BigDecimal(1121));

        validationService.validateService(transaction);
        ValidationStatus status =  validationService.validateService(transaction);


        assertEquals(Status.DUPLICATE_REFERENCE, status.getResult());
    }

    @Test
    public void testFailedFullTransactionWithDuplicateTransactionAndInvalidMutation() throws Exception{
        Transaction transaction = new Transaction();
        transaction.setTransactionReference(BigInteger.valueOf(121212L));
        transaction.setAccountNumber("12121121");
        transaction.setDescription("Test Transaction");
        transaction.setEndBalance(new BigDecimal(1122));
        transaction.setMutation(new BigDecimal(1));
        transaction.setStartBalance(new BigDecimal(1121));

        validationService.validateService(transaction);

        Transaction transaction1 = new Transaction();
        transaction1.setTransactionReference(BigInteger.valueOf(121212L));
        transaction1.setAccountNumber("12121121");
        transaction1.setDescription("Test Transaction");
        transaction1.setEndBalance(new BigDecimal(112));
        transaction1.setMutation(new BigDecimal(1));
        transaction1.setStartBalance(new BigDecimal(1121));

        ValidationStatus status =  validationService.validateService(transaction1);


        assertEquals(Status.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, status.getResult());
    }
}
