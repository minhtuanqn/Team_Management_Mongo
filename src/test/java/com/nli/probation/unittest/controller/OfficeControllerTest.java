package com.nli.probation.unittest.controller;

import static com.nli.probation.utils.OfficeTestUtils.createOfficeJsonObject;
import static com.nli.probation.utils.OfficeTestUtils.createOfficeModel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nli.probation.MockConstants;
import com.nli.probation.controller.OfficeController;
import com.nli.probation.service.OfficeService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
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

  /**
   * When find office by id then return information of existed office
   * @throws Exception
   */
  @Test
  void when_findOfficeById_theReturnOffice() throws Exception {
    when(officeService.findOfficeById(MockConstants.OFFICE_ID)).thenReturn(createOfficeModel());
    this.mockMvc = MockMvcBuilders.standaloneSetup(new OfficeController(officeService)).build();
    mockMvc.perform(get("/offices/{id}", MockConstants.OFFICE_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JSONObject().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value(MockConstants.SUCSESS_MESSAGE))
        .andExpect(jsonPath("$.statusCode").value(MockConstants.SUCCESS_STATUS_CODE))
        .andExpect(jsonPath("$.data.id").value(MockConstants.OFFICE_ID))
        .andExpect(jsonPath("$.data.name").value(MockConstants.OFFICE_NAME))
        .andExpect(jsonPath("$.data.location").value(MockConstants.OFFICE_LOCATION))
        .andExpect(jsonPath("$.data.status").value(MockConstants.OFFICE_STATUS));
  }

  /**
   * When delete existed office then delete successfully
   * @throws Exception
   */
  @Test
  public void when_deleteExistedOffice_thenSuccessfullyDelete() throws Exception {
    when(officeService.deleteOfficeById(1)).thenReturn(createOfficeModel());
    this.mockMvc = MockMvcBuilders.standaloneSetup(new OfficeController(officeService)).build();
    mockMvc.perform(delete("/offices/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value(MockConstants.SUCSESS_MESSAGE))
        .andExpect(jsonPath("$.statusCode").value(MockConstants.SUCCESS_STATUS_CODE))
        .andExpect(jsonPath("$.data.id").value(MockConstants.OFFICE_ID))
        .andExpect(jsonPath("$.data.name").value(MockConstants.OFFICE_NAME))
        .andExpect(jsonPath("$.data.location").value(MockConstants.OFFICE_LOCATION))
        .andExpect(jsonPath("$.data.status").value(MockConstants.OFFICE_STATUS));
  }

}
