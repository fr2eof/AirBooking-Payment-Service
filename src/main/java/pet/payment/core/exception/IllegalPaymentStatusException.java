package pet.payment.core.exception;

public class IllegalPaymentStatusException extends RuntimeException {
    public IllegalPaymentStatusException(String message) {
        super(message);
    }
}
