package com.rabobank.transactionservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
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
        status.setStatus(Status.SUCCESS);
        status.setMessage("Valid");

        when(validationService.validateService(any())).thenReturn(status);
        MvcResult mvcResult = mockMvc.perform(post("/api/validation").content(data).contentType(MediaType.APPLICATION_JSON)).andReturn();
        ValidationStatus status1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationStatus.class);

        assertEquals(Status.SUCCESS, status1.getStatus());
        assertEquals("Valid", status1.getMessage());
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

        ValidationStatus status = new ValidationStatus();
        status.setStatus(Status.SUCCESS);
        status.setMessage("Valid");

        when(validationService.validateService(any())).thenReturn(status);
        MvcResult mvcResult = mockMvc.perform(post("/api/validation").content(data).contentType(MediaType.APPLICATION_JSON)).andReturn();
        ValidationStatus status1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationStatus.class);

        assertEquals(Status.FAIL, status1.getStatus());
        assertEquals("transactionReference is required!", status1.getMessage());
    }


    @Test
    public void testFailedValidationForInvalidData() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setTransactionReference(BigInteger.valueOf(1212L));
        transaction.setAccountNumber("12121121");
        transaction.setDescription("Test Transaction");
        transaction.setEndBalance(new BigDecimal(1212));
        transaction.setMutation(new BigDecimal(1));
        transaction.setStartBalance(new BigDecimal(-1));

        ObjectMapper objectMapper = new ObjectMapper();
        String data = objectMapper.writeValueAsString(transaction);

        ValidationStatus status = new ValidationStatus();
        status.setStatus(Status.SUCCESS);
        status.setMessage("Valid");

        when(validationService.validateService(any())).thenReturn(status);
        MvcResult mvcResult = mockMvc.perform(post("/api/validation").content(data).contentType(MediaType.APPLICATION_JSON)).andReturn();
        ValidationStatus status1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationStatus.class);

        assertEquals(Status.FAIL, status1.getStatus());
        assertEquals("startBalance should be positive!", status1.getMessage());
    }

    @Test
    public void testFailedValidationForInvalidDataEndBalance() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setTransactionReference(BigInteger.valueOf(1212L));
        transaction.setAccountNumber("12121121");
        transaction.setDescription("Test Transaction");
        transaction.setEndBalance(new BigDecimal(-1212));
        transaction.setMutation(new BigDecimal(1));
        transaction.setStartBalance(new BigDecimal(12));

        ObjectMapper objectMapper = new ObjectMapper();
        String data = objectMapper.writeValueAsString(transaction);

        ValidationStatus status = new ValidationStatus();
        status.setStatus(Status.SUCCESS);
        status.setMessage("Valid");

        when(validationService.validateService(any())).thenReturn(status);
        MvcResult mvcResult = mockMvc.perform(post("/api/validation").content(data).contentType(MediaType.APPLICATION_JSON)).andReturn();
        ValidationStatus status1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationStatus.class);

        assertEquals(Status.FAIL, status1.getStatus());
        assertEquals("endBalance should be positive!", status1.getMessage());
    }

}
