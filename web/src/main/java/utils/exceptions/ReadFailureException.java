package utils.exceptions;

public class ReadFailureException extends Exception {
    public ReadFailureException()  {
        super();
    }
    public ReadFailureException(String msg)  {
        super(msg);
    }
}
