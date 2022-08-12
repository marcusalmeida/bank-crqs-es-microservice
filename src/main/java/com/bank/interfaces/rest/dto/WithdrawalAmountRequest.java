package com.bank.interfaces.rest.dto;

import java.math.BigDecimal;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public record WithdrawalAmountRequest(@Max(value = 2000, message = "maximal amount is 2000") @NotNull BigDecimal amount) {
}
