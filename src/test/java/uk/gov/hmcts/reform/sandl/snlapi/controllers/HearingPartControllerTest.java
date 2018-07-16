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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HearingPartController.class)
@AutoConfigureMockMvc(secure=false)
public class HearingPartControllerTest {

    public static String HEARINGS_URL = "/hearing-part";

    @Configuration
    @Import(HearingPartController.class)
    static class Config { }

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    EventsCommunicationService eventsCommunicationServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    HearingPartController hpc;

    @Test
    public void getHearingParts_returnsOk() throws Exception {
        String responseBody = "A";
        when(eventsCommunicationServiceMock.makeCall(HEARINGS_URL, HttpMethod.GET).getBody()).thenReturn(responseBody);

        mockMvc.perform(get(HEARINGS_URL))
            .andExpect(status().isOk())
            .andExpect(content().string(responseBody))
            .andReturn();
    }

    @Test
    public void upsertHearingPart_withCorrectParametersReturnsOk() throws Exception {
        String requestBody = "A";
        when(eventsCommunicationServiceMock.makePutCall(HEARINGS_URL, requestBody)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put(HEARINGS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andReturn();
    }
    @Test
    public void upsertHearingPart_withNoRequestBodyReturnsBadRequest() throws Exception {
        mockMvc.perform(put(HEARINGS_URL)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    public void assignHearingPartToSession_withCorrectParametersReturnsOk() throws Exception {
        String hearingPartId = "hearing-part-id";
        String sessionId = "session-id";
        when(eventsCommunicationServiceMock.makePutCall(HEARINGS_URL + "/{hearingPartId}", sessionId, hearingPartId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put(HEARINGS_URL + "/" + hearingPartId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(sessionId))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void assignHearingPartToSession_withNoRequestBodyReturnsBadRequest() throws Exception {
        String hearingPartId = "hearing-part-id";

        mockMvc.perform(put(HEARINGS_URL + "/" + hearingPartId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }
}
