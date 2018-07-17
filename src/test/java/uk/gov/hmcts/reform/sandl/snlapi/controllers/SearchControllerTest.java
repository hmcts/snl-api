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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = SearchController.class)
@AutoConfigureMockMvc(secure = false)
public class SearchControllerTest {

    private static String URL = "/search";

    @Configuration
    @Import(SearchController.class)
    static class Config { }

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    EventsCommunicationService eventsCommunicationServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void searchPossibleSessions_returnsOk() throws Exception {
        String from = "1";
        String to = "2";
        String duration = "3";

        String url = URL + String.format("?from=%s&to=%s&durationInSeconds=%s",
            from,
            to,
            duration);

        when(eventsCommunicationServiceMock.makeCall(url,
            HttpMethod.GET,
            from,
            to,
            String.valueOf(duration),
            null,
            null)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get(url))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void searchPossibleSessions_withJudgeIdReturnsOk() throws Exception {
        String from = "1";
        String to = "2";
        String duration = "3";
        String judgeId = "4";

        String url = URL + String.format("?from=%s&to=%s&durationInSeconds=%s&judge=%s",
            from,
            to,
            duration,
            judgeId);

        when(eventsCommunicationServiceMock.makeCall(url,
            HttpMethod.GET,
            from,
            to,
            String.valueOf(duration),
            judgeId,
            null)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get(url))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void searchPossibleSessions_withJudgeIdAndRoomIdReturnsOk() throws Exception {
        String from = "1";
        String to = "2";
        String duration = "3";
        String judgeId = "4";
        String roomId = "5";

        String url = URL + String.format("?from=%s&to=%s&durationInSeconds=%s&judge=%s&room=%s",
            from,
            to,
            duration,
            judgeId,
            roomId);

        when(eventsCommunicationServiceMock.makeCall(url,
            HttpMethod.GET,
            from,
            to,
            String.valueOf(duration),
            judgeId,
            roomId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get(url))
            .andExpect(status().isOk())
            .andReturn();
    }
}
