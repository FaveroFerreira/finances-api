package com.deliver.finances.business.facade;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.deliver.finances.business.service.BillService;
import com.deliver.finances.business.service.InterestConfigurationService;
import com.deliver.finances.model.entity.Bill;
import com.deliver.finances.model.entity.InterestConfiguration;
import com.deliver.finances.model.vo.CreateBillVO;
import com.deliver.finances.model.vo.DisplayBillVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class CreateBillFacadeTest {

    @Mock
    private InterestConfigurationService interestConfigurationService;

    @Mock
    private BillService billService;

    @InjectMocks
    private CreateBillFacade createBillFacade;

    @Test
    void listCreatedBillsWithRequiredFields() {
        final LocalDate dueDate = LocalDate.now().minusDays(2);
        final BigDecimal originalAmount = new BigDecimal("127.41");
        final BigDecimal totalInterest = new BigDecimal("6.24");
        final BigDecimal totalFine = new BigDecimal("8.92");

        Bill bill = generateParameterizedBill(dueDate, originalAmount, totalInterest, totalFine);
        Bill bill2 = generateParameterizedBill(LocalDate.now(), BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO);

        when(billService.listBills()).thenReturn(Arrays.asList(bill, bill2));

        final List<DisplayBillVO> displayBillVOs = createBillFacade.listCreatedBills();

        assertAll("displayBillVOs", () -> {
            assertFalse(displayBillVOs.isEmpty(), () -> "displayBillVOs is Empty");
            assertTrue(displayBillVOs.get(0).getNumberOfDelayedDays() > 0, () -> "displayBillVOs(0) has no delayed days");
            assertEquals(0, displayBillVOs.get(1).getNumberOfDelayedDays(), () -> "displayBillVOs(1) has delayed days");
        });
    }

    @Test
    void createBillWhenPaymentIsNotDelayed() {
        final Long delayedDays = 0L;
        final LocalDate dueDate = LocalDate.now().minusDays(delayedDays);
        final BigDecimal originalAmount = new BigDecimal("127.41");
        final BigDecimal totalInterest = new BigDecimal("0.00");
        final BigDecimal totalFine = new BigDecimal("0.00");

        CreateBillVO createBillVO = generateDelayedCreateBillVO(dueDate, originalAmount);
        Bill bill = generateParameterizedBill(dueDate, originalAmount, totalInterest, totalFine);

        when(billService.save(bill)).thenReturn(bill);

        final Bill createdBill = createBillFacade.createBill(createBillVO);

        assertAll("createdBill", () -> {
            assertNotNull(createdBill, () -> "createdBill is NULL");
            assertEquals(originalAmount, createdBill.getOriginalAmount(), () -> "Original amount does not match");
            assertEquals(createBillVO.getOriginalAmount(), createdBill.getCorrectedAmount(), () -> "Corrected amount does not match");
        });
    }

    @Test
    void createBillWhenBillPaymentDateIsDelayed() {
        final Long delayedDays = 7L;
        final LocalDate dueDate = LocalDate.now().minusDays(delayedDays);
        final BigDecimal originalAmount = new BigDecimal("127.41");
        final BigDecimal totalInterest = new BigDecimal("6.24");
        final BigDecimal totalFine = new BigDecimal("8.92");

        final BigDecimal interestConfigurationInterest = new BigDecimal("0.0070");
        final BigDecimal interestConfigurationValue = new BigDecimal("0.0700");

        CreateBillVO createBillVO = generateDelayedCreateBillVO(dueDate, originalAmount);
        InterestConfiguration interestConfiguration =
                generateInterestConfigurationForDelayedDays(delayedDays, interestConfigurationInterest, interestConfigurationValue);
        Bill bill = generateParameterizedBill(dueDate, originalAmount, totalInterest, totalFine);

        when(interestConfigurationService.findInterestConfigurationRangeByNumberOfDelayedDays(delayedDays.intValue()))
                .thenReturn(interestConfiguration);
        when(billService.save(bill)).thenReturn(bill);

        final Bill createdBill = createBillFacade.createBill(createBillVO);

        assertAll("createdBill", () -> {
            assertNotNull(createdBill, () -> "createdBill is NULL");
            assertEquals(new BigDecimal("142.57"), createdBill.getCorrectedAmount(), () -> "Corrected amount does not match");
        });

    }

    private Bill generateParameterizedBill(LocalDate dueDate,
                                           BigDecimal originalAmount,
                                           BigDecimal totalInterest,
                                           BigDecimal totalFine) {
        Bill bill = new Bill();
        bill.setId(null);
        bill.setDescription("Compras Riachuelo");
        bill.setOriginalAmount(originalAmount);
        bill.setPaymentDate(LocalDate.now());
        bill.setDueDate(dueDate);
        bill.setCorrectedAmount(originalAmount.add(totalFine).add(totalInterest));
        return bill;
    }

    private InterestConfiguration generateInterestConfigurationForDelayedDays(Long delayedDays,
                                                                              BigDecimal interestConfigurationInterest,
                                                                              BigDecimal interestConfigurationValue) {
        InterestConfiguration interestConfiguration = new InterestConfiguration();
        interestConfiguration.setId(1L);
        interestConfiguration.setStartRange(delayedDays.intValue());
        interestConfiguration.setEndRange(delayedDays.intValue() + 1);
        interestConfiguration.setInterest(interestConfigurationInterest);
        interestConfiguration.setFine(interestConfigurationValue);
        return interestConfiguration;
    }

    private CreateBillVO generateDelayedCreateBillVO(LocalDate dueDate, BigDecimal originalAmount) {
        CreateBillVO createBillVO = new CreateBillVO();
        createBillVO.setDescription("Compras Riachuelo");
        createBillVO.setDueDate(dueDate);
        createBillVO.setPaymentDate(LocalDate.now());
        createBillVO.setOriginalAmount(originalAmount.setScale(2, RoundingMode.HALF_UP));
        return createBillVO;
    }

}