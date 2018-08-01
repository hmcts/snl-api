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
@WebMvcTest(controllers = ReportController.class)
@AutoConfigureMockMvc(secure = false)
public class ReportControllerTest {

    private static final String REPORT_URL = "/report";
    private static final String RESPONSE_BODY = "report";

    @Configuration
    @Import(ReportController.class)
    static class Config { }

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    private EventsCommunicationService eventsCommunicationServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUnlistedHearingsReport_returnsOk() throws Exception {
        final String unlistedUrl = "/unlisted-hearing-requests";

        when(eventsCommunicationServiceMock.makeCall(REPORT_URL + unlistedUrl, HttpMethod.GET)
            .getBody()).thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(REPORT_URL + unlistedUrl))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }


    @Test
    public void getListedHearingsReport_returnsOk() throws Exception {
        final String listedUrl = "/listed-hearing-requests";
        final String startDate = "20-03-2018";
        final String endDate = "22-03-2018";

        when(eventsCommunicationServiceMock.makeCall(REPORT_URL + listedUrl
            + "?startDate={startDate}&endDate={endDate}", HttpMethod.GET, startDate, endDate)
            .getBody()).thenReturn(RESPONSE_BODY);

        mockMvc.perform(get(REPORT_URL + listedUrl + "?startDate=" + startDate + "&endDate=" + endDate))
            .andExpect(status().isOk())
            .andExpect(content().string(RESPONSE_BODY))
            .andReturn();
    }

}
