package com.nli.probation;

import com.nli.probation.constant.EntityStatusEnum.OfficeStatusEnum;

public class MockConstants {
  private MockConstants() {

  }

  public static final int OFFICE_ID = 1;
  public static final String OFFICE_NAME = "Tan Vien";
  public static final String OFFICE_LOCATION = "Tan Binh";
  public static final int OFFICE_STATUS = OfficeStatusEnum.ACTIVE.ordinal();
  public static final int NOT_FOUND_OFFICE_ID = Integer.MAX_VALUE;
  public static final String SEARCH_VALUE = "";
  public static final int INDEX = 0;
  public static final int LIMIT = 1;
  public static final String SORT_BY = "id";
  public static final String SORT_TYPE = "asc";
  public static final int TOTAL_RESULT = 1;
  public static final int TOTAL_PAGE = 1;
}
