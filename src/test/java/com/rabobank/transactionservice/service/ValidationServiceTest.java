package com.rabobank.transactionservice.service;

import com.rabobank.transactionservice.domain.Status;
import com.rabobank.transactionservice.domain.Transaction;
import com.rabobank.transactionservice.domain.ValidationStatus;
import com.rabobank.transactionservice.exception.InvalidDataException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        assertEquals(Status.SUCCESS, status.getStatus());
    }

    @Test
    public void testFailedFullTransactionWithInvalidMutation() throws Exception{
        Transaction transaction = new Transaction();
        transaction.setTransactionReference(BigInteger.valueOf(121212L));
        transaction.setAccountNumber("12121121");
        transaction.setDescription("Test Transaction");
        transaction.setEndBalance(new BigDecimal(112));
        transaction.setMutation(new BigDecimal(1));
        transaction.setStartBalance(new BigDecimal(1121));

        Exception exception = assertThrows(InvalidDataException.class , () -> {
            validationService.validateService(transaction);
        });


        assertEquals("Invalid Data found!", exception.getMessage());
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

        Exception exception = assertThrows(InvalidDataException.class , () -> {
            validationService.validateService(transaction);
            validationService.validateService(transaction);
        });


        assertEquals("Duplication transaction is found!", exception.getMessage());
    }
}
