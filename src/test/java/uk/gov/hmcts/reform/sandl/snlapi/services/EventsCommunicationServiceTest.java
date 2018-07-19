package uk.gov.hmcts.reform.sandl.snlapi.services;

import org.apache.http.client.utils.CloneUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.sandl.snlapi.security.services.S2SAuthenticationService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
public class EventsCommunicationServiceTest {

    public static String EVENTS_URL = "eventsUrl";
    private HttpHeaders sampleHeaders;

    @Configuration
    @Import({EventsCommunicationService.class})
    static class Config { }

    @Autowired
    EventsCommunicationService ecs;

    @SuppressWarnings("PMD.UnusedPrivateField")
    @MockBean
    S2SAuthenticationService s2SAuthenticationService;

    @MockBean
    RestTemplate restTemplateMock;



    public EventsCommunicationServiceTest() {
    }

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(this.ecs, "eventsUrl", EVENTS_URL);
        sampleHeaders = new S2SAuthenticationService("FakeSecret1", 5000)
            .createAuthenticationHeader();
        when(s2SAuthenticationService.createAuthenticationHeader()).thenReturn(sampleHeaders);
    }

    @Test
    public void makeCall_restTemplateIsProperlyCalled() {
        ecs.makeCall("/endpoint", HttpMethod.GET);

        verify(restTemplateMock, times(1))
            .exchange(
                EVENTS_URL + "/endpoint",
                HttpMethod.GET,
                new HttpEntity<>(null, sampleHeaders),
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
        headers.set(
            S2SAuthenticationService.HEADER_NAME,
            sampleHeaders.getFirst(S2SAuthenticationService.HEADER_NAME)
        );
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
