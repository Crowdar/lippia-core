package io.lippia.api.lowcode.exception;

public class LippiaException extends RuntimeException {

    public  LippiaException(String message){
        super(message);
    }
    public  LippiaException(String message,Object ...parameters){
        super(String.format(message, parameters));
    }
}
