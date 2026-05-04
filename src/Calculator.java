public class Calculator {

    // Арифметика
    public static long add(long a, long b) { return a + b; }
    public static long subtract(long a, long b) { return a - b; }
    public static long multiply(long a, long b) { return a * b; }

    public static long divide(long a, long b) {
        if (b == 0) throw new ArithmeticException("Деление на ноль");
        return a / b;
    }

    public static long mod(long a, long b) {
        if (b == 0) throw new ArithmeticException("Деление на ноль (остаток)");
        return a % b;
    }

    // Побитовые операции
    public static long and(long a, long b) { return a & b; }
    public static long or(long a, long b)  { return a | b; }
    public static long xor(long a, long b) { return a ^ b; }
    public static long not(long a)         { return ~a; }

    // Сдвиги (логарифмический сдвиг для беззнаковой работы в контексте битов)
    public static long shl(long a, int shift) {
        if (shift < 0 || shift >= 64) throw new IllegalArgumentException("Сдвиг должен быть от 0 до 63");
        return a << shift;
    }

    public static long shr(long a, int shift) {
        if (shift < 0 || shift >= 64) throw new IllegalArgumentException("Сдвиг должен быть от 0 до 63");
        return a >> shift; // Арифметический сдвиг (сохраняет знак). Для логического используйте >>>
    }
}