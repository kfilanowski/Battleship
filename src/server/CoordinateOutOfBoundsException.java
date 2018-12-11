package server;

import java.lang.Exception;

/**
 * This exception is specifically thrown when the user enters coordinates that
 * do not exist within the size of the grid.
 * 
 * @author Kevin Filanowski
 * @author Jeriah Caplinger
 * @version December 2018
 */
public class CoordinateOutOfBoundsException extends Exception {
    /**
     * Default constructor with no parameters.
     */
    public CoordinateOutOfBoundsException() {
        super("Coordinate's are not on the game board." 
        + "\nPlease pick another set of coordinates to attack.");
    }

    /**
     * Default constructor with the message parameter.
     * 
     * @param message - A specified custom error message.
     */
    public CoordinateOutOfBoundsException(String message) {
        super(message);
    }

    /**
     * Default constructor with a message and cause parameter.
     * 
     * @param message - A specified custom error message.
     * @param cause   - The cause of the exception.
     */
    public CoordinateOutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Default constructor with the cause parameter.
     * 
     * @param cause - The cause of the exception.
     */
    public CoordinateOutOfBoundsException(Throwable cause) {
        super(cause);
    }
}