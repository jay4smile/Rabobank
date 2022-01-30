package com.rabobank.transactionservice.service;

import com.rabobank.transactionservice.domain.ErrorRecord;
import com.rabobank.transactionservice.domain.Status;
import com.rabobank.transactionservice.domain.Transaction;
import com.rabobank.transactionservice.domain.ValidationStatus;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ValidationService {


    public ValidationStatus validateService(Transaction transaction) throws Exception {
        boolean isValid = true;
        List<ErrorRecord> errorRecordList = new ArrayList<>();
        ValidationStatus validationStatus = new ValidationStatus();
        boolean addition = (transaction.getStartBalance().doubleValue() + transaction.getMutation().doubleValue()) == transaction.getEndBalance().doubleValue();
        boolean deduction = (transaction.getStartBalance().doubleValue() - transaction.getMutation().doubleValue()) == transaction.getEndBalance().doubleValue();
        if (!addition && !deduction) {
            isValid = false;
        }

        if (!isValid) {
            ErrorRecord errorRecord = new ErrorRecord();
            errorRecord.setAccountNumber(transaction.getAccountNumber());
            errorRecord.setReference(transaction.getTransactionReference());
            errorRecordList.add(errorRecord);
        }

        boolean status = TransactionSet.INSTANCE.addTransaction(transaction);


        if(!status) {

            Optional<Transaction> existingTransaction = TransactionSet.INSTANCE.getTransactionSet()
                    .stream()
                    .filter(t -> transaction.getTransactionReference() == transaction.getTransactionReference())
                    .findFirst();
            ErrorRecord errorRecord = new ErrorRecord();
            errorRecord.setAccountNumber(existingTransaction.get().getAccountNumber());
            errorRecord.setReference(existingTransaction.get().getTransactionReference());
            errorRecordList.add(errorRecord);

        }

        if (!status && !isValid) {
            validationStatus.setResult(Status.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);
        }

        if (!status && isValid) {
            validationStatus.setResult(Status.DUPLICATE_REFERENCE);
        }

        if (status && !isValid) {
            validationStatus.setResult(Status.INCORRECT_END_BALANCE);
        }

        if (status && isValid) {
            validationStatus.setResult(Status.SUCCESSFUL);
        }
        validationStatus.setErrorRecords(errorRecordList);

        return validationStatus;
    }
}
