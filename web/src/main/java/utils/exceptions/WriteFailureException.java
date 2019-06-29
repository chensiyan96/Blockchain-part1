package utils.exceptions;

public class WriteFailureException extends Exception {
    public WriteFailureException()  {
        super();
    }
    public WriteFailureException(String msg)  {
        super(msg);
    }
}
