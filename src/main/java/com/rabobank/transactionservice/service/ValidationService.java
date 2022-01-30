package com.rabobank.transactionservice.service;

import com.rabobank.transactionservice.domain.Status;
import com.rabobank.transactionservice.domain.Transaction;
import com.rabobank.transactionservice.domain.ValidationStatus;
import com.rabobank.transactionservice.exception.InvalidDataException;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {


    public ValidationStatus validateService(Transaction transaction) throws Exception {
        boolean isValid = true;
        String message = null;
        boolean addition = (transaction.getStartBalance().doubleValue() + transaction.getMutation().doubleValue()) == transaction.getEndBalance().doubleValue();
        boolean deduction = (transaction.getStartBalance().doubleValue() - transaction.getMutation().doubleValue()) == transaction.getEndBalance().doubleValue();
        if (!addition && !deduction) {
            isValid = false;
        }

        if (!isValid) {
            message = "Invalid Data found!";
        }

        ValidationStatus validationStatus = new ValidationStatus();
        validationStatus.setMessage(message);
        validationStatus.setStatus(!isValid? Status.FAIL: Status.SUCCESS);

        if (!isValid) {
            throw new InvalidDataException(message);
        }

        boolean status = TransactionSet.INSTANCE.addTransaction(transaction);



        if(!status) {
            throw new InvalidDataException("Duplication transaction is found!");
        }

        return validationStatus;
    }
}
