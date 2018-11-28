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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HearingController.class)
@AutoConfigureMockMvc(secure = false)
public class HearingControllerTest {

    private static final String RESPONSE_BODY = "A";
    private static final String REQUEST_BODY = "A";
    private static final String HEARINGS_URL = "/hearing";

    @Configuration
    @Import(HearingController.class)
    static class Config { }

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    EventsCommunicationService eventsCommunicationServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    HearingController hearingController;

    @Test
    public void getHearingParts_returnsOk() throws Exception {
        final String uuid = "02941d80-eeba-4ba1-8d6e-4b1255448736";
        when(eventsCommunicationServiceMock.makeCall(HEARINGS_URL + "/{id}", HttpMethod.GET, uuid)
            .getBody())
            .thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(HEARINGS_URL + "/" + uuid))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Test
    public void assignHearingToSession_withCorrectParametersReturnsOk() throws Exception {
        String hearingPartId = "hearing-id";
        String sessionId = "session-id";
        when(eventsCommunicationServiceMock
            .makePutCall(HEARINGS_URL + "/{hearingId}", sessionId, hearingPartId))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put(HEARINGS_URL + "/" + hearingPartId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(sessionId))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void getHearingByIdWithSessions_returnsWhatEventsReturn() throws Exception {
        final String id = "02941d80-eeba-4ba1-8d6e-4b1255448736";
        when(eventsCommunicationServiceMock.makeCall(HEARINGS_URL + "/{id}/with-sessions", HttpMethod.GET, id)
            .getBody())
            .thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(HEARINGS_URL + "/" + id + "/with-sessions"))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Test
    public void searchHearings_returnsList() throws Exception {
        when(eventsCommunicationServiceMock
            .makePostCall(HEARINGS_URL, REQUEST_BODY))
            .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));

        mockMvc.perform(post(HEARINGS_URL).contentType(MediaType.APPLICATION_JSON).content(REQUEST_BODY))
            .andExpect(content().string(RESPONSE_BODY))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void searchHearings_returnsListWithPaging() throws Exception {
        final String url = HEARINGS_URL + "?page=0&size=5";
        when(eventsCommunicationServiceMock
            .makePostCall(url, REQUEST_BODY))
            .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(REQUEST_BODY))
            .andExpect(content().string(RESPONSE_BODY))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void unlistHearing_withCorrectParametersReturnsOk() throws Exception {
        String unlistUrl = HEARINGS_URL + "/unlist";
        when(eventsCommunicationServiceMock
            .makePutCall(unlistUrl, REQUEST_BODY))
            .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));

        mockMvc.perform(put(unlistUrl).contentType(MediaType.APPLICATION_JSON).content(REQUEST_BODY))
            .andExpect(content().string(RESPONSE_BODY))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void adjournHearing_withCorrectParametersReturnsOk() throws Exception {
        String adjournUrl = HEARINGS_URL + "/adjourn";
        when(eventsCommunicationServiceMock
            .makePutCall(adjournUrl, REQUEST_BODY))
            .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));

        mockMvc.perform(put(adjournUrl).contentType(MediaType.APPLICATION_JSON).content(REQUEST_BODY))
            .andExpect(content().string(RESPONSE_BODY))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void withdrawHearing_withCorrectParametersReturnsOk() throws Exception {
        String withdrawUrl = HEARINGS_URL + "/withdraw";
        when(eventsCommunicationServiceMock
            .makePutCall(withdrawUrl, REQUEST_BODY))
            .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));

        mockMvc.perform(put(withdrawUrl).contentType(MediaType.APPLICATION_JSON).content(REQUEST_BODY))
            .andExpect(content().string(RESPONSE_BODY))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void vacateHearing_withCorrectParametersReturnsOk() throws Exception {
        String vacateUrl = HEARINGS_URL + "/vacate";
        when(eventsCommunicationServiceMock
            .makePutCall(vacateUrl, REQUEST_BODY))
            .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));

        mockMvc.perform(put(vacateUrl).contentType(MediaType.APPLICATION_JSON).content(REQUEST_BODY))
            .andExpect(content().string(RESPONSE_BODY))
            .andExpect(status().isOk())
            .andReturn();
    }
}
