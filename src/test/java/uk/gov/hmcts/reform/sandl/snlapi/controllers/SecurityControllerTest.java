package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.gov.hmcts.reform.sandl.snlapi.security.requests.LoginRequest;
import uk.gov.hmcts.reform.sandl.snlapi.security.responses.JwtAuthenticationResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(secure = false)
public class SecurityControllerTest {

    private static final String USERNAME = "officer1";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest(USERNAME, "asd");

        mockMvc.perform(post("/security/signin")
            .content(objectMapper.writeValueAsString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void shouldNotLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("Wrong user", "wrong pass");

        mockMvc.perform(post("/security/signin")
            .content(objectMapper.writeValueAsString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andReturn();
    }


    @Test
    public void shouldThrowError_whenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/security/user")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andReturn();
    }

    @Test
    public void shouldReturnUserDetails_whenLoggedIn() throws Exception {
        LoginRequest loginRequest = new LoginRequest(USERNAME, "asd");

        MvcResult tokenResult = mockMvc.perform(post("/security/signin")
            .content(objectMapper.writeValueAsString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        JwtAuthenticationResponse token = objectMapper.readValue(tokenResult.getResponse().getContentAsString(),
            JwtAuthenticationResponse.class);

        mockMvc.perform(get("/security/user")
            .header("Authorization", "Bearer " + token.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(USERNAME));
    }
}
