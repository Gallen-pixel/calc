import java.util.Scanner;

public class Main {
    private static long currentValue = 0;
    private static int currentBase = 10;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Введите HELP для списка команд\n");

        boolean running = true;
        while (running) {
            displayState();
            System.out.print(">> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            try {
                String[] parts;

                // 🆕 Поддержка ввода без пробела: "+10", "<<3", "&FF", "-5"
                String opCandidate = null;
                if (line.startsWith("<<")) opCandidate = "<<";
                else if (line.startsWith(">>")) opCandidate = ">>";
                else if (line.length() > 1 && "+-*/%&|^".indexOf(line.charAt(0)) != -1) {
                    opCandidate = String.valueOf(line.charAt(0));
                }

                if (opCandidate != null) {
                    String rest = line.substring(opCandidate.length()).trim();
                    if (!rest.isEmpty()) {
                        parts = new String[]{opCandidate, rest};
                    } else {
                        // Введён только оператор без операнда
                        parts = new String[]{opCandidate};
                    }
                } else {
                    // Стандартное разделение по пробелам (BASE 16, INPUT FF, HELP и т.д.)
                    parts = line.split("\\s+");
                }

                String cmd = parts[0].toUpperCase();
                running = executeCommand(cmd, parts);
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }
        System.out.println("Программа завершена.");
    }

    private static void displayState() {
        System.out.printf("HEX : %-25s%n", NumberConverter.format(currentValue, 16));
        System.out.printf("DEC : %-25s%n", NumberConverter.format(currentValue, 10));
        System.out.printf("OCT : %-25s%n", NumberConverter.format(currentValue, 8));
        System.out.printf("BIN : %-25s%n", NumberConverter.format(currentValue, 2));
        System.out.printf("Основа: %-22s%n", currentBase);
    }

    private static boolean executeCommand(String cmd, String[] parts) {
        switch (cmd) {
            case "EXIT":
            case "QUIT":
                return false;

            case "CLEAR":
            case "C":
                currentValue = 0;
                System.out.println("Очищено.");
                return true;

            case "HELP":
            case "?":
                showHelp();
                return true;

            case "BASE":
                return handleBase(parts);

            case "INPUT":
            case "SET":
                return handleInput(parts);

            case "+": case "-": case "*": case "/": case "%":
            case "&": case "|": case "^": case "<<": case ">>":
                return handleBinaryOp(cmd, parts);

            case "~":
                currentValue = Calculator.not(currentValue);
                System.out.println("Применено побитовое NOT (~)");
                return true;

            default:
                System.out.println("Неизвестная команда. Введите HELP.");
                return true;
        }
    }

    private static boolean handleBase(String[] parts) {
        if (parts.length < 2) throw new IllegalArgumentException("Использование: BASE <2|8|10|16>");
        int newBase = Integer.parseInt(parts[1]);
        if (newBase != 2 && newBase != 8 && newBase != 10 && newBase != 16) {
            throw new IllegalArgumentException("Поддерживаются только основания: 2, 8, 10, 16");
        }
        currentBase = newBase;
        System.out.println("Система счисления изменена на " + newBase);
        return true;
    }

    private static boolean handleInput(String[] parts) {
        if (parts.length < 2) throw new IllegalArgumentException("Использование: INPUT <число>");
        currentValue = NumberConverter.parse(parts[1], currentBase);
        System.out.println("Значение установлено.");
        return true;
    }

    private static boolean handleBinaryOp(String op, String[] parts) {
        if (parts.length < 2) throw new IllegalArgumentException("Использование: " + op + " <число>");
        long operand = NumberConverter.parse(parts[1], currentBase);

        currentValue = switch (op) {
            case "+"  -> Calculator.add(currentValue, operand);
            case "-"  -> Calculator.subtract(currentValue, operand);
            case "*"  -> Calculator.multiply(currentValue, operand);
            case "/"  -> Calculator.divide(currentValue, operand);
            case "%"  -> Calculator.mod(currentValue, operand);
            case "&"  -> Calculator.and(currentValue, operand);
            case "|"  -> Calculator.or(currentValue, operand);
            case "^"  -> Calculator.xor(currentValue, operand);
            case "<<" -> Calculator.shl(currentValue, (int) operand);
            case ">>" -> Calculator.shr(currentValue, (int) operand);
            default   -> throw new IllegalStateException("Неизвестная операция");
        };

        System.out.println("Операция " + op + " выполнена.");
        return true;
    }

    private static void showHelp() {
        System.out.println("""
            📖 Доступные команды:
              +, -, *, /, %       - Арифметика
              &, |, ^             - Побитовые И, ИЛИ, XOR
              <<, >>              - Сдвиг влево/вправо
              ~                   - Побитовое НЕ (унарное)
              BASE <2|8|10|16>    - Смена активной системы счисления
              INPUT <число>       - Установка значения в текущем основании
              CLEAR               - Очистка аккумулятора (сброс в 0)
              HELP                - Показать справку
              EXIT                - Выход

            💡 Примеры ввода (с пробелом или без):
              INPUT FF       (при BASE 16 установит 255)
              +10  или  + 10 (прибавит 10)
              <<2  или  << 2 (сдвинет на 2 бита влево)
              &FF            (побитовое И с 0xFF)
            """);
    }
}