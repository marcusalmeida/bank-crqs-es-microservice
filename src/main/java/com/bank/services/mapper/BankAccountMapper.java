package com.bank.services.mapper;

import com.bank.domain.aggregates.BankAccountAggregate;
import com.bank.interfaces.rest.dto.BankAccountResponse;
import com.bank.projection.documents.BankAccountDocument;

public final class BankAccountMapper {

  private BankAccountMapper(){}

  public static BankAccountResponse bankAccountResponseFromAggregate(
      BankAccountAggregate bankAccountAggregate) {
    return new BankAccountResponse(
        bankAccountAggregate.getId(),
        bankAccountAggregate.getEmail(),
        bankAccountAggregate.getAddress(),
        bankAccountAggregate.getUserName(),
        bankAccountAggregate.getBalance()
    );
  }

  public static BankAccountResponse bankAccountResponseFromDocument(BankAccountDocument bankAccountDocument) {
    return new BankAccountResponse(
        bankAccountDocument.getAggregateId(),
        bankAccountDocument.getEmail(),
        bankAccountDocument.getAddress(),
        bankAccountDocument.getUserName(),
        bankAccountDocument.getBalance()
    );
  }

  public static BankAccountDocument bankAccountDocumentFromAggregate(BankAccountAggregate bankAccountAggregate) {
    return BankAccountDocument.builder()
        .aggregateId(bankAccountAggregate.getId())
        .email(bankAccountAggregate.getEmail())
        .address(bankAccountAggregate.getAddress())
        .userName(bankAccountAggregate.getUserName())
        .balance(bankAccountAggregate.getBalance())
        .build();
  }

}
