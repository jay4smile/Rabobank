package com.rabobank.transactionservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.BigInteger;


@JsonInclude(content = JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Data
public class Transaction {


    @NotNull(message = "transactionReference is required!")
    @EqualsAndHashCode.Include
    private BigInteger transactionReference;


    @NotNull(message = "accountNumber is required!")
    private String accountNumber;

    @NotNull(message = "startBalance is required!")
    @PositiveOrZero(message = "startBalance should be positive!")
    private BigDecimal startBalance;

    @NotNull(message = "mutation is required!")
    @PositiveOrZero(message = "mutation should be positive!")
    private BigDecimal mutation;
    private String description;

    @NotNull(message = "endBalance is required!")
    @PositiveOrZero(message = "endBalance should be positive!")
    private BigDecimal endBalance;


}
