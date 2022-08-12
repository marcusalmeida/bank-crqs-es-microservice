package com.bank.interfaces.rest;

import com.bank.domain.commands.ChangeEmailCommand;
import com.bank.domain.commands.CreateBankAccountCommand;
import com.bank.domain.commands.DepositAmountCommand;
import com.bank.domain.commands.WithdrawalAmountCommand;
import com.bank.domain.queries.FindAllOrderByBalance;
import com.bank.domain.queries.GetBankAccountByIdQuery;
import com.bank.interfaces.rest.dto.BankAccountResponse;
import com.bank.interfaces.rest.dto.ChangeEmailRequest;
import com.bank.interfaces.rest.dto.CreateBankAccountRequest;
import com.bank.interfaces.rest.dto.DepositAmountRequest;
import com.bank.interfaces.rest.dto.WithdrawalAmountRequest;
import com.bank.services.BankAccountCommandService;
import com.bank.services.BankAccountQueryService;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bank")
@Slf4j
@RequiredArgsConstructor
public class BankAccounController {

    private final BankAccountCommandService commandService;
    private final BankAccountQueryService queryService;

    @GetMapping("/{aggregateId}")
    public ResponseEntity<BankAccountResponse> getBankAccount(@PathVariable String aggregateId) {
      final var result = queryService.handle(new GetBankAccountByIdQuery(aggregateId));
      log.info("Get bank account result: {}", result);
      return ResponseEntity.ok(result);
    }

    @GetMapping("/balances")
    public ResponseEntity<Page<BankAccountResponse>> getAllOrderByBalance(
        @RequestParam(name="page", defaultValue = "0") Integer page,
        @RequestParam(name="size", defaultValue = "10") Integer size){
      final var result = queryService.handle(new FindAllOrderByBalance(page, size));
      log.info("Get all by balance page: {}, size: {}, result: {}", page, size, result);
      return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<String> createBankAccount(
            @Valid @RequestBody CreateBankAccountRequest req) {
        final var aggregateId = UUID.randomUUID().toString();
        final var id =
                commandService.handle(
                        new CreateBankAccountCommand(
                                aggregateId, req.email(), req.userName(), req.address()));
        log.info("Created bank account id: {}", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping(path="/email/{aggregateId}")
    public ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailRequest req, @PathVariable String aggregateId) {
      commandService.handle(new ChangeEmailCommand(aggregateId, req.newEmail()));
      return ResponseEntity.ok().build();
    }

    @PostMapping(path="/deposit/{aggregateId}")
    public ResponseEntity<Void> depositAmount(@Valid @RequestBody DepositAmountRequest req, @PathVariable String aggregateId) {
      commandService.handle(new DepositAmountCommand(aggregateId, req.amount()));
      return ResponseEntity.ok().build();
    }

    @PostMapping(path="/withdrawal/{aggregateId}")
  public ResponseEntity<Void> withdrawalAmount(@Valid @RequestBody WithdrawalAmountRequest req, @PathVariable String aggregateId) {
      commandService.handle(new WithdrawalAmountCommand(aggregateId, req.amount()));
      return ResponseEntity.ok().build();
    }


}
