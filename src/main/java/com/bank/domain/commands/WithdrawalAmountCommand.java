package com.bank.domain.commands;

import java.math.BigDecimal;

public record WithdrawalAmountCommand(String aggregateID, BigDecimal amount) {

}
