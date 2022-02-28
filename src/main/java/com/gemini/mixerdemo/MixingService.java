package com.gemini.mixerdemo;

import com.gemini.mixerdemo.domain.Customer;
import com.gemini.mixerdemo.domain.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class MixingService {

    private static int MIN_CUSTOMERS_FOR_MIXING = 3;
    private static int RANDOM_FACTOR = 10;

    private TransactionGatewayService transactionGatewayService;
    private LinkedList<Customer> workQueue;

    public void addPending(Customer customer) {
        workQueue.add(customer);
    }



    //repeat
    public void mixCoins() {

        while(true) {
            if (workQueue.size() > MIN_CUSTOMERS_FOR_MIXING) {
                Customer currentCustomer = workQueue.pop();
                Customer otherCustomer = getRandomOtherCustomer();

                BigDecimal randomAmount = getRandomAmountToSend(currentCustomer, otherCustomer);

                //deduct to from other customer transaction and send to random current customer address
                String randomDestinationAddress = getRandomAddress(currentCustomer.getOutboundAddresses());

                //TODO calc and deduct customer commission

                //build transaction
                Transaction mixedTransaction = Transaction.builder()
                        .amount(randomAmount)
                        .sourceAddress(TransactionGatewayService.HOUSE_ACCOUNT)
                        .destinationAddress(randomDestinationAddress)
                        .build();

                //customer level accounting
                currentCustomer.setSentAmountGross(currentCustomer.getSentAmountGross().add(randomAmount));
                currentCustomer.setSentAmountNet(currentCustomer.getSentAmountNet().add(randomAmount)); //TODO update w/ net of commission value

                //if the current customer has more mixing to do.
                if (currentCustomer.getPendingAmount().compareTo(BigDecimal.ZERO) > 0){
                    workQueue.add(currentCustomer);
                }

                //send to customer destination
                transactionGatewayService.sendTransaction(mixedTransaction);

                //TODO send commission to house commission pnl;
            } else {
                try {Thread.sleep(100);}catch(InterruptedException e){ e.printStackTrace();}
            }
        }

    }

    private Customer getRandomOtherCustomer() {
        return workQueue.get((int) Math.random() * workQueue.size());
    }

    private String getRandomAddress(List<String> addressList) {
        return addressList.get((int) Math.random() * addressList.size());
    }

    private BigDecimal getRandomAmountToSend(Customer currentCustomer, Customer otherCustomer) {
        BigDecimal pendingAmount = currentCustomer.getPendingAmount();
        BigDecimal availableAmount = otherCustomer.getPendingAmount();

        BigDecimal partialAvailableAmount = availableAmount.divide(BigDecimal.valueOf(RANDOM_FACTOR));

        //if partial available amount > pending amount,  random w/ max of pending amount
        if (partialAvailableAmount.compareTo(pendingAmount) >= 0) {
            return BigDecimal.valueOf(Math.random()).multiply(pendingAmount);
            //else if pending amount > available amount, random w/ max of partial available amount
        } else {
            return BigDecimal.valueOf(Math.random()).multiply(partialAvailableAmount);
        }
    }
}
