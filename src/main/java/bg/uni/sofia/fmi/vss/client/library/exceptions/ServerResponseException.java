package bg.uni.sofia.fmi.vss.client.library.exceptions;

public class ServerResponseException extends RuntimeException {

    private static final long serialVersionUID = 5611541307804757631L;

    private Integer statusCode;

    public ServerResponseException(String msg, Integer statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }
}
