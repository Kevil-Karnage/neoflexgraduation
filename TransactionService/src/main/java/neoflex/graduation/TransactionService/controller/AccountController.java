package neoflex.graduation.TransactionService.controller;

import lombok.RequiredArgsConstructor;
import neoflex.graduation.TransactionService.dto.response.TransactionHistoryRs;
import neoflex.graduation.TransactionService.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/account", produces = APPLICATION_JSON_VALUE)
public class AccountController {
    private final TransactionService transactionService;

    @GetMapping("/history/{id}")
    public TransactionHistoryRs getAccountTransactions(@PathVariable("id") long accountId) {
        return transactionService.getAccountTransactions(accountId);
    }
}
