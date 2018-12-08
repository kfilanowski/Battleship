package server;

import java.lang.Exception;

/**
 * This exception is specifically thrown when game ends because all ships
 * on a grid have been destroyed.
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public class GameOverException extends Exception {

    /**
     * Default constructor for the exception with no parameters.
     */
    public GameOverException() {
        super("Game Over!");
    }

    /**
     * Default constructor for the exception with no parameters.
     * @param message - A specified message for the exception.
     */
    public GameOverException(String message) {
        super(message);
    }

    /**
     * Default constructor for the exception with a message and a cause.
     * @param message - A specified message for the exception.
     * @param cause  - The cause of the exception.
     */
    public GameOverException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Default constructor for the exception with a cause.
     * @param cause - The cause of the exception.
     */
    public GameOverException(Throwable cause) {
        super(cause);
    }
}