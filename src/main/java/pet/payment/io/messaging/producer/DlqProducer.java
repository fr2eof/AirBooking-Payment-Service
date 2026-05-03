package pet.payment.io.messaging.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DlqProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendToDlq(String originalMessage, String reason) {
        kafkaTemplate.send(
                "booking.created.dlq",
                originalMessage
        );
    }
}
