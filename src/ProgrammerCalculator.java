public class ProgrammerCalculator extends AbstractCalculator {
    @Override public long add(long a, long b) { return a + b; }
    @Override public long subtract(long a, long b) { return a - b; }
    @Override public long multiply(long a, long b) { return a * b; }

    @Override public long divide(long a, long b) {
        if (b == 0) throw new ArithmeticException("Деление на ноль");
        return a / b;
    }
    @Override public long mod(long a, long b) {
        if (b == 0) throw new ArithmeticException("Остаток от нуля");
        return a % b;
    }

    @Override public long and(long a, long b) { return a & b; }
    @Override public long or(long a, long b)  { return a | b; }
    @Override public long xor(long a, long b) { return a ^ b; }
    @Override public long not(long a)         { return ~a; }

    @Override public long shiftLeft(long a, int shift) {
        validateShift(shift);
        return a << shift;
    }
    @Override public long shiftRight(long a, int shift) {
        validateShift(shift);
        return a >> shift;
    }
}