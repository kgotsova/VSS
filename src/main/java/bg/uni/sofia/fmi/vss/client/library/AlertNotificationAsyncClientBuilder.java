package bg.uni.sofia.fmi.vss.client.library;

import bg.uni.sofia.fmi.vss.client.library.exceptions.BufferOverflowException;
import bg.uni.sofia.fmi.vss.client.library.internal.AlertNotificationAsyncClient;
import bg.uni.sofia.fmi.vss.client.library.internal.InMemoryCustomerResourceEventBuffer;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

public class AlertNotificationAsyncClientBuilder {

    public static final int DEFAULT_MIN_THREADS_COUNT = 10;
    public static final int DEFAULT_MAX_THREADS_COUNT = 20;
    public static final int DEFAULT_EVENT_BUFFER_CAPACITY = 100;
    public static final long DEFAULT_IDLE_THREADS_LIFESPAN_SECONDS = 30L;

    private int minThreadsCount;
    private int maxThreadsCount;
    private long idleThreadsLifespanInSeconds;
    private ICustomerResourceEventBuffer eventBuffer;
    private IAlertNotificationClient alertNotificationClient;

    public AlertNotificationAsyncClientBuilder(IAlertNotificationClient alertNotificationClient) {
        this.minThreadsCount = DEFAULT_MIN_THREADS_COUNT;
        this.maxThreadsCount = DEFAULT_MAX_THREADS_COUNT;
        this.alertNotificationClient = alertNotificationClient;
        this.idleThreadsLifespanInSeconds = DEFAULT_IDLE_THREADS_LIFESPAN_SECONDS;
        this.eventBuffer = new InMemoryCustomerResourceEventBuffer(DEFAULT_EVENT_BUFFER_CAPACITY);
    }

    public AlertNotificationAsyncClientBuilder withThreadsCount(int minThreadsCount, int maxThreadsCount) {
        this.minThreadsCount = minThreadsCount;
        this.maxThreadsCount = maxThreadsCount;

        return this;
    }

    public AlertNotificationAsyncClientBuilder withIdleThreadsLifespan(Duration duration) {
        this.idleThreadsLifespanInSeconds = duration.getSeconds();

        return this;
    }

    public AlertNotificationAsyncClientBuilder withEventBuffer(ICustomerResourceEventBuffer eventBuffer) {
        this.eventBuffer = eventBuffer;

        return this;
    }

    public AlertNotificationAsyncClient build() {
        assertValidThreadCountRange(minThreadsCount, maxThreadsCount);

        return new AlertNotificationAsyncClient(createExecutorService(), requireNonNull(eventBuffer), requireNonNull(alertNotificationClient));
    }

    private ExecutorService createExecutorService() {
        return new ThreadPoolExecutor(
                minThreadsCount,
                maxThreadsCount,
                idleThreadsLifespanInSeconds,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(eventBuffer.getCapacity()),
                (r, executor) -> {
                    throw new BufferOverflowException();
                });
    }

    private static void assertValidThreadCountRange(int minThreadsCount, int maxThreadsCount) {
        if (minThreadsCount < 1 || minThreadsCount > maxThreadsCount) {
            throw new IllegalArgumentException();
        }
    }
}
