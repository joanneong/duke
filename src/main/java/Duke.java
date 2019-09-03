import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Duke {
    private static String LOGO = " ____        _        \n"
        + "|  _ \\ _   _| | _____ \n"
        + "| | | | | | | |/ / _ \\\n"
        + "| |_| | |_| |   <  __/\n"
        + "|____/ \\__,_|_|\\_\\___|\n";

    private static String HORIZONTAL_LINE = "-------------------------------------------------------------\n";

    private static String GREETING = "    Hello! I'm Duke\n"
            +  "    What can I do for you?\n";

    private static String DONE = "done";
    private static String LIST = "list";
    private static String BYE = "bye";
    private static String DELETE = "delete";

    private static String TODO = "todo";
    private static String EVENT = "event";
    private static String DEADLINE = "deadline";
    private static String TODO_SHORTFORM = "T";
    private static String EVENT_SHORTFORM = "E";
    private static String DEADLINE_SHORTFORM = "D";

    private static String DONE_MESSAGE = "Nice! I've marked this task as done:";
    private static String LIST_MESSAGE = "Here are the tasks in your list:";
    private static String BYE_MESSAGE = "Bye. Hope to see you again soon!";
    private static String ADDED_MESSAGE = "Got it. I've added this task:";
    private static String DELETE_MESSAGE = "Noted. I've removed this task:";

    private static String INVALID_COMMAND = "☹ OOPS!!! I'm sorry, but I don't know what that means :-(";
    private static String TODO_NO_DESC = "☹ OOPS!!! The description of a todo cannot be empty.";
    private static String EVENT_NO_DESC = "☹ OOPS!!! The description of an event cannot be empty.";
    private static String EVENT_NO_DATE = "☹ OOPS!!! The date of an event cannot be empty.";
    private static String DEADLINE_NO_DESC = "☹ OOPS!!! The description of a deadline cannot be empty.";
    private static String DEADLINE_NO_DATE = "☹ OOPS!!! The date of a deadline cannot be empty.";
    private static String NO_SUCH_COMMAND = "☹ OOPS!!! No such command exists in the list.";
    private static String INVALID_DATA = "☹ OOPS!!! The data saved/to be saved is invalid.";

    private static String INDENTATION = "    ";
    private static String DATA_DELIMITER = "|";
    private static String DATA_MARKED_DONE = "1";
    private static String DATA_MARKED_NOT_DONE = "0";

    private static String DATA_FILE = "data/duke.txt";

    private static ArrayList<Task> commands = new ArrayList<>(100);

    public static void loadData() throws Exception {
        File file = new File(DATA_FILE);
        file.createNewFile();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input;

        while ((input = br.readLine()) != null) {
            initialiseCommands(input);
        }
    }

    private static void initialiseCommands(String input) throws Exception {
        String[] toInitialise = input.split("\\" + DATA_DELIMITER);
        String taskType = toInitialise[0].trim();
        boolean isDone = toInitialise[1].trim().equals(DATA_MARKED_DONE);
        String description = toInitialise[2].trim();

        Task newTask;
        if (taskType.equals(TODO_SHORTFORM)) {
            newTask = new Todo(description);
        } else if (taskType.equals(EVENT_SHORTFORM)) {
            newTask = new Event(description, toInitialise[3]);
        } else if (taskType.equals(DEADLINE_SHORTFORM)) {
            newTask = new Deadline(description, toInitialise[3]);
        } else {
            throw new DukeException(INDENTATION + INVALID_DATA);
        }

        if (isDone) {
            newTask.markDone();
        }

        commands.add(newTask);
    }

    private static void saveData() throws Exception {
        FileWriter fw = new FileWriter(DATA_FILE);

        for (Task task: commands) {
            String doneStatus = task.isDone ? DATA_MARKED_DONE : DATA_MARKED_NOT_DONE;

            if (task instanceof Todo) {
                fw.write(TODO_SHORTFORM + DATA_DELIMITER + doneStatus + DATA_DELIMITER
                        + task.getDescription());
            } else if (task instanceof Event) {
                fw.write(EVENT_SHORTFORM + DATA_DELIMITER + doneStatus + DATA_DELIMITER
                        + task.getDescription() + DATA_DELIMITER + ((Event) task).getAt());
            } else if (task instanceof Deadline) {
                fw.write(DEADLINE_SHORTFORM + DATA_DELIMITER + doneStatus + DATA_DELIMITER
                        + task.getDescription() + DATA_DELIMITER + ((Deadline) task).getBy());
            } else {
                throw new DukeException(INDENTATION + INVALID_DATA);
            }

            fw.write("\n");
        }

        fw.close();
    }

    public static void greetUser() {
        System.out.println("Hello from\n" + LOGO);
        System.out.println(HORIZONTAL_LINE);
        System.out.println(GREETING);
    }

    public static void checkTask(String command) {
        String[] splitCommand = command.split(" ");
        if (splitCommand.length == 2) {
            int idx = Integer.parseInt(splitCommand[1]) - 1;

            if (idx >= 0 && idx < commands.size()) {
                commands.get(idx).markDone();

                System.out.println(INDENTATION + DONE_MESSAGE);
                System.out.println(INDENTATION + "[" + commands.get(idx).getStatusIcon() + "] " + commands.get(idx).getDescription());
            }
        }
    }

    public static void printList() {
        System.out.println(INDENTATION + LIST_MESSAGE);
        for (int i = 0; i < commands.size(); i++) {
            Task task = commands.get(i);
            System.out.println(INDENTATION + (i + 1) + ". " + task.toString());
        }
    }

    public static void printGoodbye() {
        System.out.println(INDENTATION + BYE_MESSAGE);
        System.out.println(HORIZONTAL_LINE);
    }

    public static void deleteCommand(String command) throws Exception {
        String[] splitCommand = command.split(" ");
        int idx = Integer.parseInt(splitCommand[1]) - 1;

        if (idx < 0 || idx >= commands.size()) {
            throw new DukeException(INDENTATION + NO_SUCH_COMMAND);
        }

        Task toRemove = commands.get(idx);
        commands.remove(idx);
        System.out.println(DELETE_MESSAGE);
        System.out.println(INDENTATION + toRemove.toString());
        System.out.println("Now you have " + commands.size() + " tasks in the list.");
    }

    public static void addCommand(String command) throws Exception {
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
            if (splitCommand.length <= 1) {
                throw new DukeException(INDENTATION + TODO_NO_DESC);
            }
            newTask = new Todo(remainingCommand);
        } else if (taskType.equals(EVENT)) {
            if (splitCommand.length <= 1) {
                throw new DukeException(INDENTATION + EVENT_NO_DESC);
            }

            String[] splitRemaining = remainingCommand.split("/at");
            if (splitRemaining.length < 2) {
                throw new DukeException(INDENTATION + EVENT_NO_DATE);
            }
            newTask = new Event(splitRemaining[0], splitRemaining[1]);
        } else if (taskType.equals(DEADLINE)) {
            if (splitCommand.length <= 1) {
                throw new DukeException(INDENTATION + DEADLINE_NO_DESC);
            }

            String[] splitRemaining = remainingCommand.split("/by");
            if (splitRemaining.length < 2) {
                throw new DukeException(INDENTATION + DEADLINE_NO_DATE);
            }
            newTask = new Deadline(splitRemaining[0], splitRemaining[1]);
        }

        commands.add(newTask);
        System.out.println(INDENTATION + ADDED_MESSAGE);
        System.out.println(INDENTATION + newTask.toString());
        System.out.println(INDENTATION + "Now you have " + commands.size() + " tasks in the list.");
    }

    public static void processCommands() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println(HORIZONTAL_LINE);
            String command = sc.nextLine();
            System.out.println(command);
            System.out.println(HORIZONTAL_LINE);

            try {
                if (command.contains(DONE)) {
                    checkTask(command);
                    saveData();
                } else if (command.equals(LIST)) {
                    printList();
                } else if (command.equals(BYE)) {
                    printGoodbye();
                    break;
                } else if (command.contains(DELETE)) {
                    deleteCommand(command);
                    saveData();
                } else if (command.contains(TODO) || command.contains(EVENT) || command.contains(DEADLINE)) {
                    addCommand(command);
                    saveData();
                } else {
                    throw new DukeException(INDENTATION + INVALID_COMMAND);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            loadData();
            greetUser();
            processCommands();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
