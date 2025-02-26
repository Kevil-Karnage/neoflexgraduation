package neoflex.graduation.AccountService.controller;

import lombok.RequiredArgsConstructor;
import neoflex.graduation.AccountService.dto.request.AccountAddBalanceRq;
import neoflex.graduation.AccountService.dto.response.AccountAddBalanceRs;
import neoflex.graduation.AccountService.dto.response.AccountGetBalanceRs;
import neoflex.graduation.AccountService.service.AccountService;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/account", produces = APPLICATION_JSON_VALUE)
public class AccountController {
    private final AccountService service;

    @GetMapping("/{id}/balance")
    public AccountGetBalanceRs getAccountBalance(@PathVariable Long id) {
        return service.getAccountBalance(id);
    }

    @PostMapping("/{id}/balance")
    public AccountAddBalanceRs addBalanceToAccount(@RequestBody AccountAddBalanceRq rq,
                                                   @PathVariable("id") long id) {
        return service.addBalanceToAccount(id, rq);
    }
}
