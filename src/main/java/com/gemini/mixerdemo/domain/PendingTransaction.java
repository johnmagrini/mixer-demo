package com.gemini.mixerdemo.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PendingTransaction {
    private BigDecimal sentAmount;
    private Transaction transaction;
    public BigDecimal getPendingAmount() {
        return transaction.getAmount().subtract(sentAmount);
    }
}
