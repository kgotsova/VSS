package bg.uni.sofia.fmi.vss.client.library.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FailureReason {

    private final Integer code;
    private final String reason;
    private final Long timestamp;

    @JsonCreator
    public FailureReason(
            @JsonProperty("code") int code,
            @JsonProperty("reason") String reason,
            @JsonProperty("timestamp") long timestamp
    ) {
        this.code = code;
        this.reason = reason;
        this.timestamp = timestamp;
    }

    public Integer getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
