package bg.uni.sofia.fmi.vss.client.library;

import bg.uni.sofia.fmi.vss.client.library.exceptions.AuthorizationException;
import bg.uni.sofia.fmi.vss.client.library.exceptions.ClientRequestException;
import bg.uni.sofia.fmi.vss.client.library.exceptions.ServerResponseException;
import bg.uni.sofia.fmi.vss.client.library.model.CustomerResourceEvent;
import bg.uni.sofia.fmi.vss.client.library.model.PagedResponse;

import java.util.Map;

public interface IAlertNotificationClient {

    /**
     * Posts an event for processing.
     *
     * @param event to be sent to   Alert Notification
     * @throws ClientRequestException  on failure to connect to   Alert Notification
     * @throws AuthorizationException  on authorization error returned from   Alert Notification
     * @throws ServerResponseException on error returned from   Alert Notification
     * @return the posted event enhanced with an unique ID that could be used for tracing
     */
    CustomerResourceEvent sendEvent(CustomerResourceEvent event) throws ClientRequestException, ServerResponseException;

    /**
     * Gets events that are matched by client's subscription.
     *
     * @param queryParameters for filtering of all available matched events
     * @throws ClientRequestException  on failure to connect to   Alert Notification
     * @throws AuthorizationException  on authorization error returned from   Alert Notification
     * @throws ServerResponseException on error returned from   Alert Notification
     * @return found results in pages
     */
    PagedResponse getMatchedEvents(Map<QueryParameter, String> queryParameters) throws ClientRequestException, ServerResponseException;

    /**
     * Gets event that is matched by client's subscription.
     *
     * @param eventId is the ID that was received in the response body when event was sent to   Alert Notification
     * @param queryParameters for filtering of all available events (those could be more than one with the same ID due to multiple matched subscriptions)
     * @throws ClientRequestException  on failure to connect to   Alert Notification
     * @throws AuthorizationException  on authorization error returned from   Alert Notification
     * @throws ServerResponseException on error returned from   Alert Notification
     * @return found results in pages
     */
    PagedResponse getMatchedEvent(String eventId, Map<QueryParameter, String> queryParameters) throws ClientRequestException, ServerResponseException;

    /**
     * Gets events undelivered to some targets.
     *
     * @param queryParameters for filtering of all available undelivered events
     * @throws ClientRequestException  on failure to connect to   Alert Notification
     * @throws AuthorizationException  on authorization error returned from   Alert Notification
     * @throws ServerResponseException on error returned from   Alert Notification
     * @return found results in pages
     */
    PagedResponse getUndeliveredEvents(Map<QueryParameter, String> queryParameters) throws ClientRequestException, ServerResponseException;

    /**
     * Gets event undelivered to some targets.
     *
     * @param eventId is the ID that was received in the response body when event was sent to   Alert Notification
     * @param queryParameters for filtering of all available events (those could be more than one with the same ID due to multiple matched subscriptions)
     * @throws ClientRequestException  on failure to connect to   Alert Notification
     * @throws AuthorizationException  on authorization error returned from   Alert Notification
     * @throws ServerResponseException on error returned from   Alert Notification
     * @return found results in pages
     */
    PagedResponse getUndeliveredEvent(String eventId, Map<QueryParameter, String> queryParameters) throws ClientRequestException, ServerResponseException;
}
