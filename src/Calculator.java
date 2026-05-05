public interface Calculator {
    long add(long a, long b);
    long subtract(long a, long b);
    long multiply(long a, long b);
    long divide(long a, long b);
    long mod(long a, long b);

    long and(long a, long b);
    long or(long a, long b);
    long xor(long a, long b);
    long not(long a);

    long shiftLeft(long a, int shift);
    long shiftRight(long a, int shift);
}