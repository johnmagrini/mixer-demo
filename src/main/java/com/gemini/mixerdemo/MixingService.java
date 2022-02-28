package com.gemini.mixerdemo;

import com.gemini.mixerdemo.domain.Customer;
import com.gemini.mixerdemo.domain.PendingTransaction;
import com.gemini.mixerdemo.domain.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MixingService {

    private static int MIN_CUSTOMERS_FOR_MIXING = 3;
    private static int RANDOM_FACTOR = 10;

    private HashMap<String, Customer> pendingCustomers;
    private TransactionGatewayService transactionGatewayService;

    public void addPending(Customer customer) {
        pendingCustomers.put(customer.getInboundAddress(), customer);
    }



    //repeat
    public void mixCoins() {

        if (pendingCustomers.size() > MIN_CUSTOMERS_FOR_MIXING) {

            for (Customer currentCustomer : pendingCustomers.values()) {

                //create a random list of the other customers
                List<Customer> otherCustomers = pendingCustomers.values().stream().filter(oc -> !oc.getInboundAddress().equals(currentCustomer.getInboundAddress())).collect(Collectors.toList());

                for (PendingTransaction currentTransaction : currentCustomer.getPendingTransactions()) {

                    //randomize them
                    Collections.shuffle(otherCustomers);

                    //deduct a random amount from each other customer
                    for (Customer otherCustomer : otherCustomers) {

                        //from each other customer pending transaction
                        for (PendingTransaction otherCustomerTransaction : otherCustomer.getPendingTransactions()) {

                            //calc random amount to mix
                            BigDecimal randomAmount = getRandomAmountToSend(currentTransaction, otherCustomerTransaction);

                            //deduct to from other customer transaction and send to random current customer address
                            String randomDestinationAddress = getRandomAddress(currentCustomer.getOutboundAddresses());

                            //TODO calc and deduct customer commission

                            //build transaction
                            Transaction mixedTransaction = Transaction.builder()
                                    .amount(randomAmount)
                                    .sourceAddress(otherCustomerTransaction.getTransaction().getDestinationAddress())
                                    .destinationAddress(randomDestinationAddress)
                                    .build();

                            //customer level accounting
                            currentCustomer.setSentAmountGross(currentCustomer.getSentAmountGross().add(randomAmount));
                            currentCustomer.setSentAmountNet(currentCustomer.getSentAmountNet().add(randomAmount)); //TODO update w/ net of commission value

                            //pending transaction level accounting
                            currentTransaction.setSentAmount(currentTransaction.getSentAmount().add(randomAmount));

                            //send to customer destination
                            transactionGatewayService.sendTransaction(mixedTransaction);

                            //TODO send commission to house commission pnl;

                        } //end other customer pending transactions

                    } // end other customers
                }//end current customer pending transactions

                //TODO move completed transactions to archive
            } // end current customer

        } else {
            log.info("Not enough customers to mix, waiting...");
        }
    }

    private String getRandomAddress(List<String> addressList) {
        return addressList.get((int) Math.random() * addressList.size());
    }

    private BigDecimal getRandomAmountToSend(PendingTransaction currentTransaction, PendingTransaction otherCustomerTransaction) {

        BigDecimal pendingAmount = currentTransaction.getPendingAmount();
        BigDecimal availableAmount = otherCustomerTransaction.getPendingAmount();

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
