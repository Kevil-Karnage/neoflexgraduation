package neoflex.graduation.AccountService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountAddBalanceRq {
    private BigDecimal amount;
}
