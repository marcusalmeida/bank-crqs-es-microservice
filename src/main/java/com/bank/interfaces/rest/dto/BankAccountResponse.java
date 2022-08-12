package com.bank.interfaces.rest.dto;

import java.math.BigDecimal;

public record BankAccountResponse(
    String aggregateId,
    String email,
    String address,
    String userName,
    BigDecimal balance
) {

}
