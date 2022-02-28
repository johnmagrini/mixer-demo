package com.gemini.mixerdemo.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Transaction {

    private String sourceAddress;
    private String destinationAddress;
    private BigDecimal amount;
}
