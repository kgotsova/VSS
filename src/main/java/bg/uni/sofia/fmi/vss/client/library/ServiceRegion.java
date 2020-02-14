package bg.uni.sofia.fmi.vss.client.library;

import java.net.URI;

import static java.util.Objects.requireNonNull;

public class ServiceRegion {

    private final Platform platform;
    private final URI serviceURI;

    public ServiceRegion(Platform platform, String serviceURI) {
        this.platform = requireNonNull(platform);
        this.serviceURI = URI.create(serviceURI);
    }

    public URI getServiceURI() {
        return serviceURI;
    }

    public Platform getPlatform() {
        return platform;
    }
}
