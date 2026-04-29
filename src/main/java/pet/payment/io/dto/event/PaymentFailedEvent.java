package pet.payment.io.dto.event;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PaymentFailedEvent {
    private Long id;
    private Long bookingId;
    private String reason;

}
