package bg.uni.sofia.fmi.vss.client.library.exceptions;

public class AuthorizationException extends ServerResponseException {

    private static final long serialVersionUID = 1680072165927004872L;

    public AuthorizationException(String msg, int status) {
        super(msg, status);
    }
}
