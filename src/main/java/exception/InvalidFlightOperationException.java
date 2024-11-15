package exception;

public class InvalidFlightOperationException extends RuntimeException{

    public InvalidFlightOperationException(String message){
        super(message);
    }
}
