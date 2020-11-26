package com.deliver.finances.business.facade;

import static java.util.stream.Collectors.toList;

import com.deliver.finances.business.service.BillService;
import com.deliver.finances.business.service.InterestConfigurationService;
import com.deliver.finances.config.flags.FeatureFlags;
import com.deliver.finances.model.entity.Bill;
import com.deliver.finances.model.entity.InterestConfiguration;
import com.deliver.finances.model.vo.CreateBillVO;
import com.deliver.finances.model.vo.DisplayBillVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CreateBillFacade {

    private final InterestConfigurationService interestConfigurationService;
    private final BillService billService;

    public CreateBillFacade(InterestConfigurationService interestConfigurationService, BillService billService) {
        this.interestConfigurationService = interestConfigurationService;
        this.billService = billService;
    }

    public Bill createBill(CreateBillVO createBillVO) {
        Bill bill = populateEntity(createBillVO);

        if (bill.isDelayed()) {
            performAdjustments(bill);
        }

        return billService.save(bill);
    }

    public List<DisplayBillVO> listCreatedBills() {
        final List<Bill> bills = billService.listBills();

        return bills.stream()
                .map(DisplayBillVO::new)
                .collect(toList());
    }

    private void performAdjustments(Bill bill) {
        Integer numberOfDelayedDays = bill.getNumberOfDelayedDays();

        InterestConfiguration interestConfiguration = interestConfigurationService
                .findInterestConfigurationRangeByNumberOfDelayedDays(numberOfDelayedDays);

        BigDecimal totalInterest = calculateTotalInterest(numberOfDelayedDays, bill.getOriginalAmount(), interestConfiguration.getInterest());
        BigDecimal fine = calculateFine(bill.getOriginalAmount(), interestConfiguration.getFine());

        bill.setCorrectedAmount(bill.getOriginalAmount().add(totalInterest).add(fine));
    }

    private BigDecimal calculateFine(BigDecimal originalAmount, BigDecimal fine) {
        return originalAmount.multiply(fine)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTotalInterest(Integer numberOfDelayedDays, BigDecimal originalAmount, BigDecimal interest) {
        if (FeatureFlags.CALCULATE_COMPOSED_INTEREST.isActive()) {
            return interest.add(BigDecimal.ONE).pow(numberOfDelayedDays);
        }

        return originalAmount.multiply(interest)
                .multiply(BigDecimal.valueOf(numberOfDelayedDays))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Bill populateEntity(CreateBillVO createBillVO) {
        Bill bill = new Bill();
        bill.setDescription(createBillVO.getDescription());
        bill.setOriginalAmount(createBillVO.getOriginalAmount().setScale(2, RoundingMode.HALF_UP));
        bill.setCorrectedAmount(createBillVO.getOriginalAmount().setScale(2, RoundingMode.HALF_UP));
        bill.setDueDate(createBillVO.getDueDate());
        bill.setPaymentDate(createBillVO.getPaymentDate());
        return bill;
    }

}
