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

    private static String LIST = "list";
    private static String BYE = "bye";

    private static String BYE_MESSAGE = "Bye. Hope to see you again soon!";

    private static String INDENTATION = "    ";

    private static String[] commands = new String[100];
    private static int no_of_commands = 0;

    public static void greet_user() {
        System.out.println("Hello from\n" + LOGO);
        System.out.println(HORIZONTAL_LINE);
        System.out.println(GREETING);
    }

    public static void print_goodbye() {
        System.out.println(INDENTATION + BYE_MESSAGE);
        System.out.println(HORIZONTAL_LINE);
    }

    public static void process_commands() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println(HORIZONTAL_LINE);
            String command = sc.nextLine();
            System.out.println(HORIZONTAL_LINE);

            if (command.equals(LIST)) {
                for (int i = 0; i < no_of_commands; i++) {
                    System.out.println(i + 1 + ". " + commands[i]);
                }
            } else if (command.equals(BYE)) {
                print_goodbye();
                break;
            } else {
                commands[no_of_commands] = command;
                no_of_commands++;
                System.out.println(INDENTATION + "added: " + command);
            }
        }
    }

    public static void main(String[] args) {
        greet_user();
        process_commands();
    }
}
