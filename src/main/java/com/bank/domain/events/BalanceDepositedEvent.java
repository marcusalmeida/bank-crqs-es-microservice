package com.bank.domain.events;

import com.bank.domain.aggregates.BankAccountAggregate;
import com.bank.es.BaseEvent;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
public class BalanceDepositedEvent extends BaseEvent {
  public static final String BALANCE_DEPOSITED = "BALANCE_DEPOSITED_V1";
  public static final String AGGREGATE_TYPE = BankAccountAggregate.AGGREGATE_TYPE;

  private BigDecimal amount;

  @Builder
  public BalanceDepositedEvent(String aggregateId, BigDecimal amount) {
    super(aggregateId);
    this.amount = amount;
  }
}
