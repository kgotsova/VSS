package bg.uni.sofia.fmi.vss.client.library.exceptions;

public class ClientRequestException extends RuntimeException {

    private static final long serialVersionUID = 1680072165927004872L;

    public ClientRequestException(Throwable cause) {
        super(cause);
    }
}
