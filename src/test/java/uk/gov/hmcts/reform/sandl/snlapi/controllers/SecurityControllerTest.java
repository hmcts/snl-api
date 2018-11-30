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
import uk.gov.hmcts.reform.sandl.snlapi.repositories.UserRepository;
import uk.gov.hmcts.reform.sandl.snlapi.security.model.User;
import uk.gov.hmcts.reform.sandl.snlapi.security.requests.LoginRequest;
import uk.gov.hmcts.reform.sandl.snlapi.security.responses.JwtAuthenticationResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityControllerTest {

    private static final String OFFICER1 = "officer1";
    private static final String OFFICER_RESET_REQUIRED = "officer_reset_required";
    private static final String OFFICER_NOT_ENABLED = "officer_not_enabled";
    private static final String UNKNOWN_USER = "unknown";
    private static final String ENCODED_ASD_PASSWORD =
        "fa2fbd666a080874b7d83e611aa1a4e67f8c010fef1b19d7725725aa74f177a324b6c033e1abaf9e";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest(OFFICER1, "asd");

        mockMvc.perform(post("/security/signin")
            .content(objectMapper.writeValueAsString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void shouldNotLogin_whenUnknownUser() throws Exception {
        assertNull(userRepository.findByUsername(UNKNOWN_USER));

        LoginRequest loginRequest = new LoginRequest(UNKNOWN_USER, "password");

        mockMvc.perform(post("/security/signin")
            .content(objectMapper.writeValueAsString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andReturn();
    }

    @Test
    public void shouldNotLogin_whenWrongPassword() throws Exception {
        checkUser(OFFICER1, ENCODED_ASD_PASSWORD, true, false);

        LoginRequest loginRequest = new LoginRequest(OFFICER1, "wrong pass");

        mockMvc.perform(post("/security/signin")
            .content(objectMapper.writeValueAsString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andReturn();
    }

    @Test
    public void shouldNotLogin_whenCredentialsExpired() throws Exception {
        checkUser(OFFICER_RESET_REQUIRED, ENCODED_ASD_PASSWORD, true, true);

        LoginRequest loginRequest = new LoginRequest(OFFICER_RESET_REQUIRED, "asd");

        mockMvc.perform(post("/security/signin")
            .content(objectMapper.writeValueAsString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andReturn();
    }

    @Test
    public void shouldNotLogin_whenUserNotEnabled() throws Exception {
        checkUser(OFFICER_NOT_ENABLED, ENCODED_ASD_PASSWORD, false, false);

        LoginRequest loginRequest = new LoginRequest(OFFICER_NOT_ENABLED, "asd");

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
        LoginRequest loginRequest = new LoginRequest(OFFICER1, "asd");

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
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(OFFICER1));
    }

    private void checkUser(String username, String encodedPassword, boolean isEnabled, boolean isResetRequired) {
        User user = userRepository.findByUsername(username);
        assertEquals(user.getPassword(), encodedPassword);
        assertEquals(isEnabled, user.isEnabled());
        assertEquals(isResetRequired, user.isResetRequired());
    }
}
