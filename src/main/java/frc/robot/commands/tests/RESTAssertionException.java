package frc.robot.commands.tests;

public class RESTAssertionException extends RuntimeException{
    public RESTAssertionException() {
        super("REST assertion failed.");
    }

    public RESTAssertionException(String message) {
        super(message);
    }
}
