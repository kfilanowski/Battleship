package server;

import java.lang.Exception;

/**
 * 
 */
public class CoordinateOutOfBoundsException extends Exception {
    /**
     * 
     */
    public CoordinateOutOfBoundsException() {
        super();
    }

    /**
     * 
     * @param message
     */
    public CoordinateOutOfBoundsException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public CoordinateOutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 
     * @param cause
     */
    public CoordinateOutOfBoundsException(Throwable cause) {
        super(cause);
    }
}