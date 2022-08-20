package duke;

import duke.command.Command;
import duke.common.DukeException;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Duke, a Personal Assistant Chatbot that helps a person to keep track of various things.
 *
 * @author Rama Aryasuta Pangestu
 */
public class Duke {
    private final Ui ui;
    private final Storage storage;
    private TaskList taskList;

    /**
     * Constructs an instance of Duke.
     *
     * @param filePath the file location for saving data
     */
    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            taskList = new TaskList(storage.load());
        } catch (DukeException exception) {
            ui.showError(exception.getMessage());
            taskList = new TaskList();
        }
    }

    public static void main(String[] args) {
        new Duke("data/tasks.txt").run();
    }

    /**
     * Starts the instance of Duke.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(ui, storage, taskList);
                isExit = c.isExit();
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }
}
