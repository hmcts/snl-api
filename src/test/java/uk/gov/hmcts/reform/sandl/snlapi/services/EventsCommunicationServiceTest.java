package uk.gov.hmcts.reform.sandl.snlapi.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class EventsCommunicationServiceTest {

    public static String EVENTS_URL = "eventsUrl";

    @Configuration
    @Import(EventsCommunicationService.class)
    static class Config { }

    @Autowired
    EventsCommunicationService ecs;

    @MockBean
    RestTemplate restTemplateMock;

    public EventsCommunicationServiceTest() {
    }

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(this.ecs, "eventsUrl", EVENTS_URL);
    }

    @Test
    public void makeCall_restTemplateIsProperlyCalled() {
        ecs.makeCall("/endpoint", HttpMethod.GET);

        verify(restTemplateMock, times(1))
            .exchange(
                EVENTS_URL + "/endpoint",
                HttpMethod.GET,
                null,
                String.class
            );
    }

    @Test
    public void makePutCall_restTemplateIsProperlyCalled() {
        String body = "body";
        ecs.makePutCall("/endpoint", body);

        verify(restTemplateMock, times(1))
            .exchange(
                EVENTS_URL + "/endpoint",
                HttpMethod.PUT,
                getHttpEntityWithJsonContentType(body),
                String.class
            );
    }

    @Test
    public void makePostCall_restTemplateIsProperlyCalled() {
        String body = "body";
        ecs.makePostCall("/endpoint", body);

        verify(restTemplateMock, times(1))
            .exchange(
                EVENTS_URL + "/endpoint",
                HttpMethod.POST,
                getHttpEntityWithJsonContentType(body),
                String.class
            );
    }

    private HttpEntity getHttpEntityWithJsonContentType(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
