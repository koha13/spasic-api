package music.server.exceptionhandle;

public class PasswordNotMatchException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PasswordNotMatchException() {
        super("Current password not match or new password invalid");
    }
}