package com.bank.services;

import com.bank.domain.aggregates.BankAccountAggregate;
import com.bank.domain.commands.ChangeEmailCommand;
import com.bank.domain.commands.CreateBankAccountCommand;
import com.bank.domain.commands.DepositAmountCommand;
import com.bank.domain.commands.WithdrawalAmountCommand;
import com.bank.es.EventStoreDB;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankAccountCommandServiceImpl implements BankAccountCommandService {

    private final EventStoreDB eventStoreDB;
    private static final String SERVICE_NAME = "microservice";

    @Override
    @NewSpan
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public String handle(@SpanTag("command") CreateBankAccountCommand command) {
        final var aggregate = new BankAccountAggregate(command.aggregateID());
        aggregate.createBankAccount(command.email(), command.address(), command.userName());
        eventStoreDB.save(aggregate);

        log.info("(CreateBankAccountCommand) aggregate: {}", aggregate);
        return aggregate.getId();
    }

    @Override
    @NewSpan
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public void handle(@SpanTag("command") ChangeEmailCommand cmd) {
        final var aggregate = eventStoreDB.load(cmd.aggregateID(), BankAccountAggregate.class);
        aggregate.changeEmail(cmd.newEmail());
        eventStoreDB.save(aggregate);
        log.info("(ChangeEmailCommand) aggregate: {}", aggregate);
    }

    @Override
    @NewSpan
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public void handle(@SpanTag("command") DepositAmountCommand cmd) {
        final var aggregate = eventStoreDB.load(cmd.aggregateID(), BankAccountAggregate.class);
        aggregate.depositBalance(cmd.amount());
        eventStoreDB.save(aggregate);
        log.info("(DepositAmountCommand) aggregate : {}", aggregate);
    }

    @Override
    @NewSpan
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME)
    public void handle(@SpanTag("command") WithdrawalAmountCommand cmd) {
        final var aggregate = eventStoreDB.load(cmd.aggregateID(), BankAccountAggregate.class);
        aggregate.withdrawalBalance(cmd.amount());
        eventStoreDB.save(aggregate);
        log.info("(DepositAmountCommand) aggregate : {}", aggregate);
    }
}
