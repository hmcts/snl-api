package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.hmcts.reform.sandl.snlapi.config.WebSecurityConfig;
import uk.gov.hmcts.reform.sandl.snlapi.security.CustomUserDetailsService;
import uk.gov.hmcts.reform.sandl.snlapi.security.JwtAuthenticationEntryPoint;
import uk.gov.hmcts.reform.sandl.snlapi.security.JwtAuthenticationFilter;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HearingPartController.class)
@AutoConfigureMockMvc(secure=false)
public class HearingPartControllerTest {

    private static final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        Charset.forName("utf8"));

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
    public void getHearingParts_restTemplateIsProperlyCalled() throws Exception {
        String responseBody = "A";
        when(eventsCommunicationServiceMock.makeCall("/hearing-part", HttpMethod.GET).getBody()).thenReturn(responseBody);

        mockMvc.perform(get("/hearing-part"))
            .andExpect(status().isOk())
            .andExpect(content().string(responseBody))
            .andReturn();
    }

    @Test
    public void upsertHearingPart_restTemplateIsProperlyCalled() throws Exception {
        String requestBody = "A";
        when(eventsCommunicationServiceMock.makePutCall("/hearing-part", requestBody)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put("/hearing-part")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void assignHearingPartToSession_restTemplateIsProperlyCalled() throws Exception {
        String hearingPartId = "hearing-part-id";
        String sessionId = "session-id";
        when(eventsCommunicationServiceMock.makePutCall("/hearing-part/{hearingPartId}", sessionId, hearingPartId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put("/hearing-part/" + hearingPartId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(sessionId))
            .andExpect(status().isOk())
            .andReturn();
    }

}
