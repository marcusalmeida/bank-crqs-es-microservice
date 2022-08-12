package com.bank.configurations;

import com.bank.projection.documents.BankAccountDocument;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MongoConfiguration {

  private final MongoTemplate mongoTemplate;

  @PostConstruct
  public void mongoInit() {
    final var bankAccounts = mongoTemplate.getCollection("bankAccounts");
    final var aggregateIdIndex = mongoTemplate.indexOps(BankAccountDocument.class).ensureIndex(new Index("aggregateId",
        Direction.ASC).unique());
    final var indexInfo = mongoTemplate.indexOps(BankAccountDocument.class).getIndexInfo();
  }
}
