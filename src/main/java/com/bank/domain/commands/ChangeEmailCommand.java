package com.bank.domain.commands;

public record ChangeEmailCommand(String aggregateID, String newEmail) {
}
