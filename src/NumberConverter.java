public class NumberConverter {

    /**
     * Парсит строку в число с учётом указанного основания.
     * Автоматически обрезает префиксы 0x, 0b, 0o если они есть.
     */
    public static long parse(String input, int base) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Пустой ввод");
        }
        String clean = input.trim().toUpperCase();

        if (clean.startsWith("0X") && base == 16) clean = clean.substring(2);
        if (clean.startsWith("0B") && base == 2)  clean = clean.substring(2);
        if (clean.startsWith("0O") && base == 8)  clean = clean.substring(2);

        return Long.parseLong(clean, base);
    }

    /**
     * Форматирует long-значение в строку указанного основания.
     * Использует стандартное двухдополнительное представление для отрицательных чисел.
     */
    public static String format(long value, int base) {
        return switch (base) {
            case 2 -> "0b" + Long.toBinaryString(value);
            case 8 -> "0o" + Long.toOctalString(value);
            case 10 -> String.valueOf(value);
            case 16 -> "0x" + Long.toHexString(value).toUpperCase();
            default -> throw new IllegalArgumentException("Неподдерживаемая система: " + base);
        };
    }
}