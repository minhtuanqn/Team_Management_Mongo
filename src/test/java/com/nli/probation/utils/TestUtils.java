package com.nli.probation.utils;

import com.nli.probation.constant.EntityStatusEnum.OfficeStatusEnum;
import com.nli.probation.model.office.CreateOfficeModel;
import com.nli.probation.model.office.OfficeModel;
import org.junit.jupiter.api.Assertions;

public class TestUtils {

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
   * Campare two office model
   * @param expected
   * @param actual
   * @return true or false
   */
  public static boolean compareTwoOffice(OfficeModel expected, OfficeModel actual) {
    Assertions.assertEquals(expected.getId(), actual.getId());
    Assertions.assertEquals(expected.getName(), actual.getName());
    Assertions.assertEquals(expected.getLocation(), actual.getLocation());
    Assertions.assertEquals(expected.getStatus(), actual.getStatus());
    return true;
  }
}
