package music.server.exceptionhandle;

public class UserNotFoundException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super("User not found");
    }
}
