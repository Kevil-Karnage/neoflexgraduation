package neoflex.graduation.AccountService.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionNotSavedException extends RuntimeException {
    public TransactionNotSavedException() {
        super("Error saving the transaction");
        log.error("|---| Failed |---|");
        log.error("Caused by {}", this.getMessage());
    }
}
