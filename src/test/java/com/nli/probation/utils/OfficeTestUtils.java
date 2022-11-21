package com.nli.probation.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nli.probation.constant.EntityStatusEnum.OfficeStatusEnum;
import com.nli.probation.model.office.CreateOfficeModel;
import com.nli.probation.model.office.OfficeModel;
import com.nli.probation.model.office.UpdateOfficeModel;
import java.util.List;

public class OfficeTestUtils {

  /**
   * Create mock office
   * @return mock office model
   */
  public static OfficeModel createOfficeModel() {
    OfficeModel officeModel = new OfficeModel();
    officeModel.setId(1);
    officeModel.setName("Tan Vien");
    officeModel.setLocation("Tan Binh");
    officeModel.setStatus(OfficeStatusEnum.ACTIVE.ordinal());
    return officeModel;
  }

  /**
   * Create mock create office
   * @return mock create office model
   */
  public static CreateOfficeModel createCreateOfficeModel() {
    CreateOfficeModel createOfficeModel = new CreateOfficeModel();
    createOfficeModel.setName("Tan Vien");
    createOfficeModel.setLocation("Tan Binh");
    return createOfficeModel;
  }

  /**
   * Create mock update office
   * @return mock update office model
   */
  public static UpdateOfficeModel createUpdateOfficeModel() {
    UpdateOfficeModel updateOfficeModel = new UpdateOfficeModel();
    updateOfficeModel.setId(1);
    updateOfficeModel.setName("Tan Vien");
    updateOfficeModel.setLocation("Tan Binh");
    updateOfficeModel.setStatus(OfficeStatusEnum.ACTIVE.ordinal());
    return updateOfficeModel;
  }

  /**
   * Campare two office model
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
   * @param expectedList
   * @param actualList
   * @return true or false
   */
  public static boolean compareTwoOfficeList(List<OfficeModel> expectedList, List<OfficeModel> actualList) {
    assertEquals(expectedList.size(), actualList.size());
    for (OfficeModel officeModel : expectedList) {
      compareTwoOffice(officeModel, actualList.get(expectedList.indexOf(officeModel)));
    }
    return true;
  }
}
