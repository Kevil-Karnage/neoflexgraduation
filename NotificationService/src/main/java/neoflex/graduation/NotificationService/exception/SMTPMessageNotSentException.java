package neoflex.graduation.NotificationService.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SMTPMessageNotSentException extends RuntimeException {
    public SMTPMessageNotSentException() {
        super("Message not sent to email");
        log.error("|---| Failed |---|");
        log.error("Caused by {}", this.getMessage());
    }
}
