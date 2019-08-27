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

    private static String DONE = "done";
    private static String LIST = "list";
    private static String BYE = "bye";
    private static String TODO = "todo";
    private static String EVENT = "event";
    private static String DEADLINE = "deadline";

    private static String DONE_MESSAGE = "Nice! I've marked this task as done:";
    private static String LIST_MESSAGE = "Here are the tasks in your list:";
    private static String BYE_MESSAGE = "Bye. Hope to see you again soon!";
    private static String ADDED_MESSAGE = "Got it. I've added this task:";

    private static String INDENTATION = "    ";

    private static Task[] commands = new Task[100];
    private static int no_of_commands = 0;

    public static void greet_user() {
        System.out.println("Hello from\n" + LOGO);
        System.out.println(HORIZONTAL_LINE);
        System.out.println(GREETING);
    }

    public static void check_task(String command) {
        String[] splitCommand = command.split(" ");
        if (splitCommand.length == 2) {
            int idx = Integer.parseInt(splitCommand[1]) - 1;

            if (idx >= 0 && idx < no_of_commands) {
                commands[idx].markDone();

                System.out.println(INDENTATION + DONE_MESSAGE);
                System.out.println(INDENTATION + "[" + commands[idx].getStatusIcon() + "] " + commands[idx].getDescription());
            }
        }
    }

    public static void print_list() {
        System.out.println(INDENTATION + LIST_MESSAGE);
        for (int i = 0; i < no_of_commands; i++) {
            Task task = commands[i];
            System.out.println(INDENTATION + (i + 1) + ". " + task.toString());
        }
    }

    public static void print_goodbye() {
        System.out.println(INDENTATION + BYE_MESSAGE);
        System.out.println(HORIZONTAL_LINE);
    }

    public static void add_command(String command) {
        String[] splitCommand = command.split(" ");
        String taskType = splitCommand[0];

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < splitCommand.length; i++) {
            builder.append(splitCommand[i]);

            if (i != splitCommand.length - 1) {
                builder.append(" ");
            }
        }
        String remainingCommand = builder.toString();

        Task newTask = null;
        if (taskType.equals(TODO)) {
            newTask = new Todo(remainingCommand);
        } else if (taskType.equals(EVENT)) {
            String[] splitRemaining = remainingCommand.split("/at");
            newTask = new Event(splitRemaining[0], splitRemaining[1]);
        } else if (taskType.equals(DEADLINE)) {
            String[] splitRemaining = remainingCommand.split("/by");
            newTask = new Deadline(splitRemaining[0], splitRemaining[1]);
        }

        commands[no_of_commands] = newTask;
        no_of_commands++;
        System.out.println(INDENTATION + ADDED_MESSAGE);
        System.out.println(INDENTATION + newTask.toString());
        System.out.println(INDENTATION + "Now you have " + no_of_commands + " tasks in the list.");
    }

    public static void process_commands() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println(HORIZONTAL_LINE);
            String command = sc.nextLine();
            System.out.println(command);
            System.out.println(HORIZONTAL_LINE);

            if (command.contains(DONE)) {
                check_task(command);
            } else if (command.equals(LIST)) {
                print_list();
            } else if (command.equals(BYE)) {
                print_goodbye();
                break;
            } else {
                add_command(command);
            }
        }
    }

    public static void main(String[] args) {
        greet_user();
        process_commands();
    }
}
