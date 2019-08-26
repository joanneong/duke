import java.util.Scanner;

public class Duke {
    private static String LOGO = " ____        _        \n"
        + "|  _ \\ _   _| | _____ \n"
        + "| | | | | | | |/ / _ \\\n"
        + "| |_| | |_| |   <  __/\n"
        + "|____/ \\__,_|_|\\_\\___|\n";

    private static String HORIZONTAL_LINE = "---------------------------------------------------\n";

    private static String GREETING = "    Hello! I'm Duke\n"
            +  "    What can I do for you?\n";

    private static String BYE = "bye";

    private static String BYE_MESSAGE = "Bye. Hope to see you again soon!";

    private static String INDENTATION = "    ";

    public static void greet_user() {
        System.out.println("Hello from\n" + LOGO);
        System.out.println(HORIZONTAL_LINE);
        System.out.println(GREETING);
        System.out.println(HORIZONTAL_LINE);
    }

    public static void echo_commands() {
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();

        while (!command.equals(BYE)) {
            System.out.println(HORIZONTAL_LINE);
            System.out.println(INDENTATION + command);
            System.out.println(HORIZONTAL_LINE);
            command = sc.nextLine();
        }

        System.out.println(HORIZONTAL_LINE);
        System.out.println(INDENTATION + BYE_MESSAGE);
        System.out.println(HORIZONTAL_LINE);
    }

    public static void main(String[] args) {
        greet_user();
        echo_commands();
    }
}
