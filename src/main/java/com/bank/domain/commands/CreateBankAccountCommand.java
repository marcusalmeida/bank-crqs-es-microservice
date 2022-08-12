package com.bank.domain.commands;

public record CreateBankAccountCommand(String aggregateID, String email, String userName, String address) {
}
