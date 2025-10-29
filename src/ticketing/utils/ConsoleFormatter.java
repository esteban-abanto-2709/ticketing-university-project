package ticketing.utils;

public class ConsoleFormatter {

    private static final int MAX_WIDTH = 60; // Longitud máxima de línea
    private static final int TAB_SIZE = 3; // Cantidad de espacios por nivel de tabulación

    public static void printCentered(String text, String fillChar) {

        if (text.length() >= MAX_WIDTH) {
            System.out.println(text.substring(0, MAX_WIDTH));
            return;
        }

        int remainingSpace = MAX_WIDTH - text.length();

        int leftPadding = remainingSpace / 2;
        int rightPadding = remainingSpace - leftPadding;

        String result = fillChar.repeat(leftPadding) + text + fillChar.repeat(rightPadding);
        System.out.println(result);
    }
    
    public static void cleanConsole() {
        String linesBreak = "\n".repeat(20);
        System.out.println(linesBreak);
    }

    public static void printTabbed(String text) {
        printTabbed(text, 1);
    }

    public static void printTabbed(String text, int tabLevel) {
        String spaces = " ".repeat(TAB_SIZE * tabLevel);
        System.out.println(spaces + text);
    }

    public static void printLine(String fillChar) {
        System.out.println(fillChar.repeat(MAX_WIDTH));
    }

    public static void printList(String leftText, String rightText, String fillChar) {
        printList(leftText, rightText, fillChar, 1);
    }

    public static void printList(String leftText, String rightText, String fillChar, int tabLevel) {
        // Aplicamos tabulación personalizada a ambos lados
        String leftWithTab = " ".repeat(TAB_SIZE * tabLevel) + leftText;
        String rightWithTab = rightText + " ".repeat(TAB_SIZE * tabLevel);

        // Calculamos el espacio disponible para el relleno
        int usedSpace = leftWithTab.length() + rightWithTab.length();

        // Si se pasa del ancho, recortamos
        if (usedSpace >= MAX_WIDTH) {
            String result = leftWithTab + rightWithTab;
            System.out.println(result.substring(0, MAX_WIDTH));
            return;
        }

        // Calculamos cuánto relleno necesitamos
        int fillSpace = MAX_WIDTH - usedSpace;
        String fill = fillChar.repeat(fillSpace);

        System.out.println(leftWithTab + fill + rightWithTab);
    }
    
    public static void printError(String message) {
        System.out.println("[Error]: " + message);
    }
    
    public static void printWaring(String message) {
        System.out.println("[Waring]: " + message);
    }
    
    public static void printInfo(String message) {
        System.out.println("[Info]: " + message);
    }
    
    public static void printSuccess(String message) {
        System.out.println("[Success]: " + message);
    }

    public static void printLineBreak(int count) {
        if (count < 1) return;
        System.out.print("\n".repeat(count));
    }

    public static void printLineBreak() {
        printLineBreak(1);
    }
}
