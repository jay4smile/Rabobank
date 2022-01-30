package com.rabobank.transactionservice.domain;

import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;

@Data
@ToString
public class ErrorRecord {

    private BigInteger reference;
    private String accountNumber;

}
