package pet.payment.io.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pet.payment.core.service.PaymentService;
import pet.payment.io.dto.event.BookingCreatedEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCreatedConsumer {

    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;

    @KafkaListener(
            topics = "booking.created",
            groupId = "payment-service"
    )
    public void handle(String message) {

        try {
            BookingCreatedEvent event =
                    objectMapper.readValue(message, BookingCreatedEvent.class);

            paymentService.handleBookingCreated(event);

        } catch (Exception ex) {
            log.error("Failed to process booking.created event: {}", message, ex);

        }
    }
}
