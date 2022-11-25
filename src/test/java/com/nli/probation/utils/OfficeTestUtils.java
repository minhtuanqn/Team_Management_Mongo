package com.nli.probation.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nli.probation.MockConstants;
import com.nli.probation.constant.EntityStatusEnum.OfficeStatusEnum;
import com.nli.probation.metamodel.OfficeEntity_;
import com.nli.probation.model.office.CreateOfficeModel;
import com.nli.probation.model.office.OfficeModel;
import com.nli.probation.model.office.UpdateOfficeModel;
import java.util.List;
import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;

public class OfficeTestUtils {

  /**
   * Create mock office
   *
   * @return mock office model
   */
  public static OfficeModel createOfficeModel() {
    OfficeModel officeModel = new OfficeModel();
    officeModel.setId(MockConstants.OFFICE_ID);
    officeModel.setName(MockConstants.OFFICE_NAME);
    officeModel.setLocation(MockConstants.OFFICE_LOCATION);
    officeModel.setStatus(MockConstants.OFFICE_STATUS);
    return officeModel;
  }

  /**
   * Create mock create office
   *
   * @return mock create office model
   */
  public static CreateOfficeModel createCreateOfficeModel() {
    CreateOfficeModel createOfficeModel = new CreateOfficeModel();
    createOfficeModel.setName(MockConstants.OFFICE_NAME);
    createOfficeModel.setLocation(MockConstants.OFFICE_LOCATION);
    return createOfficeModel;
  }

  /**
   * Create mock update office
   *
   * @return mock update office model
   */
  public static UpdateOfficeModel createUpdateOfficeModel() {
    UpdateOfficeModel updateOfficeModel = new UpdateOfficeModel();
    updateOfficeModel.setId(MockConstants.OFFICE_ID);
    updateOfficeModel.setName(MockConstants.OFFICE_NAME);
    updateOfficeModel.setLocation(MockConstants.OFFICE_LOCATION);
    updateOfficeModel.setStatus(MockConstants.OFFICE_STATUS);
    return updateOfficeModel;
  }

  /**
   * Campare two office model
   *
   * @param expected
   * @param actual
   * @return true or false
   */
  public static boolean compareTwoOffice(OfficeModel expected, OfficeModel actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getLocation(), actual.getLocation());
    assertEquals(expected.getStatus(), actual.getStatus());
    return true;
  }

  /**
   * Compare two list of office
   *
   * @param expectedList
   * @param actualList
   * @return true or false
   */
  public static boolean compareTwoOfficeList(List<OfficeModel> expectedList,
      List<OfficeModel> actualList) {
    assertEquals(expectedList.size(), actualList.size());
    for (OfficeModel officeModel : expectedList) {
      compareTwoOffice(officeModel, actualList.get(expectedList.indexOf(officeModel)));
    }
    return true;
  }

  /**
   * Create mock json object
   * @return ofice json
   * @throws JSONException
   */
  public static JSONObject createOfficeJsonObject() throws JSONException {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put(OfficeEntity_.ID, MockConstants.OFFICE_ID);
    jsonObject.put(OfficeEntity_.NAME, MockConstants.OFFICE_NAME);
    jsonObject.put(OfficeEntity_.LOCATION, MockConstants.OFFICE_LOCATION);
    jsonObject.put(OfficeEntity_.STATUS, MockConstants.OFFICE_STATUS);
    return jsonObject;
  }
}
