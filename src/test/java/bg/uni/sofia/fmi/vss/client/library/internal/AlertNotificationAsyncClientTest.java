package bg.uni.sofia.fmi.vss.client.library.internal;

import bg.uni.sofia.fmi.vss.client.library.util.SynchronousExecutorService;
import bg.uni.sofia.fmi.vss.client.library.IAlertNotificationClient;
import bg.uni.sofia.fmi.vss.client.library.ICustomerResourceEventBuffer;
import bg.uni.sofia.fmi.vss.client.library.QueryParameter;
import bg.uni.sofia.fmi.vss.client.library.model.CustomerResourceEvent;
import bg.uni.sofia.fmi.vss.client.library.model.PagedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import static bg.uni.sofia.fmi.vss.client.library.QueryParameter.CORRELATION_ID;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AlertNotificationAsyncClientTest {

    private static final String TEST_EVENT_ID = "TEST_EVENT_ID";
    private static final Map<QueryParameter, String> TEST_QUERY_PARAMETERS = singletonMap(CORRELATION_ID, "test_correlation_id");


    private PagedResponse testPagedResponse;
    private ExecutorService testExecutorService;
    private CustomerResourceEvent testResourceEvent;
    private AlertNotificationAsyncClient classUnderTest;
    private ICustomerResourceEventBuffer testEventBuffer;
    private IAlertNotificationClient testAlertNotificationClient;

    @BeforeEach
    public void setUp() {
        testPagedResponse = mock(PagedResponse.class);
        testResourceEvent = mock(CustomerResourceEvent.class);
        testExecutorService = Mockito.spy(new SynchronousExecutorService());
        testAlertNotificationClient = mock(IAlertNotificationClient.class);
        testEventBuffer = Mockito.spy(new InMemoryCustomerResourceEventBuffer(100));
        classUnderTest = new AlertNotificationAsyncClient(testExecutorService, testEventBuffer, testAlertNotificationClient);
    }

    @Test
    public void givenThatExecutorServiceIsNull_whenConstructingClient_thenExceptionIsThrown() {
        assertThrows(NullPointerException.class, () -> {
            new AlertNotificationAsyncClient(null, testEventBuffer, testAlertNotificationClient);
        });
    }

    @Test
    public void givenThatEventBufferIsNull_whenConstructingClient_thenExceptionIsThrown() {
        assertThrows(NullPointerException.class, () -> {
            new AlertNotificationAsyncClient(testExecutorService, null, testAlertNotificationClient);
        });
    }

    @Test
    public void givenThatAlertNotificationClientIsNull_whenConstructingClient_thenExceptionIsThrown() {
        assertThrows(NullPointerException.class, () -> {
            new AlertNotificationAsyncClient(testExecutorService, testEventBuffer, null);
        });
    }

    @Test
    public void whenGetExecutorServiceIsCalled_thenCorrectResultIsReturned() {
        assertEquals(testExecutorService, classUnderTest.getExecutorService());
    }

    @Test
    public void whenGetEventBufferIsCalled_thenCorrectResultIsReturned() {
        assertEquals(testEventBuffer, classUnderTest.getEventBuffer());
    }

    @Test
    public void whenGetAlertNotificationClientIsCalled_thenCorrectResultIsReturned() {
        assertEquals(testAlertNotificationClient, classUnderTest.getAlertNotificationClient());
    }

    @Test
    public void whenSendEventIsCalled_thenEventIsScheduledForSending() throws Exception {
        doReturn(testResourceEvent).when(testAlertNotificationClient).sendEvent(testResourceEvent);

        assertSame(testResourceEvent, classUnderTest.sendEvent(testResourceEvent).get());

        InOrder executionOrder = inOrder(testEventBuffer, testAlertNotificationClient);
        executionOrder.verify(testEventBuffer).write(testResourceEvent);
        executionOrder.verify(testEventBuffer).read(any(UUID.class));
        executionOrder.verify(testAlertNotificationClient).sendEvent(testResourceEvent);
    }

    @Test
    public void whenGetMatchedEventsIsCalled_thenCorrectRequestIsScheduledForSending() throws Exception {
        doReturn(testPagedResponse).when(testAlertNotificationClient).getMatchedEvents(TEST_QUERY_PARAMETERS);

        assertSame(testPagedResponse, classUnderTest.getMatchedEvents(TEST_QUERY_PARAMETERS).get());

        verify(testAlertNotificationClient).getMatchedEvents(TEST_QUERY_PARAMETERS);
    }

    @Test
    public void whenGetMatchedEventIsCalled_thenCorrectRequestIsScheduledForSending() throws Exception {
        doReturn(testPagedResponse).when(testAlertNotificationClient).getMatchedEvent(TEST_EVENT_ID, TEST_QUERY_PARAMETERS);

        assertSame(testPagedResponse, classUnderTest.getMatchedEvent(TEST_EVENT_ID, TEST_QUERY_PARAMETERS).get());

        verify(testAlertNotificationClient).getMatchedEvent(TEST_EVENT_ID, TEST_QUERY_PARAMETERS);
    }

    @Test
    public void whenGetUndeliveredEventsIsCalled_thenCorrectRequestIsScheduledForSending() throws Exception {
        doReturn(testPagedResponse).when(testAlertNotificationClient).getUndeliveredEvents(TEST_QUERY_PARAMETERS);

        assertSame(testPagedResponse, classUnderTest.getUndeliveredEvents(TEST_QUERY_PARAMETERS).get());

        verify(testAlertNotificationClient).getUndeliveredEvents(TEST_QUERY_PARAMETERS);
    }

    @Test
    public void whenGetUndeliveredEventIsCalled_thenCorrectRequestIsScheduledForSending() throws Exception {
        doReturn(testPagedResponse).when(testAlertNotificationClient).getUndeliveredEvent(TEST_EVENT_ID, TEST_QUERY_PARAMETERS);

        assertSame(testPagedResponse, classUnderTest.getUndeliveredEvent(TEST_EVENT_ID, TEST_QUERY_PARAMETERS).get());

        verify(testAlertNotificationClient).getUndeliveredEvent(TEST_EVENT_ID, TEST_QUERY_PARAMETERS);
    }

    @Test
    public void whenShutdownIsCalled_thenExecutorServiceIsStopped() {
        classUnderTest.shutdown();

        verify(testExecutorService).shutdownNow();
    }
}
