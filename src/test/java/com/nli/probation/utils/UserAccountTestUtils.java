package com.nli.probation.utils;

import static com.nli.probation.utils.OfficeTestUtils.createOfficeModel;
import static com.nli.probation.utils.RoleTestUtils.createRoleModel;
import static com.nli.probation.utils.TeamTestUtils.createTeamModel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nli.probation.constant.EntityStatusEnum.UserAccountStatusEnum;
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
    userAccountModel.setId(1);
    userAccountModel.setName("Tuan");
    userAccountModel.setEmail("minhtuan@gmail.com");
    userAccountModel.setPhone("0987654321");
    userAccountModel.setStatus(UserAccountStatusEnum.ACTIVE.ordinal());
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
    createUserAccountModel.setName("Tuan");
    createUserAccountModel.setEmail("minhtuan@gmail.com");
    createUserAccountModel.setPhone("0987654321");
    createUserAccountModel.setOfficeId(1);
    createUserAccountModel.setTeamId(1);
    createUserAccountModel.setRoleId(1);
    return createUserAccountModel;
  }

  /**
   * Create mock create user account entity
   *
   * @return mock create user account entity
   */
  public static UserAccountEntity createUserAccountEntity() {
    UserAccountEntity userAccountEntity = new UserAccountEntity();
    userAccountEntity.setId(1);
    userAccountEntity.setName("Tuan");
    userAccountEntity.setEmail("minhtuan@gmail.com");
    userAccountEntity.setPhone("0987654321");
    userAccountEntity.setOfficeId(1);
    userAccountEntity.setTeamId(1);
    userAccountEntity.setRoleId(1);
    userAccountEntity.setStatus(1);
    return userAccountEntity;
  }

  /**
   * Create mock update user account model
   *
   * @return mock update user account model
   */
  public static UpdateUserAccountModel createUpdateUserAccountModel() {
    UpdateUserAccountModel updateUserAccountModel = new UpdateUserAccountModel();
    updateUserAccountModel.setId(1);
    updateUserAccountModel.setName("Tuan");
    updateUserAccountModel.setEmail("minhtuan@gmail.com");
    updateUserAccountModel.setPhone("0987654321");
    updateUserAccountModel.setOfficeId(1);
    updateUserAccountModel.setTeamId(1);
    updateUserAccountModel.setRoleId(1);
    updateUserAccountModel.setStatus(1);
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
