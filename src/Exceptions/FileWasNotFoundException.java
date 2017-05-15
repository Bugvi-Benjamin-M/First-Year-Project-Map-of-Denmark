package Exceptions;

/*
 * This exception describes an event, where a selected file wasn't found.
 */
public class FileWasNotFoundException extends Exception {

    /*
     * Constructor
     */
    public FileWasNotFoundException(String message) { super(message); }
}
