package neoflex.graduation.TransactionService.controller;

import lombok.RequiredArgsConstructor;
import neoflex.graduation.TransactionService.dto.request.TransactionGetRq;
import neoflex.graduation.TransactionService.dto.request.TransactionNewRq;
import neoflex.graduation.TransactionService.dto.response.TransactionNewRs;
import neoflex.graduation.TransactionService.dto.response.TransactionRs;
import neoflex.graduation.TransactionService.service.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/transactions", produces = APPLICATION_JSON_VALUE)
public class TransactionController {
    private final TransactionService service;

    @PostMapping("/new")
    public TransactionNewRs createNew(@RequestBody TransactionNewRq rq) {
        return service.createNewTransaction(rq);
    }

    @PostMapping("/get")
    public TransactionRs getById(@RequestBody TransactionGetRq rq) {
        return service.getTransactionById(rq);
    }
}
