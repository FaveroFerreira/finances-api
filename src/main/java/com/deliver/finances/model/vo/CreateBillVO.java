package com.deliver.finances.model.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBillVO {

    @NotBlank
    private String description;

    @NotNull
    private BigDecimal originalAmount;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private LocalDate paymentDate;

}
