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
import org.springframework.security.access.annotation.Secured;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = SessionController.class)
@AutoConfigureMockMvc(secure = false)
public class SessionControllerTest {

    private static String URL = "/sessions";

    @Configuration
    @Import(SessionController.class)
    static class Config { }

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    EventsCommunicationService eventsCommunicationServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void controllerIsAnnotatedAsSecuredWithProperRoles() {
        assertThat(SessionController.class.getAnnotation(Secured.class).value()).isEqualTo(new String[]{"ROLE_USER"});
    }

    @Test
    public void getSessions_returnsOk() throws Exception {
        String responseBody = "A";
        String date = "any date string";

        when(eventsCommunicationServiceMock
            .makeCall(URL + "?date={date}", HttpMethod.GET, date)
            .getBody())
            .thenReturn(responseBody);

        mockMvc.perform(get(URL + "?date=" + date))
            .andExpect(status().isOk())
            .andExpect(content().string(responseBody))
            .andReturn();
    }

    @Test
    public void getSessionsById_returnsOk() throws Exception {
        String responseBody = "A";
        String id = "any id";

        when(eventsCommunicationServiceMock
            .makeCall(URL + "/{id}", HttpMethod.GET, id)
            .getBody())
            .thenReturn(responseBody);

        mockMvc.perform(get(URL + "/" + id))
            .andExpect(status().isOk())
            .andExpect(content().string(responseBody))
            .andReturn();
    }

    @Test
    public void getSessions_betweenDatesReturnsOk() throws Exception {
        String responseBody = "A";
        String date = "any date string";
        String endDate = "any date string";

        when(eventsCommunicationServiceMock
            .makeCall(URL + "?startDate={startDate}&endDate={endDate}", HttpMethod.GET, date, endDate)
            .getBody())
            .thenReturn(responseBody);

        mockMvc.perform(get(URL + "?startDate=" + date + "&endDate=" + endDate))
            .andExpect(status().isOk())
            .andExpect(content().string(responseBody))
            .andReturn();
    }

    @Test
    public void insertSession_withCorrectParametersReturnsOk() throws Exception {
        String requestBody = "A";
        when(eventsCommunicationServiceMock.makePutCall(URL, requestBody))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void updateSession_withCorrectParametersReturnsOk() throws Exception {
        String requestBody = "A";
        when(eventsCommunicationServiceMock.makePutCall(URL + "/update", requestBody))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put(URL + "/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void getJudgeDiary_withCorrectParametersReturnsOk() throws Exception {
        String finalUrl = URL + "/judge-diary?judge={judgeUsername}&startDate={startDate}&endDate={endDate}";
        String finalUrlPattern = URL + "/judge-diary?judge={0}&startDate={1}&endDate={2}";
        String judgeUsername = "A";
        String startDate = "B";
        String endDate = "C";

        when(eventsCommunicationServiceMock.makeCall(finalUrl, HttpMethod.GET, judgeUsername, startDate, endDate))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get(MessageFormat.format(finalUrlPattern, judgeUsername, startDate, endDate)))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void upsertHearingPart_withNoRequestBodyReturnsBadRequest() throws Exception {
        mockMvc.perform(put(URL)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }
}
