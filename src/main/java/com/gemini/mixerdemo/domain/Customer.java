package com.gemini.mixerdemo.domain;

import jdk.jfr.DataAmount;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class Customer {

    private String inboundAddress;  //key
    private List<String> outboundAddresses;
    private BigDecimal receivedAmount;
    private BigDecimal sentAmountGross;
    private BigDecimal sentAmountNet;
    private BigDecimal commissionRate;

    public BigDecimal getPendingAmount() {
        return receivedAmount.subtract(sentAmountGross);
    }
}
