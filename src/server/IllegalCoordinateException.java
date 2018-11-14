package server;

import java.lang.Exception;

/**
 * 
 */
public class IllegalCoordinateException extends Exception {
    /**
     * 
     */
    public IllegalCoordinateException() {
        super();
    }

    /**
     * 
     * @param message
     */
    public IllegalCoordinateException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public IllegalCoordinateException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 
     * @param cause
     */
    public IllegalCoordinateException(Throwable cause) {
        super(cause);
    }
}