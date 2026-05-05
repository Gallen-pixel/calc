public class LongConverter extends AbstractConverter {
    @Override
    public long parse(String input, int base) {
        String clean = input;
        if (clean.startsWith("0X") && base == 16) clean = clean.substring(2);
        if (clean.startsWith("0B") && base == 2)  clean = clean.substring(2);
        if (clean.startsWith("0O") && base == 8)  clean = clean.substring(2);
        return Long.parseLong(clean, base);
    }

    @Override
    public String format(long value, int base) {
        return switch (base) {
            case 2 -> "0b" + Long.toBinaryString(value);
            case 8 -> "0o" + Long.toOctalString(value);
            case 10 -> String.valueOf(value);
            case 16 -> "0x" + Long.toHexString(value).toUpperCase();
            default -> throw new IllegalStateException("Неподдерживаемая система: " + base);
        };
    }
}