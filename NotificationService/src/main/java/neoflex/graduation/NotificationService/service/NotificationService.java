package neoflex.graduation.NotificationService.service;

import neoflex.graduation.NotificationService.exception.SMTPMessageNotSentException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Setter
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final MailSender mailSender;
    private final SimpleMailMessage templateMessage;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.send-to}")
    private String emailSendTo;
    private final String INCREASE_ACCOUNT_BALANCE = "Уведомление о новой транзакции";

    public void sendNotify(String message) {
        log.info("|---| Sending notification to email = {}: {}", emailSendTo, message);

        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setFrom(username);
        msg.setTo(emailSendTo);
        msg.setSubject(INCREASE_ACCOUNT_BALANCE);
        msg.setText(message);

        try {
            this.mailSender.send(msg);
            log.info("|---| Sending notification to email = {}: Success", emailSendTo);
        }
        catch (MailException ex) {
            throw new SMTPMessageNotSentException();
        }
    }
}
