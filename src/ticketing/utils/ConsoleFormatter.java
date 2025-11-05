package ticketing.utils;

import ticketing.Config;

public class ConsoleFormatter {

    private static final int MAX_WIDTH = 80;
    private static final int TAB_SIZE = 3;

    public static void printLeft(String text) {
        if (text == null) text = "";
        if (text.length() >= MAX_WIDTH) {
            System.out.println(text.substring(0, MAX_WIDTH));
            return;
        }

        int padding = MAX_WIDTH - text.length();
        String result = text + " ".repeat(padding);
        System.out.println(result);
    }

    public static void printRight(String text) {
        if (text == null) text = "";
        if (text.length() >= MAX_WIDTH) {
            System.out.println(text.substring(0, MAX_WIDTH));
            return;
        }

        int padding = MAX_WIDTH - text.length();
        String result = " ".repeat(padding) + text;
        System.out.println(result);
    }

    public static void printCentered(String text, String fillChar) {
        if (text == null) text = "";
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
        String leftWithTab = " ".repeat(TAB_SIZE * tabLevel) + leftText;
        String rightWithTab = rightText + " ".repeat(TAB_SIZE * tabLevel);

        int usedSpace = leftWithTab.length() + rightWithTab.length();

        if (usedSpace >= MAX_WIDTH) {
            // If too long, print trimmed text
            System.out.println((leftWithTab + " " + rightWithTab).substring(0, MAX_WIDTH));
            return;
        }

        int fillSpace = MAX_WIDTH - usedSpace;
        String fill = fillChar.repeat(fillSpace);

        System.out.println(leftWithTab + fill + rightWithTab);
    }

    public static void printError(String message) {
        System.out.println("[Error]: " + message);
    }

    public static void printWarning(String message) {
        System.out.println("[Warning]: " + message);
    }

    public static void printInfo(String message) {
        System.out.println("[Info]: " + message);
    }

    public static void printSuccess(String message) {
        System.out.println("[Success]: " + message);
    }

    public static void printDebug(String message) {
        if (!Config.SHOW_DEBUG_LOGS) return;
        System.out.println(message);
    }

    public static void cleanConsole() {
        printLineBreak(25);
    }

    public static void printLineBreak(int count) {
        if (count < 1) return;
        System.out.print("\n".repeat(count));
    }

    public static void printLineBreak() {
        printLineBreak(1);
    }
}
