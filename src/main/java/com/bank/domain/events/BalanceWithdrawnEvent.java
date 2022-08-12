package com.bank.domain.events;

import com.bank.domain.aggregates.BankAccountAggregate;
import com.bank.es.BaseEvent;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
public class BalanceWithdrawnEvent extends BaseEvent {

  public static final String BALANCE_WITHDRAWN = "BALANCE_WITHDRAWN";
  public static final String AGGREGATE_TYPE = BankAccountAggregate.AGGREGATE_TYPE;


  private BigDecimal amount;

  @Builder
  public BalanceWithdrawnEvent(String aggregateId, BigDecimal amount) {
    super(aggregateId);
    this.amount = amount;
  }
}
