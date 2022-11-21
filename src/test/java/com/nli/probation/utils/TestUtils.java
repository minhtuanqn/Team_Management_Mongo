package com.nli.probation.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nli.probation.model.ResourceModel;
import java.util.List;

public class TestUtils <T> {

  /**
   * Create mock resource model for testing
   * @param searchValue
   * @param sortType
   * @param sortBy
   * @param totalResult
   * @param totalPage
   * @param index
   * @param limit
   * @param modelList
   * @return resource model
   */
  public ResourceModel<T> createResourceModel(String searchValue, String sortType, String sortBy,
      int totalResult, int totalPage, int index, int limit, List<T> modelList) {
    ResourceModel<T> resourceModel = new ResourceModel<>();
    resourceModel.setSearchText(searchValue);
    resourceModel.setSortType(sortType);
    resourceModel.setSortBy(sortBy);
    resourceModel.setTotalResult(totalResult);
    resourceModel.setTotalPage(totalPage);
    resourceModel.setLimit(limit);
    resourceModel.setIndex(index);
    resourceModel.setData(modelList);
    return resourceModel;
  }

  /**
   * Compare information of two resource model
   * @param expected
   * @param actual
   * @return true or false
   */
  public boolean compareTwoResourceInformation(ResourceModel<T> expected,
      ResourceModel<T> actual) {
    assertEquals(expected.getSearchText(), actual.getSearchText());
    assertEquals(expected.getIndex(), actual.getIndex());
    assertEquals(expected.getLimit(), actual.getLimit());
    assertEquals(expected.getTotalPage(), actual.getTotalPage());
    assertEquals(expected.getTotalResult(), actual.getTotalResult());
    assertEquals(expected.getSortType(), actual.getSortType());
    assertEquals(expected.getSortBy(), actual.getSortBy());
    return true;
  }
}
