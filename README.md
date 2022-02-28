# Coin Mixing Example
###John Magrini,  Feb 27 2022

## Components / Methods
&nbsp;<br>
### Customer Service
    public String createCustomer(List<String> withdrawalAddresses)
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**Input:** List of Withdrawal Addresses <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**Output:** Single deposit Address <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**TODO:** Creation of wallet addresses, Persistence of addresses, perhaps naming convention to be able to recognize without persistence <br>
&nbsp;<br>
### Transaction Gateway Service
    public void onTransaction(Transaction transaction)
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**Input:** "Confirmed" Transaction from ledger <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**TODO:** Integrate with actual event service <br>

    public void onNewCustomer(Customer customer)
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**Input:** Any new customer that's been created that we should listen to for deposits <br>

    public void sendTransaction(Transaction transaction)
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**Input:** Transaction that's to be sent to ledger

&nbsp;<br>

### Address Service
    public String getNewDepositAddress()
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**Output:** Single deposit Address <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**TODO:** Something other than random<br> 
&nbsp;<br>

### Mixing Service
Main loop of the service
TODO Finish commission
TODO break out "otherCustomers" to a service call that only returns a handful of randomly selected
### Reference Documentation
