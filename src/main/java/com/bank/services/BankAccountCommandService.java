package com.bank.services;

import com.bank.domain.commands.ChangeEmailCommand;
import com.bank.domain.commands.CreateBankAccountCommand;
import com.bank.domain.commands.DepositAmountCommand;
import com.bank.domain.commands.WithdrawalAmountCommand;

public interface BankAccountCommandService {
    String handle(CreateBankAccountCommand cmd);

    void handle(ChangeEmailCommand cmd);

    void handle(DepositAmountCommand cmd);

    void handle(WithdrawalAmountCommand cmd);
}
