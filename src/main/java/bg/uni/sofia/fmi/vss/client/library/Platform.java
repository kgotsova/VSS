package bg.uni.sofia.fmi.vss.client.library;

import static java.util.Objects.requireNonNull;

public class Platform {

    private final String key;

    public Platform(String key) {
        this.key = requireNonNull(key);
    }

    public String getKey() {
        return key;
    }
}
