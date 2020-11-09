package com.deliver.finances.model.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "INTEREST_CONFIGURATION")
public class InterestConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INTEREST_ID")
    private Long id;

    @Column(name = "START_RANGE")
    private Integer startRange;

    @Column(name = "END_RANGE")
    private Integer endRange;

    @Column(name = "FINE")
    private BigDecimal fine;

    @Column(name = "INTEREST")
    private BigDecimal interest;


}
