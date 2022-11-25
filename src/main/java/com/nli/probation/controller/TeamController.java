package com.nli.probation.controller;

import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import com.nli.probation.model.ResponseModel;
import com.nli.probation.model.team.CreateTeamModel;
import com.nli.probation.model.team.TeamModel;
import com.nli.probation.model.team.UpdateTeamModel;
import com.nli.probation.model.useraccount.UserAccountModel;
import com.nli.probation.resolver.annotation.RequestPagingParam;
import com.nli.probation.service.TeamService;
import com.nli.probation.service.UserAccountService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/teams")
public class TeamController {

  private final TeamService teamService;
  private final UserAccountService userAccountService;

  public TeamController(TeamService teamService,
      UserAccountService userAccountService) {
    this.teamService = teamService;
    this.userAccountService = userAccountService;
  }

  /**
   * Create new team
   *
   * @param createModel
   * @return response entity contains created model
   */
  @PostMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ResponseModel> createTeam(@Valid @RequestBody CreateTeamModel createModel) {
    TeamModel savedModel = teamService.createTeam(createModel);
    ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
        .data(savedModel)
        .message("OK");
    return new ResponseEntity<>(responseModel, HttpStatus.OK);
  }

  /**
   * Find team by id
   *
   * @param id
   * @return response entity contains model
   */
  @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ResponseModel> findTeamById(@PathVariable int id) {
    TeamModel foundTeam = teamService.findTeamById(id);
    ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
        .data(foundTeam)
        .message("OK");
    return new ResponseEntity<>(responseModel, HttpStatus.OK);
  }

  /**
   * Delete a team by id
   *
   * @param id
   * @return response entity contains deleted model
   */
  @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ResponseModel> deleteTeam(@PathVariable int id) {
    TeamModel deletedModel = teamService.deleteTeamById(id);
    ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
        .data(deletedModel)
        .message("OK");
    return new ResponseEntity<>(responseModel, HttpStatus.OK);

  }

  /**
   * Update team
   *
   * @param requestModel
   * @return response entity contains model
   */
  @PutMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ResponseModel> updateTeam(
      @Valid @RequestBody UpdateTeamModel requestModel) {
    TeamModel updatedModel = teamService.updateTeam(requestModel);
    ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
        .data(updatedModel)
        .message("OK");
    return new ResponseEntity<>(responseModel, HttpStatus.OK);
  }

  /**
   * Search teams
   *
   * @param requestPaginationModel
   * @param searchText
   * @return response entity contains data resource
   */
  @GetMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Object> searchTeams(
      @RequestPagingParam RequestPaginationModel requestPaginationModel,
      @RequestParam(value = "searchText", defaultValue = "") String searchText) {
    ResourceModel<TeamModel> teamList = teamService.searchTeams(searchText, requestPaginationModel);
    return new ResponseEntity<>(teamList, HttpStatus.OK);
  }

  /**
   * Update team of accounts
   *
   * @param id
   * @param userIds
   * @return updated account list
   */
  @PostMapping(path = "{id}/user-accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ResponseModel> addUsersToTeam(@PathVariable int id,
      @RequestBody List<Integer> userIds) {
    List<UserAccountModel> updatedModels = userAccountService.addUserListToTeam(id, userIds);
    ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
        .data(updatedModels)
        .message("OK");
    return new ResponseEntity<>(responseModel, HttpStatus.OK);
  }

  /**
   * Search user accounts of a team
   *
   * @param requestPaginationModel
   * @param searchText
   * @return response entity contains data resource
   */
  @GetMapping(path = "{id}/user-accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Object> searchUsersOfTeam(
      @RequestPagingParam RequestPaginationModel requestPaginationModel,
      @PathVariable int id,
      @RequestParam(value = "searchText", defaultValue = "") String searchText) {
    ResourceModel<UserAccountModel> accountList = userAccountService
        .searchAccounts(searchText, requestPaginationModel, id);
    return new ResponseEntity<>(accountList, HttpStatus.OK);
  }

  /**
   * Delete user accounts from team
   *
   * @param id
   * @param userIds
   * @return deleted account list
   */
  @DeleteMapping(path = "{id}/user-accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ResponseModel> deleteUsersFromTeam(@PathVariable int id,
      @RequestBody List<Integer> userIds) {
    List<UserAccountModel> updatedModels = userAccountService.deleteUserListFromTeam(id, userIds);
    ResponseModel responseModel = new ResponseModel().statusCode(HttpStatus.OK.value())
        .data(updatedModels)
        .message("OK");
    return new ResponseEntity<>(responseModel, HttpStatus.OK);
  }
}
