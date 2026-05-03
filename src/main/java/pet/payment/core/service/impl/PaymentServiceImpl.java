package pet.payment.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.payment.core.entity.PaymentEntity;
import pet.payment.core.repository.PaymentJpaRepository;
import pet.payment.core.service.PaymentService;
import pet.payment.io.dto.event.BookingCreatedEvent;
import pet.payment.io.messaging.producer.PaymentProducer;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentJpaRepository paymentRepository;
    private final PaymentProducer paymentProducer;

    @Override
    @Transactional
    public void handleBookingCreated(BookingCreatedEvent event) {

        log.info("Processing booking.created bookingId={}", event.getBookingId());

        if (paymentRepository.existsByBookingId(event.getBookingId())) {
            log.info("Payment already exists for bookingId={}", event.getBookingId());
            return;
        }

        PaymentEntity payment = new PaymentEntity(
                event.getUserId(),
                event.getBookingId(),
                event.getAmount()
        );

        paymentRepository.save(payment);

        // (заглушка платежного шлюза)
        boolean success = processPayment(event);

        if (success) {
            payment.complete();
            paymentProducer.sendCompleted(payment);

            log.info("Payment COMPLETED bookingId={}", event.getBookingId());
        } else {
            payment.fail();
            paymentProducer.sendFailed(payment, "PAYMENT_DECLINED");

            log.warn("Payment FAILED bookingId={}", event.getBookingId());
        }
    }

    private boolean processPayment(BookingCreatedEvent event) {
        return Math.random() > 0.4;
    }

}