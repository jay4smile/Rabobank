package com.rabobank.transactionservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.transactionservice.domain.ErrorRecord;
import com.rabobank.transactionservice.domain.Status;
import com.rabobank.transactionservice.domain.Transaction;
import com.rabobank.transactionservice.domain.ValidationStatus;
import com.rabobank.transactionservice.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@AutoConfigureMockMvc
@WebMvcTest()
public class TransactionValidatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private ValidationService validationService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSuccessFulValidation() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setTransactionReference(BigInteger.valueOf(121212L));
        transaction.setAccountNumber("12121121");
        transaction.setDescription("Test Transaction");
        transaction.setEndBalance(new BigDecimal(11));
        transaction.setMutation(new BigDecimal(1));
        transaction.setStartBalance(new BigDecimal(1121));

        ObjectMapper objectMapper = new ObjectMapper();
        String data = objectMapper.writeValueAsString(transaction);

        ValidationStatus status = new ValidationStatus();
        status.setResult(Status.SUCCESSFUL);
        status.setErrorRecords(new ArrayList<>());

        when(validationService.validateService(any())).thenReturn(status);
        MvcResult mvcResult = mockMvc.perform(post("/api/validation").content(data).contentType(MediaType.APPLICATION_JSON)).andReturn();
        ValidationStatus status1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationStatus.class);

        assertEquals(Status.SUCCESSFUL, status1.getResult());
    }


    @Test
    public void testFailedValidationForMissingField() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setAccountNumber("12121121");
        transaction.setDescription("Test Transaction");
        transaction.setEndBalance(new BigDecimal(11));
        transaction.setMutation(new BigDecimal(1));
        transaction.setStartBalance(new BigDecimal(1121));

        ObjectMapper objectMapper = new ObjectMapper();
        String data = objectMapper.writeValueAsString(transaction);

        MvcResult mvcResult = mockMvc.perform(post("/api/validation").content(data).contentType(MediaType.APPLICATION_JSON)).andReturn();
        ValidationStatus status1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationStatus.class);

        assertEquals(Status.BAD_REQUEST, status1.getResult());
    }


    @Test
    public void testFailedValidationForInvalidDataEndBalance() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setTransactionReference(BigInteger.valueOf(1212L));
        transaction.setAccountNumber("12121121");
        transaction.setDescription("Test Transaction");
        transaction.setEndBalance(new BigDecimal(14));
        transaction.setMutation(new BigDecimal(1));
        transaction.setStartBalance(new BigDecimal(12));

        ObjectMapper objectMapper = new ObjectMapper();
        String data = objectMapper.writeValueAsString(transaction);

        ValidationStatus status = new ValidationStatus();
        status.setResult(Status.INCORRECT_END_BALANCE);

        ErrorRecord errorRecord = new ErrorRecord();
        errorRecord.setReference(transaction.getTransactionReference());
        errorRecord.setAccountNumber(transaction.getAccountNumber());

        List<ErrorRecord>  errorRecordList = new ArrayList<>();
        errorRecordList.add(errorRecord);

        status.setErrorRecords(errorRecordList);
        when(validationService.validateService(any())).thenReturn(status);
        MvcResult mvcResult = mockMvc.perform(post("/api/validation").content(data).contentType(MediaType.APPLICATION_JSON)).andReturn();
        ValidationStatus status1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationStatus.class);

        assertEquals(Status.INCORRECT_END_BALANCE, status1.getResult());
        assertEquals("12121121", status1.getErrorRecords().get(0).getAccountNumber());
    }

}
