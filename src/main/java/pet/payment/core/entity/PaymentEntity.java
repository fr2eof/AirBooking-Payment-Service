package pet.payment.core.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pet.payment.core.exception.IllegalPaymentStatusException;
import pet.payment.core.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long bookingId;
    private Long userId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Version
    private long version;

    public PaymentEntity(Long userId, Long bookingId, BigDecimal amount) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public void complete() {
        if (getStatus() != PaymentStatus.PENDING) {
            throw new IllegalPaymentStatusException("Cannot complete payment");
        } else {
            setStatus(PaymentStatus.COMPLETED);
        }
    }

    public void fail() {
        if (getStatus() != PaymentStatus.PENDING) {
            throw new IllegalPaymentStatusException("Cannot fail payment");
        } else {
            setStatus(PaymentStatus.FAILED);
        }
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
