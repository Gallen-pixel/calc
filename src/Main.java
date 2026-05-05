import java.util.Scanner;

public class Main {
    private long currentValue = 0;
    private int currentBase = 10;

    private final NumberConverter converter;
    private final Calculator calculator;
    private final Scanner scanner;

    public Main(NumberConverter converter, Calculator calculator) {
        this.converter = converter;
        this.calculator = calculator;
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        NumberConverter converter = new LongConverter();
        Calculator calculator = new ProgrammerCalculator();
        new Main(converter, calculator).run();
    }

    private void run() {
        System.out.println("Введите HELP для списка команд");

        boolean running = true;
        while (running) {
            displayState();
            System.out.print(">> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            try {
                String[] parts = parseInput(line);
                String cmd = parts[0].toUpperCase();
                running = executeCommand(cmd, parts);
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }
        System.out.println("Программа завершена.");
    }

    private String[] parseInput(String line) {
        String opCandidate = null;
        if (line.startsWith("<<")) opCandidate = "<<";
        else if (line.startsWith(">>")) opCandidate = ">>";
        else if (line.length() > 1 && "+-*/%&|^".indexOf(line.charAt(0)) != -1) {
            opCandidate = String.valueOf(line.charAt(0));
        }

        if (opCandidate != null) {
            String rest = line.substring(opCandidate.length()).trim();
            return rest.isEmpty() ? new String[]{opCandidate} : new String[]{opCandidate, rest};
        }
        return line.split("\\s+");
    }

    private void displayState() {
        System.out.printf("HEX : %-25s%n", converter.format(currentValue, 16));
        System.out.printf("DEC : %-25s%n", converter.format(currentValue, 10));
        System.out.printf("OCT : %-25s%n", converter.format(currentValue, 8));
        System.out.printf("BIN : %-25s%n", converter.format(currentValue, 2));
        System.out.printf("Текущая СС: %-22s%n", currentBase);
    }

    private boolean executeCommand(String cmd, String[] parts) {
        switch (cmd) {
            case "EXIT", "QUIT" -> { return false; }
            case "CLEAR", "C" -> { currentValue = 0; System.out.println("Очищено."); return true; }
            case "HELP", "?" -> { showHelp(); return true; }
            case "BASE" -> { return handleBase(parts); }
            case "INPUT", "SET" -> { return handleInput(parts); }
            case "+", "-", "*", "/", "%", "&", "|", "^", "<<", ">>" -> { return handleBinaryOp(cmd, parts); }
            case "~" -> { currentValue = calculator.not(currentValue); System.out.println("Применено побитовое NOT (~)"); return true; }
            default -> { System.out.println("Неизвестная команда. Введите HELP."); return true; }
        }
    }

    private boolean handleBase(String[] parts) {
        if (parts.length < 2) throw new IllegalArgumentException("Использование: BASE <2|8|10|16>");
        int newBase = Integer.parseInt(parts[1]);
        if (!converter.supportsBase(newBase)) {
            throw new IllegalArgumentException("Поддерживаются только основания: 2, 8, 10, 16");
        }
        currentBase = newBase;
        System.out.println("Система счисления изменена на " + currentBase);
        return true;
    }

    private boolean handleInput(String[] parts) {
        if (parts.length < 2) throw new IllegalArgumentException("Использование: INPUT <число>");
        currentValue = converter.safeParse(parts[1], currentBase);
        System.out.println("Значение установлено.");
        return true;
    }

    private boolean handleBinaryOp(String op, String[] parts) {
        if (parts.length < 2) throw new IllegalArgumentException("Использование: " + op + " <число>");
        long operand = converter.safeParse(parts[1], currentBase);

        currentValue = switch (op) {
            case "+"  -> calculator.add(currentValue, operand);
            case "-"  -> calculator.subtract(currentValue, operand);
            case "*"  -> calculator.multiply(currentValue, operand);
            case "/"  -> calculator.divide(currentValue, operand);
            case "%"  -> calculator.mod(currentValue, operand);
            case "&"  -> calculator.and(currentValue, operand);
            case "|"  -> calculator.or(currentValue, operand);
            case "^"  -> calculator.xor(currentValue, operand);
            case "<<" -> calculator.shiftLeft(currentValue, (int) operand);
            case ">>" -> calculator.shiftRight(currentValue, (int) operand);
            default   -> throw new IllegalStateException("Неизвестная операция");
        };

        System.out.println("Операция " + op + " выполнена.");
        return true;
    }

    private void showHelp() {
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
              INPUT FF
              +10  или  + 10
              <<2  или  << 2
              &FF
            """);
    }
}