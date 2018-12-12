package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import lombok.val;
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
@WebMvcTest(controllers = HearingPartController.class)
@AutoConfigureMockMvc(secure = false)
public class HearingPartControllerTest {

    private static String HEARINGS_URL = "/hearing-part";
    private static String HEARINGS_WITH_IS_LISTED_PARAM_URL = "/hearing-part?isListed=false";
    private static String HEARINGS_WITH_WRONG_LISTED_PARAM_URL = "/hearing-part?isListed=";
    private static String RESPONSE_BODY = "A";

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
        when(eventsCommunicationServiceMock.makeCall(HEARINGS_URL, HttpMethod.GET).getBody()).thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(HEARINGS_URL))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Test
    public void getHearingPartsWithListedParam_returnsOk() throws Exception {
        when(eventsCommunicationServiceMock
            .makeCall(HEARINGS_WITH_IS_LISTED_PARAM_URL, HttpMethod.GET)
            .getBody()
        ).thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(HEARINGS_WITH_IS_LISTED_PARAM_URL))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Test
    public void getHearingPartById_returnsOk() throws Exception {
        when(eventsCommunicationServiceMock
            .makeCall(HEARINGS_URL + "/{id}", HttpMethod.GET, "1")
            .getBody()
        ).thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(HEARINGS_URL + "/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Test
    public void getHearingPartsWithWrongListedParam_passRequestWithoutParam() throws Exception {
        when(eventsCommunicationServiceMock
            .makeCall(HEARINGS_URL, HttpMethod.GET)
            .getBody()
        ).thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(HEARINGS_WITH_WRONG_LISTED_PARAM_URL))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Test
    public void createHearingPartAction_withCorrectParametersReturnsOk() throws Exception {
        when(eventsCommunicationServiceMock.makePutCall(HEARINGS_URL + "/create", RESPONSE_BODY))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put(HEARINGS_URL + "/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(RESPONSE_BODY))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void updateHearingPartAction_withCorrectParametersReturnsOk() throws Exception {
        when(eventsCommunicationServiceMock.makePutCall(HEARINGS_URL + "/update", RESPONSE_BODY))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put(HEARINGS_URL + "/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(RESPONSE_BODY))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void amendScheduledListing_withCorrectParametersReturnsOk() throws Exception {
        when(eventsCommunicationServiceMock.makePutCall(HEARINGS_URL + "/amend-scheduled-listing",
            RESPONSE_BODY))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put(HEARINGS_URL + "/amend-scheduled-listing")
            .contentType(MediaType.APPLICATION_JSON)
            .content(RESPONSE_BODY))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void upsertHearingPart_withCorrectParametersReturnsOk() throws Exception {
        when(eventsCommunicationServiceMock.makePutCall(HEARINGS_URL, RESPONSE_BODY))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put(HEARINGS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(RESPONSE_BODY))
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
        when(eventsCommunicationServiceMock
            .makePutCall(HEARINGS_URL + "/{hearingPartId}", sessionId, hearingPartId))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

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

    @Test
    public void deleteHearingPart_returnsWhatEventsReturn() throws Exception {
        val result = "r";
        val url = HEARINGS_URL +  "/delete";
        val id = "id";

        when(eventsCommunicationServiceMock
            .makePostCall(url, "id"))
            .thenReturn(new ResponseEntity<>(result, HttpStatus.OK));

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content("id"))
            .andExpect(content().string(result))
            .andExpect(status().isOk())
            .andReturn();
    }
}
