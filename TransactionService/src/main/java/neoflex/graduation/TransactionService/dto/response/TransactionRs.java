package neoflex.graduation.TransactionService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRs {
    private UUID transactionId;
    private Date timestamp;
    private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;
}
