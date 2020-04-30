package music.server.exceptionhandle;

public class ApiMissingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ApiMissingException() {
        super("API key is missing or invalid");
    }
}
