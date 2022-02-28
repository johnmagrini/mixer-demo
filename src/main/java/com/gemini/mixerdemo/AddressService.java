package com.gemini.mixerdemo;

import java.util.UUID;

public class AddressService {
    public String getNewDepositAddress() {
        return  UUID.randomUUID().toString();
    }
}
