package pet.payment.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pet.payment.core.entity.PaymentEntity;


public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}
