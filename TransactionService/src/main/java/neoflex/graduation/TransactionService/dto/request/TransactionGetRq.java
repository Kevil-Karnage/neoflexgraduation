package neoflex.graduation.TransactionService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TransactionGetRq {
    private UUID transactionId;
}
