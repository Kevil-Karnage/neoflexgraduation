package neoflex.graduation.AccountService.controller;

import neoflex.graduation.AccountService.dto.response.ExceptionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public ExceptionResponse healthCheck() {
        return new ExceptionResponse(200, "AccountService is working...");
    }
}
