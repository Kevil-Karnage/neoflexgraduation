package neoflex.graduation.NotificationService.kafka;

import neoflex.graduation.NotificationService.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaConsumer {
    private final NotificationService notificationService;

    @KafkaListener(topics = "transaction", groupId = "group1")
    public void listenTransactions(String message) {
        log.info("|---| Got message from Kafka : {}", message);
        notificationService.sendNotify(message);
    }
}


