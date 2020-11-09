package com.deliver.finances.model.entity;

import static java.time.temporal.ChronoUnit.DAYS;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "BILL")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BILL_ID")
    private Long id;

    @NotBlank
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Column(name = "ORIGINAL_AMOUNT")
    private BigDecimal originalAmount;

    @NotNull
    @Column(name = "CORRECTED_AMOUNT")
    private BigDecimal correctedAmount;

    @NotNull
    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @NotNull
    @Column(name = "PAYMENT_DATE")
    private LocalDate paymentDate;

    @JsonIgnore
    public boolean isDelayed() {
        return this.paymentDate.isAfter(dueDate);
    }

    @JsonIgnore
    public Integer getNumberOfDelayedDays() {
        if (this.isDelayed()) {
            return (int) DAYS.between(dueDate, paymentDate);
        }

        return 0;
    }

}
