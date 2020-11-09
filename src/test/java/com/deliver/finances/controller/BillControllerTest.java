package com.deliver.finances.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.deliver.finances.business.facade.CreateBillFacade;
import com.deliver.finances.model.entity.Bill;
import com.deliver.finances.model.vo.CreateBillVO;
import com.deliver.finances.model.vo.DisplayBillVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BillController.class)
class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateBillFacade createBillFacade;

    private static final String BILLS_URL = "/bills";

    @Test
    void shouldReturnPersistedBills() throws Exception {
        DisplayBillVO billVO = new DisplayBillVO();
        billVO.setDescription("Compras Riachuelo");

        List<DisplayBillVO> findAll = Collections.singletonList(billVO);

        String expectedJson = objectMapper.writeValueAsString(findAll);

        when(createBillFacade.listCreatedBills()).thenReturn(findAll);

        mockMvc.perform(get(BILLS_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldTrowValidationErrorsAndReturn400BadRequest() throws Exception {
        CreateBillVO createBillVO = new CreateBillVO();
        createBillVO.setDescription("Compras Riachuelo");

        String body = objectMapper.writeValueAsString(createBillVO);

        mockMvc.perform(
                post(BILLS_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateBillSuccessfuly() throws Exception {
        CreateBillVO createBillVO = new CreateBillVO();
        createBillVO.setDescription("Compras Riachuelo");
        createBillVO.setPaymentDate(LocalDate.now());
        createBillVO.setDueDate(LocalDate.now());
        createBillVO.setOriginalAmount(new BigDecimal("127.30"));

        Bill bill = new Bill();
        bill.setDescription("Compras Riachuelo");

        String requestBody = objectMapper.writeValueAsString(createBillVO);
        String responseBody = objectMapper.writeValueAsString(bill);

        when(createBillFacade.createBill(any())).thenReturn(bill);

        mockMvc.perform(
                post(BILLS_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(content().json(responseBody));
    }

}