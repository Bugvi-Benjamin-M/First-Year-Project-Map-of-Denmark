package Exceptions;

/*
 * This exception is thrown, when you try to initialize a window with
 * an unsupported layout.
 */
public class IncorrectLayoutException extends RuntimeException {

    /*
     * Constructor
     */
    public IncorrectLayoutException(String message) { super(message); }
}
