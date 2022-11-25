package com.nli.probation.utils;

import static com.nli.probation.utils.OfficeTestUtils.createOfficeModel;
import static com.nli.probation.utils.RoleTestUtils.createRoleModel;
import static com.nli.probation.utils.TeamTestUtils.createTeamModel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nli.probation.MockConstants;
import com.nli.probation.entity.UserAccountEntity;
import com.nli.probation.model.useraccount.CreateUserAccountModel;
import com.nli.probation.model.useraccount.UpdateUserAccountModel;
import com.nli.probation.model.useraccount.UserAccountModel;

public class UserAccountTestUtils {

  /**
   * Create mock user account
   *
   * @return mock user account model
   */
  public static UserAccountModel createUserAccountModel() {
    UserAccountModel userAccountModel = new UserAccountModel();
    userAccountModel.setId(MockConstants.ACCOUNT_ID);
    userAccountModel.setName(MockConstants.ACCOUNT_NAME);
    userAccountModel.setEmail(MockConstants.ACCOUNT_EMAIL);
    userAccountModel.setPhone(MockConstants.ACCOUNT_PHONE);
    userAccountModel.setStatus(MockConstants.ACCOUNT_STATUS);
    userAccountModel.setRoleModel(createRoleModel());
    userAccountModel.setTeamModel(createTeamModel());
    userAccountModel.setOfficeModel(createOfficeModel());
    return userAccountModel;
  }

  /**
   * Create mock create user account
   *
   * @return mock create user account model
   */
  public static CreateUserAccountModel createCreateUserAccountModel() {
    CreateUserAccountModel createUserAccountModel = new CreateUserAccountModel();
    createUserAccountModel.setName(MockConstants.ACCOUNT_NAME);
    createUserAccountModel.setEmail(MockConstants.ACCOUNT_EMAIL);
    createUserAccountModel.setPhone(MockConstants.ACCOUNT_PHONE);
    createUserAccountModel.setOfficeId(MockConstants.OFFICE_ID);
    createUserAccountModel.setTeamId(MockConstants.TEAM_ID);
    createUserAccountModel.setRoleId(MockConstants.ROLE_ID);
    return createUserAccountModel;
  }

  /**
   * Create mock create user account entity
   *
   * @return mock create user account entity
   */
  public static UserAccountEntity createUserAccountEntity() {
    UserAccountEntity userAccountEntity = new UserAccountEntity();
    userAccountEntity.setId(MockConstants.ACCOUNT_ID);
    userAccountEntity.setName(MockConstants.ACCOUNT_NAME);
    userAccountEntity.setEmail(MockConstants.ACCOUNT_EMAIL);
    userAccountEntity.setPhone(MockConstants.ACCOUNT_PHONE);
    userAccountEntity.setOfficeId(MockConstants.OFFICE_ID);
    userAccountEntity.setTeamId(MockConstants.TEAM_ID);
    userAccountEntity.setRoleId(MockConstants.ROLE_ID);
    userAccountEntity.setStatus(MockConstants.ACCOUNT_STATUS);
    return userAccountEntity;
  }

  /**
   * Create mock update user account model
   *
   * @return mock update user account model
   */
  public static UpdateUserAccountModel createUpdateUserAccountModel() {
    UpdateUserAccountModel updateUserAccountModel = new UpdateUserAccountModel();
    updateUserAccountModel.setId(MockConstants.ACCOUNT_ID);
    updateUserAccountModel.setName(MockConstants.ACCOUNT_NAME);
    updateUserAccountModel.setEmail(MockConstants.ACCOUNT_EMAIL);
    updateUserAccountModel.setPhone(MockConstants.ACCOUNT_PHONE);
    updateUserAccountModel.setOfficeId(MockConstants.OFFICE_ID);
    updateUserAccountModel.setTeamId(MockConstants.TEAM_ID);
    updateUserAccountModel.setRoleId(MockConstants.ROLE_ID);
    updateUserAccountModel.setStatus(MockConstants.ACCOUNT_STATUS);
    return updateUserAccountModel;
  }

  /**
   * Campare two role model
   *
   * @param expected
   * @param actual
   * @return true or false
   */
  public static boolean compareTwoUserAccount(UserAccountModel expected, UserAccountModel actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getStatus(), actual.getStatus());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getStatus(), actual.getStatus());
//    compareTwoRole(expected.getRoleModel(), actual.getRoleModel());
//    compareTwoOffice(expected.getOfficeModel(), actual.getOfficeModel());
//    compareTwoTeam(expected.getTeamModel(), actual.getTeamModel());
    return true;
  }
}
