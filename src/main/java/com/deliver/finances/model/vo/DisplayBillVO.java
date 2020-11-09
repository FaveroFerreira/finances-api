package com.deliver.finances.model.vo;

import com.deliver.finances.model.entity.Bill;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisplayBillVO {

    private String description;
    private BigDecimal originalAmount;
    private BigDecimal totalAmount;
    private Integer numberOfDelayedDays;
    private LocalDate paymentDate;

    public DisplayBillVO(Bill bill) {
        this.description = bill.getDescription();
        this.originalAmount = bill.getOriginalAmount();
        this.paymentDate = bill.getPaymentDate();
        this.numberOfDelayedDays = bill.getNumberOfDelayedDays();
        this.totalAmount = bill.getCorrectedAmount();
    }

    public DisplayBillVO() {

    }

}
