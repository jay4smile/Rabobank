package com.rabobank.transactionservice.controller;


import com.rabobank.transactionservice.domain.Transaction;
import com.rabobank.transactionservice.domain.ValidationStatus;
import com.rabobank.transactionservice.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/api/validation")
public class TransactionValidatorController {


    @Autowired
    private ValidationService validationService;


    @PostMapping
    public ValidationStatus validation(@Valid @RequestBody Transaction transaction) throws Exception{
        ValidationStatus validationStatus =  validationService.validateService(transaction);
        return validationStatus;
    }

}
