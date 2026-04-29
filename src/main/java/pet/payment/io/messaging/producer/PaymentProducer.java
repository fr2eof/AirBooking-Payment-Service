package pet.payment.io.messaging.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pet.payment.core.entity.PaymentEntity;
import pet.payment.io.dto.event.PaymentCompletedEvent;
import pet.payment.io.dto.event.PaymentFailedEvent;

@Service
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendCompleted(PaymentEntity payment) {
        try {
            PaymentCompletedEvent event = new PaymentCompletedEvent(
                    payment.getId(),
                    payment.getBookingId()
            );

            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    "payment.completed",
                    String.valueOf(payment.getBookingId()),
                    payload
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to send payment.completed", e);
        }
    }

    public void sendFailed(PaymentEntity payment, String reason) {
        try {
            PaymentFailedEvent event = new PaymentFailedEvent(
                    payment.getId(),
                    payment.getBookingId(),
                    reason
            );

            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    "payment.failed",
                    String.valueOf(payment.getBookingId()),
                    payload
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to send payment.failed", e);
        }
    }
}
