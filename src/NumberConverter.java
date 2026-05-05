public interface NumberConverter {
    long parse(String input, int base);
    String format(long value, int base);

    default boolean supportsBase(int base) {
        return base == 2 || base == 8 || base == 10 || base == 16;
    }

    default long safeParse(String input, int base) {
        if (!supportsBase(base)) {
            throw new IllegalArgumentException("Неподдерживаемая система: " + base);
        }
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Пустой ввод");
        }
        return parse(input.trim().toUpperCase(), base);
    }
}