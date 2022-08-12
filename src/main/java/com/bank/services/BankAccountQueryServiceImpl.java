package com.bank.services;

import com.bank.domain.aggregates.BankAccountAggregate;
import com.bank.domain.queries.FindAllOrderByBalance;
import com.bank.domain.queries.GetBankAccountByIdQuery;
import com.bank.domain.repositories.BankAccountMongoRepository;
import com.bank.es.EventStoreDB;
import com.bank.interfaces.rest.dto.BankAccountResponse;
import com.bank.projection.documents.BankAccountDocument;
import com.bank.services.mapper.BankAccountMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountQueryServiceImpl implements BankAccountQueryService{

  private final EventStoreDB eventStoreDB;
  private final BankAccountMongoRepository mongoRepository;
  private static final String SERVICE_NAME = "microservice";
  @Override
  @NewSpan
  @Retry(name = SERVICE_NAME)
  @CircuitBreaker(name = SERVICE_NAME)
  public BankAccountResponse handle(@SpanTag("query") GetBankAccountByIdQuery query) {
    Optional<BankAccountDocument> optionalDocument = mongoRepository.findByAggregateId(query.aggregateId());
    if (optionalDocument.isPresent()) {
      return BankAccountMapper.bankAccountResponseFromDocument(optionalDocument.get());
    }

    final var aggregate = eventStoreDB.load(query.aggregateId(), BankAccountAggregate.class);
    final var savedDocument = mongoRepository.save(BankAccountMapper.bankAccountDocumentFromAggregate(aggregate));
    log.info("(GetBankAccountByIdQuery) savedDocument: {}", savedDocument);

    final var bankAccounResponse = BankAccountMapper.bankAccountResponseFromAggregate(aggregate);
    log.info("(GetBankAccounByIdQuery) response: {}", bankAccounResponse);
    return bankAccounResponse;
  }

  @Override
  @NewSpan
  @Retry(name = SERVICE_NAME)
  @CircuitBreaker(name = SERVICE_NAME)
  public Page<BankAccountResponse> handle(@SpanTag("query") FindAllOrderByBalance query) {
    return mongoRepository.findAll(PageRequest.of(query.page(), query.size(), Sort.by("balance")))
        .map(BankAccountMapper::bankAccountResponseFromDocument);
  }
}
