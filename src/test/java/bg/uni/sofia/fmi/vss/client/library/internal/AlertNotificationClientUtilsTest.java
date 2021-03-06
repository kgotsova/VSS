package bg.uni.sofia.fmi.vss.client.library.internal;

import bg.uni.sofia.fmi.vss.client.library.Platform;
import bg.uni.sofia.fmi.vss.client.library.QueryParameter;
import bg.uni.sofia.fmi.vss.client.library.ServiceRegion;
import bg.uni.sofia.fmi.vss.client.library.exceptions.AuthorizationException;
import bg.uni.sofia.fmi.vss.client.library.exceptions.ServerResponseException;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static bg.uni.sofia.fmi.vss.client.library.internal.AlertNotificationClientUtils.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

public class AlertNotificationClientUtilsTest {

    private static final String TEST_EVENT_ID = "12345";
    private static final String TEST_PROTOCOL_NAME = "HTTP";
    private static final int TEST_PROTOCOL_MAJOR_VERSION = 3;
    private static final int TEST_PROTOCOL_MINOR_VERSION = 0;
    private static final String TEST_REASON_PHRASE = "Test Reason";
    private static final String TEST_PLATFORM_NAME = "TEST_PLATFORM";
    private static final String TEST_SERVICE_URI = "https://no.such.url..com";
    private static final Platform TEST_PLATFORM = new Platform(TEST_PLATFORM_NAME);
    private static final ServiceRegion TEST_SERVICE_REGION = new ServiceRegion(TEST_PLATFORM, TEST_SERVICE_URI);
    private static final ProtocolVersion TEST_PROTOCOL_VERSION = new ProtocolVersion(TEST_PROTOCOL_NAME, TEST_PROTOCOL_MAJOR_VERSION, TEST_PROTOCOL_MINOR_VERSION);

    @Test
    public void givenThatResponseIsSuccessful_whenAssertSuccessfulResponseIsCalled_thenNoExceptionIsThrown() {
        for (int httpStatus = SC_OK; httpStatus < SC_MULTIPLE_CHOICES; ++httpStatus) {
            HttpResponse successfulResponse = createResponseForCode(httpStatus);
            assertDoesNotThrow(() -> assertSuccessfulResponse(successfulResponse));
        }
    }

    @Test
    public void givenThatResponseIsForbidden_whenAssertSuccessfulResponseIsCalled_thenExceptionIsThrown() {
        assertThrows(AuthorizationException.class, () -> assertSuccessfulResponse(createResponseForCode(SC_FORBIDDEN)));
    }

    @Test
    public void givenThatResponseIsUnauthorized_whenAssertSuccessfulResponseIsCalled_thenExceptionIsThrown() {
        assertThrows(AuthorizationException.class, () -> assertSuccessfulResponse(createResponseForCode(SC_UNAUTHORIZED)));
    }

    @Test
    public void givenThatFailureResponseIsChecked_whenAssertSuccessfulResponseIsCalled_thenExceptionIsThrown() {
        for (int httpStatus = 0; httpStatus < 600; ++httpStatus) {
            if ((httpStatus >= SC_OK && httpStatus < SC_MULTIPLE_CHOICES) || httpStatus == SC_FORBIDDEN || httpStatus == SC_UNAUTHORIZED) {
                continue;
            }

            HttpResponse failureResponse = createResponseForCode(httpStatus);
            assertThrows(ServerResponseException.class, () -> assertSuccessfulResponse(failureResponse));
        }
    }

    @Test
    public void whenBuildProducerURIIsCalled_thenCorrectURIIsBuilt() {
        String expectedURIAsString = String.format("%s/%s/producer/v1/resource-events", TEST_SERVICE_URI, TEST_PLATFORM_NAME);

        assertEquals(expectedURIAsString, buildProducerURI(TEST_SERVICE_REGION).toString());
    }

    @Test
    public void whenBuildMatchedEventsURIIsCalled_thenCorrectURIIsBuilt() {
        Map<QueryParameter, String> testQueryParameters = Collections.singletonMap(QueryParameter.EVENT_TYPE, "TEST");
        String expectedURIAsString = String.format("%s/%s/consumer/v1/matched-events?eventType=TEST", TEST_SERVICE_URI, TEST_PLATFORM_NAME);

        assertEquals(expectedURIAsString, buildMatchedEventsURI(TEST_SERVICE_REGION, testQueryParameters).toString());
    }

    @Test
    public void whenBuildMatchedEventsURIForParticularEventIsCalled_thenCorrectURIIsBuilt() {
        Map<QueryParameter, String> testQueryParameters = Collections.singletonMap(QueryParameter.SEVERITY, "TEST");
        String expectedURIAsString = String.format("%s/%s/consumer/v1/matched-events/%s?severity=TEST", TEST_SERVICE_URI, TEST_PLATFORM_NAME, TEST_EVENT_ID);

        assertEquals(expectedURIAsString, buildMatchedEventsURI(TEST_SERVICE_REGION, TEST_EVENT_ID, testQueryParameters).toString());
    }

    @Test
    public void whenBuildUndeliveredEventsURIIsCalled_thenCorrectURIIsBuilt() {
        Map<QueryParameter, String> testQueryParameters = Collections.singletonMap(QueryParameter.EVENT_TYPE, "TEST");
        String expectedURIAsString = String.format("%s/%s/consumer/v1/undelivered-events?eventType=TEST", TEST_SERVICE_URI, TEST_PLATFORM_NAME);

        assertEquals(expectedURIAsString, buildUndeliveredEventsURI(TEST_SERVICE_REGION, testQueryParameters).toString());
    }

    @Test
    public void whenBuildUndeliveredURIForParticularEventIsCalled_thenCorrectURIIsBuilt() {
        Map<QueryParameter, String> testQueryParameters = Collections.singletonMap(QueryParameter.SEVERITY, "TEST");
        String expectedURIAsString = String.format("%s/%s/consumer/v1/undelivered-events/%s?severity=TEST", TEST_SERVICE_URI, TEST_PLATFORM_NAME, TEST_EVENT_ID);

        assertEquals(expectedURIAsString, buildUndeliveredEventsURI(TEST_SERVICE_REGION, TEST_EVENT_ID, testQueryParameters).toString());
    }

    private HttpResponse createResponseForCode(int httpStatusCode) {
        return new BasicHttpResponse(new BasicStatusLine(TEST_PROTOCOL_VERSION, httpStatusCode, TEST_REASON_PHRASE));
    }
}
