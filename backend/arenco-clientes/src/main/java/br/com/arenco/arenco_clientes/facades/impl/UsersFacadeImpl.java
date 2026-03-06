package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.ArencoAuthLibUserFacade;
import br.com.arenco.arenco_clientes.services.ArencoSessionService;
import br.com.arenco.arenco_clientes.services.ArencoUserAuthService;
import br.com.arenco.arenco_clientes.dtos.user.ContactDto;
import br.com.arenco.arenco_clientes.dtos.user.UserDto;
import br.com.arenco.arenco_clientes.dtos.user.UserGroupDto;
import br.com.arenco.arenco_clientes.exceptions.OldPasswordInvalidException;
import br.com.arenco.arenco_clientes.exceptions.UserIdNotFoundException;
import br.com.arenco.arenco_clientes.facades.UsersFacade;
import br.com.arenco.arenco_clientes.services.users.UserGroupService;
import br.com.arenco.arenco_clientes.services.users.impl.UserServiceImpl;
import br.com.arenco.arenco_clientes.factories.UserDtoFactory;
import br.com.arenco.arenco_clientes.factories.UserGroupDtoFactory;
import br.com.arenco.arenco_clientes.entities.UserModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsersFacadeImpl implements UsersFacade {
  private final UserServiceImpl userServiceImpl;
  private final ArencoUserAuthService arencoUserAuthService;
  private final UserGroupService userGroupService;
  private final ArencoSessionService arencoSessionService;
  private final ArencoAuthLibUserFacade arencoAuthLibUserFacade;

  @Override
  public UserModel findUser(final String id) {
    return userServiceImpl.findUserById(id, UserIdNotFoundException.class);
  }

  @Override
  public Optional<UserDto> getCurrentUser() {
    log.info("getCurrentUser - Init");
    final var userModel = arencoSessionService.getCurrentUser();
    final var userDto = new UserDtoFactory(userModel).create();
    return Optional.of(userDto);
  }

  @Override
  public List<ContactDto> getCurrentUserContacts() {
    final var userModel = arencoSessionService.getCurrentUser();
    return arencoAuthLibUserFacade.getCurrentUserContacts(userModel, false);
  }

  @Override
  public List<UserGroupDto> getCurrentUserGroups() {
    final var userModel = arencoSessionService.getCurrentUser();
    final var userGroups = userGroupService.getGroupsByUser(userModel);
    final var list = new ArrayList<UserGroupDto>();
    for (final var group : userGroups) {
      log.info("getCurrentUserGroups - Converting UserGroupDto: {}", group);
      final var groupDto = new UserGroupDtoFactory(group).create();
      list.add(groupDto);
    }
    return list;
  }

  @Override
  public void changeCurrentUserPassword(final String oldPassword, final String newPassword) {
    log.info("changeCurrentUserPassword - Init");
    final var userModel = arencoSessionService.getCurrentUser();
    validateOldPassword(oldPassword, userModel);
    arencoUserAuthService.setNewPassword(userModel, newPassword);
    log.info("changeCurrentUserPassword - Finish");
  }

  private void validateOldPassword(String oldPassword, UserModel userModel) {
    try {
      arencoUserAuthService.authenticateUserAndPassword(userModel, oldPassword);
    } catch (final BadCredentialsException e) {
      log.debug(e.getMessage(), e);
      throw new OldPasswordInvalidException(e);
    }
  }
}
