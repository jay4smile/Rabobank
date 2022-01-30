package com.rabobank.transactionservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;


@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ToString
@Data
public class ValidationStatus {
    private Status status;
    private String message;

}
