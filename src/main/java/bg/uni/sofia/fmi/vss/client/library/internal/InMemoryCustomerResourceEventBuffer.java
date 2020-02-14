package bg.uni.sofia.fmi.vss.client.library.internal;

import bg.uni.sofia.fmi.vss.client.library.model.CustomerResourceEvent;
import bg.uni.sofia.fmi.vss.client.library.ICustomerResourceEventBuffer;
import bg.uni.sofia.fmi.vss.client.library.exceptions.BufferOverflowException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryCustomerResourceEventBuffer implements ICustomerResourceEventBuffer {

    private final int capacity;
    private final Map<UUID, CustomerResourceEvent> eventBuffer;

    public InMemoryCustomerResourceEventBuffer(int capacity) {
        this.capacity = capacity;
        this.eventBuffer = new HashMap<>(capacity);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public synchronized CustomerResourceEvent read(UUID eventUuid) {
        return eventBuffer.remove(eventUuid);
    }

    @Override
    public synchronized UUID write(CustomerResourceEvent event) throws BufferOverflowException {
        assertBufferHasFreeCapacity();

        UUID eventUuid = UUID.randomUUID();

        eventBuffer.put(eventUuid, event);

        return eventUuid;
    }

    private void assertBufferHasFreeCapacity() {
        if (eventBuffer.size() >= capacity) {
            throw new BufferOverflowException();
        }
    }
}
