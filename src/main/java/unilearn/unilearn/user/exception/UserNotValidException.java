package unilearn.unilearn.user.exception;

import java.util.List;

public class UserNotValidException extends RuntimeException{
    private List<String> errorList;

    public UserNotValidException() {
        super();
    }

    public UserNotValidException(String message) {
        super(message);
    }

    public UserNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotValidException(Throwable cause) {
        super(cause);
    }

    protected UserNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserNotValidException(List<String> errorList, String message) {
        super(message);
        this.errorList = errorList;
    }

    public List<String> getErrorList() {
        return errorList;
    }
}
