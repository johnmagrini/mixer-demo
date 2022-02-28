package com.gemini.mixerdemo;

import com.gemini.mixerdemo.domain.Customer;
import com.gemini.mixerdemo.domain.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
@Slf4j
public class TransactionGatewayService {

    public static String HOUSE_ACCOUNT = "HOUSE_ACCOUNT";

    private Hashtable<String, Customer> depositAddresses;
    private CustomerService customerService;
    private MixingService mixingService;


    //filter all transactions for ones addresses we "own"
    public void onTransaction(Transaction transaction) {
        if (depositAddresses.containsKey(transaction.getDestinationAddress())) {
            Customer customer = depositAddresses.get(transaction.getDestinationAddress());
            customerService.receiveCoins(transaction, customer);
            sendtoHouseAccount(transaction);
        }
    }

    private void sendtoHouseAccount(Transaction transaction) {
        sendTransaction(Transaction.builder()
                .sourceAddress(transaction.getDestinationAddress())
                .destinationAddress(HOUSE_ACCOUNT)
                .amount(transaction.getAmount())
                .build());
    }

    //Keep track of new customer creations
    public void onNewCustomer(Customer customer) {
        depositAddresses.put(customer.getInboundAddress(), customer);
    }

    //send outgoing transactions to ledger
    public void sendTransaction(Transaction t) {
        log.info("Sending Transaction: {}", t);
    }
}
