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
import uk.gov.hmcts.reform.sandl.snlapi.security.model.User;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserTransactionController.class)
@AutoConfigureMockMvc(secure = false)
public class UserTransactionControllerTest {

    private static String URL = "/user-transaction";

    @Configuration
    @Import(UserTransactionController.class)
    static class Config { }

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    EventsCommunicationService eventsCommunicationServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUserTransaction_returnsOk() throws Exception {
        String responseBody = "A";
        String id = "1";
        when(eventsCommunicationServiceMock.makeCall(URL + "/{id}", HttpMethod.GET, id).getBody()).thenReturn(responseBody);

        mockMvc.perform(get(URL + "/" + id))
            .andExpect(status().isOk())
            .andExpect(content().string(responseBody))
            .andReturn();
    }

    @Test
    public void commitUserTransaction_returnsOk() throws Exception {
        String responseBody = "A";
        String id = "1";
        when(eventsCommunicationServiceMock.makePostCall(URL + "/{id}/commit", null, id).getBody()).thenReturn(responseBody);

        mockMvc.perform(post(URL + "/" + id + "/commit"))
            .andExpect(status().isOk())
            .andExpect(content().string(responseBody))
            .andReturn();
    }

    @Test
    public void rollbackUserTransaction_returnsOk() throws Exception {
        String responseBody = "A";
        String id = "1";
        when(eventsCommunicationServiceMock.makePostCall(URL + "/{id}/rollback", null, id).getBody()).thenReturn(responseBody);

        mockMvc.perform(post(URL + "/" + id + "/rollback"))
            .andExpect(status().isOk())
            .andExpect(content().string(responseBody))
            .andReturn();
    }
}
