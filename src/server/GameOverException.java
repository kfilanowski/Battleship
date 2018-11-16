package server;

import java.lang.Exception;

/**
 *
 */
public class GameOverException extends Exception {
    /**
     *
     */
    public GameOverException() {
        super();
    }

    /**
     *
     * @param message
     */
    public GameOverException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public GameOverException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause
     */
    public GameOverException(Throwable cause) {
        super(cause);
    }
}