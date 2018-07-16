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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PersonController.class)
@AutoConfigureMockMvc(secure = false)
public class PersonControllerTest {

    private static String URL = "/person";

    @Configuration
    @Import(PersonController.class)
    static class Config { }

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    EventsCommunicationService eventsCommunicationServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getRooms_returnsOk() throws Exception {
        String responseBody = "A";
        when(eventsCommunicationServiceMock.makeCall(URL, HttpMethod.GET).getBody()).thenReturn(responseBody);

        mockMvc.perform(get(URL))
            .andExpect(status().isOk())
            .andExpect(content().string(responseBody))
            .andReturn();
    }

    @Test
    public void getJudges_withCorrectParametersReturnsOk() throws Exception {
        String type = "judge";
        when(eventsCommunicationServiceMock.makeCall(URL + "?personType={personType}",
            HttpMethod.GET,
            type)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get(URL + "?personType=" + type)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    }
}
