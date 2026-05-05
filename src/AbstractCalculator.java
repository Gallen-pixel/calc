public abstract class AbstractCalculator implements Calculator {
    protected final void validateShift(int shift) {
        if (shift < 0 || shift >= 64) {
            throw new IllegalArgumentException("Сдвиг должен быть в диапазоне [0, 63]");
        }
    }
}