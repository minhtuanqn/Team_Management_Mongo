package com.nli.probation.unittest.controller;

import static com.nli.probation.utils.OfficeTestUtils.createOfficeJsonObject;
import static com.nli.probation.utils.OfficeTestUtils.createOfficeModel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nli.probation.MockConstants;
import com.nli.probation.controller.OfficeController;
import com.nli.probation.service.OfficeService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class OfficeControllerTest {

  MockMvc mockMvc;

  OfficeService officeService = Mockito.mock(OfficeService.class);

  /**
   * Create new office and saved successfully
   *
   * @throws Exception
   */
  @Test
  void when_saveOfficeByPostMethod_thenSuccessfullySave() throws Exception {
    when(officeService.createOffice(any())).thenReturn(createOfficeModel());
    this.mockMvc = MockMvcBuilders.standaloneSetup(new OfficeController(officeService)).build();
    JSONObject paramJson = createOfficeJsonObject();
    mockMvc.perform(post("/offices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(paramJson.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value(MockConstants.SUCSESS_MESSAGE))
        .andExpect(jsonPath("$.statusCode").value(MockConstants.SUCCESS_STATUS_CODE))
        .andExpect(jsonPath("$.data.id").value(MockConstants.OFFICE_ID))
        .andExpect(jsonPath("$.data.name").value(MockConstants.OFFICE_NAME))
        .andExpect(jsonPath("$.data.location").value(MockConstants.OFFICE_LOCATION))
        .andExpect(jsonPath("$.data.status").value(MockConstants.OFFICE_STATUS));
  }

}
