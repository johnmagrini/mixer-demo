package com.gemini.mixerdemo;

import com.gemini.mixerdemo.domain.Customer;
import com.gemini.mixerdemo.domain.PendingTransaction;
import com.gemini.mixerdemo.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class CustomerService {

    private AddressService addressService;
    private MixingService mixingService;
    private List<Customer> customerList;

    //TODO getDepositAddress  (createCustomer)
    //Input - List of Withdrawal Addresses
    //Persist
    //Output - Single Deposit Address
    public Customer createCustomer(List<String> depositAddresses) {
        Customer customer = Customer.builder()
                .outboundAddresses(depositAddresses)
                .inboundAddress(addressService.getNewDepositAddress())
                .build();
        customerList.add(customer);
        return customer;
    }


    public void receiveCoins(Transaction transaction, Customer customer) {
        //move inbound transaction into waiting queue
        //credit customer
        customer.setReceivedAmount(customer.getReceivedAmount().add(transaction.getAmount()));
        customer.getPendingTransactions().add(
                PendingTransaction.builder()
                        .sentAmount(BigDecimal.ZERO)
                        .transaction(transaction)
                        .build());
        mixingService.addPending(customer);
    }
}
