package com.bank.domain.commands;

import java.math.BigDecimal;

public record DepositAmountCommand(String aggregateID, BigDecimal amount) {

}
