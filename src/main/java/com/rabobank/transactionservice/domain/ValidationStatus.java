package com.rabobank.transactionservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ToString
@Data
public class ValidationStatus {
    private Status result;
    private List<ErrorRecord> errorRecords;

}
