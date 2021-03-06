package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ActivityLogController.class)
@AutoConfigureMockMvc(secure = false)
public class ActivityLogControllerTest {
    private static final String URL = "/activity-log";
    private static final String RESPONSE_BODY = "A";

    @Autowired
    private MockMvc mockMvc;

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    private EventsCommunicationService eventsCommunicationServiceMock;

    @Test
    public void getActivityLog_returnsOk() throws Exception {
        final UUID entityId = UUID.randomUUID();
        when(eventsCommunicationServiceMock.makeCall(URL + "/{entityId}", HttpMethod.GET, entityId.toString())
            .getBody())
            .thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(URL + "/" + entityId))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }
}
