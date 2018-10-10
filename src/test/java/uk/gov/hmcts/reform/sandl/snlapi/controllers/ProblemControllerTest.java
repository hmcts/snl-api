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
@WebMvcTest(controllers = ProblemController.class)
@AutoConfigureMockMvc(secure = false)
public class ProblemControllerTest {

    private static final String PAGED_PROBLEMS_URL = "/problems?page=1&size=20";
    private static final String PROBLEMS_URL = "/problems";
    private static final String RESPONSE_BODY = "problem";

    @Configuration
    @Import(ProblemController.class)
    static class Config { }

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    private EventsCommunicationService eventsCommunicationServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getProblems_returnsOk() throws Exception {
        when(eventsCommunicationServiceMock.makeCall(PROBLEMS_URL, HttpMethod.GET).getBody()).thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(PROBLEMS_URL))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Test
    public void getPagedProblems_returnsOk() throws Exception {
        when(eventsCommunicationServiceMock.makeCall(PAGED_PROBLEMS_URL, HttpMethod.GET).getBody()).thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(PAGED_PROBLEMS_URL))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Test
    public void getProblemsByEntity_returnsOk() throws Exception {
        final String entityParam = "/by-entity-id?id=12";

        when(eventsCommunicationServiceMock.makeCall(PROBLEMS_URL + "/by-entity-id?id={id}",
            HttpMethod.GET, "12").getBody()).thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(PROBLEMS_URL + entityParam))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

    @Test
    public void getProblemsByUserTransaction_returnsOk() throws Exception {
        final String entityParam = "/by-user-transaction-id?id=12";

        when(eventsCommunicationServiceMock.makeCall(PROBLEMS_URL + "/by-user-transaction-id?id={id}",
            HttpMethod.GET, "12").getBody()).thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(PROBLEMS_URL + entityParam))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }
}
