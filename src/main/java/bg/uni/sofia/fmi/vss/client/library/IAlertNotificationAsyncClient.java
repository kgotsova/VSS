package bg.uni.sofia.fmi.vss.client.library;

import bg.uni.sofia.fmi.vss.client.library.exceptions.BufferOverflowException;
import bg.uni.sofia.fmi.vss.client.library.model.CustomerResourceEvent;
import bg.uni.sofia.fmi.vss.client.library.model.PagedResponse;

import java.util.Map;
import java.util.concurrent.Future;

public interface IAlertNotificationAsyncClient {

    /**
     * Posts an event for async processing.
     *
     * @param event to be sent to   Alert Notification
     * @return the posted event enhanced with an unique ID that could be used for tracing
     * @throws BufferOverflowException if the event cannot be accepted for execution due to full buffer capacity
     */
    Future<CustomerResourceEvent> sendEvent(CustomerResourceEvent event) throws BufferOverflowException;

    /**
     * Gets events that are matched by client's subscription.
     *
     * @param queryParameters for filtering of all available matched events
     * @return found results in pages
     * @throws BufferOverflowException if the event cannot be accepted for execution due to full buffer capacity
     */
    Future<PagedResponse> getMatchedEvents(Map<QueryParameter, String> queryParameters) throws BufferOverflowException;

    /**
     * Gets event that is matched by client's subscription.
     *
     * @param eventId is the ID that was received in the response body when event was sent to   Alert Notification
     * @param queryParameters for filtering of all available events (those could be more than one with the same ID due to multiple matched subscriptions)
     * @return found results in pages
     * @throws BufferOverflowException if the event cannot be accepted for execution due to full buffer capacity
     */
    Future<PagedResponse> getMatchedEvent(String eventId, Map<QueryParameter, String> queryParameters) throws BufferOverflowException;

    /**
     * Gets events undelivered to some targets.
     *
     * @param queryParameters for filtering of all available undelivered events
     * @return found results in pages
     * @throws BufferOverflowException if the event cannot be accepted for execution due to full buffer capacity
     */
    Future<PagedResponse> getUndeliveredEvents(Map<QueryParameter, String> queryParameters) throws BufferOverflowException;

    /**
     * Gets event undelivered to some targets.
     *
     * @param eventId is the ID that was received in the response body when event was sent to   Alert Notification
     * @param queryParameters for filtering of all available events (those could be more than one with the same ID due to multiple matched subscriptions)
     * @return found results in pages
     * @throws BufferOverflowException if the event cannot be accepted for execution due to full buffer capacity
     */
    Future<PagedResponse> getUndeliveredEvent(String eventId, Map<QueryParameter, String> queryParameters)
            throws BufferOverflowException;

    void shutdown();
}
