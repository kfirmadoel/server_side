package kfirmadoel.server_side.documents;

public class Command {

    private String command;
    private commandStatus status;

    enum commandStatus {
        success, fail
    }

    public String getCommand() {
        return command;
    }

    public commandStatus getStatus() {
        return status;
    }

    public Command(String command, commandStatus status) {
        this.command = command;
        this.status = status;
    }

    public Command(String command) {
        this.command = command;
    };

}
