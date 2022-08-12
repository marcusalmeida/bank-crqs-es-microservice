package com.bank.domain.repositories;

import com.bank.projection.documents.BankAccountDocument;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BankAccountMongoRepository extends MongoRepository<BankAccountDocument, String> {
  Optional<BankAccountDocument> findByAggregateId(String aggregateId);

  void deleteByAggregateId(String aggregateId);
}
