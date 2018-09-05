package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ReferenceDataController.class)
@AutoConfigureMockMvc(secure = false)
public class ReferenceDataControllerTest {
    public static final String URL_ROOM_TYPES = "/reference/room-types";
    public static final String URL_SESSION_TYPES = "/reference/session-types";
    public static final String URL_HEARING_TYPES = "/reference/hearing-types";
    public static final String URL_CASE_TYPES = "/reference/case-types";

    private static final String RESPONSE_BODY = "{string-response}";
    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    private EventsCommunicationService eventsCommunicationServiceMock;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getCaseTypes_returnsOk() throws Exception {
        when(eventsCommunicationServiceMock
            .makeCall(URL_CASE_TYPES, HttpMethod.GET).getBody())
            .thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(URL_CASE_TYPES))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Test
    public void getSessionTypes_returnsOk() throws Exception {
        when(eventsCommunicationServiceMock
            .makeCall(URL_SESSION_TYPES, HttpMethod.GET).getBody())
            .thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(URL_SESSION_TYPES))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Test
    public void getHearingTypes_returnsOk() throws Exception {
        when(eventsCommunicationServiceMock
            .makeCall(URL_HEARING_TYPES, HttpMethod.GET).getBody())
            .thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(URL_HEARING_TYPES))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Test
    public void getRoomTypes_returnsOk() throws Exception {
        when(eventsCommunicationServiceMock
            .makeCall(URL_ROOM_TYPES, HttpMethod.GET).getBody())
            .thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(URL_ROOM_TYPES))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Configuration
    @Import(ReferenceDataController.class)
    static class Config {
    }
}
