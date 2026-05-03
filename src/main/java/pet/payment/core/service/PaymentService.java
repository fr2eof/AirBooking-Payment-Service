package pet.payment.core.service;

import pet.payment.io.dto.event.BookingCreatedEvent;

public interface PaymentService {

    void handleBookingCreated(BookingCreatedEvent event);
}
