package com.bank.domain.aggregates;

import com.bank.domain.events.BalanceDepositedEvent;
import com.bank.domain.events.BalanceWithdrawnEvent;
import com.bank.domain.events.BankAccountCreatedEvent;
import com.bank.domain.events.EmailChangedEvent;
import com.bank.domain.exceptions.InvalidEmailException;
import com.bank.es.AggregateRoot;
import com.bank.es.Event;
import com.bank.es.SerializerUtils;
import com.bank.es.exceptions.InvalidEventTypeException;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BankAccountAggregate extends AggregateRoot {

    public static final String AGGREGATE_TYPE = "BankAccountAggregate";

    private String email;
    private String userName;
    private String address;
    private BigDecimal balance;

    public BankAccountAggregate(String id) {
        super(id, AGGREGATE_TYPE);
    }

    @Override
    public void when(Event event) {
      switch(event.getEventType()) {
        case BankAccountCreatedEvent.BANK_ACCOUNT_CREATED_V1 ->
            handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), BankAccountCreatedEvent.class));
        case EmailChangedEvent.EMAIL_CHANGED_V1 ->
            handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), EmailChangedEvent.class));
        case BalanceDepositedEvent.BALANCE_DEPOSITED ->
            handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), BalanceDepositedEvent.class));
        case BalanceWithdrawnEvent.BALANCE_WITHDRAWN ->
            handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), BalanceWithdrawnEvent.class));

        default -> throw new InvalidEventTypeException(event.getEventType());
      }
    }

    public void createBankAccount(String email, String address, String userName) {
        final var data =
                BankAccountCreatedEvent.builder()
                        .aggregateId(id)
                        .email(email)
                        .address(address)
                        .userName(userName)
                        .build();

        final byte[] dataBytes = SerializerUtils.serializeToJsonBytes(data);
        final var event =
                this.createEvent(BankAccountCreatedEvent.BANK_ACCOUNT_CREATED_V1, dataBytes, null);
        this.apply(event);
    }

  private void handle(final BankAccountCreatedEvent event) {
      this.email = event.getEmail();
      this.userName = event.getUserName();
      this.address = event.getAddress();
      this.balance = BigDecimal.valueOf(0);
  }

  public void changeEmail(String email) {
      final var data = EmailChangedEvent.builder().aggregateId(id).newEmail(email).build();
      final byte[] dataBytes = SerializerUtils.serializeToJsonBytes(data);
      final var event = this.createEvent(EmailChangedEvent.EMAIL_CHANGED_V1, dataBytes, null);
      apply(event);
  }

  private void handle(final EmailChangedEvent event) {
      Objects.requireNonNull(event.getNewEmail());
      if (event.getNewEmail().isBlank()) throw new InvalidEmailException();
      this.email = event.getNewEmail();
  }

  public void depositBalance(BigDecimal amount) {
      final var data = BalanceDepositedEvent.builder().aggregateId(id).amount(amount).build();
      final byte[] dataBytes = SerializerUtils.serializeToJsonBytes(data);
      final var event = this.createEvent(BalanceDepositedEvent.BALANCE_DEPOSITED, dataBytes, null);
      apply(event);
  }

  private void handle(final BalanceDepositedEvent event) {
      Objects.requireNonNull(event.getAmount());
      this.balance = this.balance.add(event.getAmount());
  }


  public void withdrawalBalance(BigDecimal amount) {
    final var data = BalanceWithdrawnEvent.builder().aggregateId(id).amount(amount).build();
    final byte[] dataBytes = SerializerUtils.serializeToJsonBytes(data);
    final var event = this.createEvent(BalanceWithdrawnEvent.BALANCE_WITHDRAWN, dataBytes, null);
    apply(event);
  }

  private void handle(final BalanceWithdrawnEvent event) {
    Objects.requireNonNull(event.getAmount());
    this.balance = this.balance.subtract(event.getAmount());
  }

  @Override
  public String toString() {
      return "BankAccountAggregate{" +
          "email='" + email + '\'' +
          ", userName='" + userName + '\'' +
          ", address='" + address + '\'' +
          ", balance=" + balance +
          ", id='" + id + '\'' +
          ", type='" + type + '\'' +
          ", version=" + version +
          ", changes=" + changes.size() +
          '}';
  }

}
