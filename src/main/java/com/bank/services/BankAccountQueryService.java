package com.bank.services;

import com.bank.domain.queries.FindAllOrderByBalance;
import com.bank.domain.queries.GetBankAccountByIdQuery;
import com.bank.interfaces.rest.dto.BankAccountResponse;
import org.springframework.data.domain.Page;

public interface BankAccountQueryService {
  BankAccountResponse handle(GetBankAccountByIdQuery query);
  Page<BankAccountResponse> handle(FindAllOrderByBalance query);
}
