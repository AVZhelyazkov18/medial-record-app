package bg.nbu.medialrecordapp.exception;

public class CredentialsNotFoundException extends RuntimeException {
    public CredentialsNotFoundException(String message) {
        super(message);
    }
}
