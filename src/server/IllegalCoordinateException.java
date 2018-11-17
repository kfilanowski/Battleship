package server;

import java.lang.Exception;

/**
 * This exception is specifically thrown when the user enters coordinates that
 * have already been attacked.
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version November 2018
 */
public class IllegalCoordinateException extends Exception {

    /**
     * Default constructor with no parameters.
     */
    public IllegalCoordinateException() {
        super();
    }

    /**
     * Default constructor for the exception with a message.
     * @param message - A specified message for the exception.
     */
    public IllegalCoordinateException(String message) {
        super(message);
    }

    /**
     * Default constructor for the exception with a message and a cause.
     * @param message - A specified message for the exception.
     * @param cause   - The cause of the exception.
     */
    public IllegalCoordinateException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Default constructor for the exception with a cause.
     * @param cause   - The cause of the exception.
     */
    public IllegalCoordinateException(Throwable cause) {
        super(cause);
    }
}