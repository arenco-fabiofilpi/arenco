package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.exceptions.CustomerBlockedException;
import br.com.arenco.arenco_clientes.exceptions.CustomerDisabledException;
import br.com.arenco.arenco_clientes.exceptions.PasswordNotSetException;
import br.com.arenco.arenco_clientes.services.ArencoAuthLibGroupService;
import br.com.arenco.arenco_clientes.services.BruteForceDefenseService;
import br.com.arenco.arenco_clientes.entities.UserGroupModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.repositories.UserModelRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoUserDetailsServiceImpl implements UserDetailsService {
  private final UserModelRepository userModelRepository;
  private final BruteForceDefenseService bruteForceDefenseService;
  private final ArencoAuthLibGroupService arencoAuthLibGroupService;

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final Optional<UserModel> userOptional = userModelRepository.findByUsername(username);
    if (userOptional.isEmpty()) {
      throw new UsernameNotFoundException("Usuário não encontrado");
    }
    final UserModel user = userOptional.get();
    log.debug("Found user {}", username);
    final var userGroups = getUserGroupModels(user);
    check(user, userGroups);
    return new User(
        user.getUsername(),
        user.getPassword(),
        arencoAuthLibGroupService.getGrantedAuthorities(userGroups));
  }

  private void check(final UserModel user, final List<UserGroupModel> userGroups) {
    if (userGroups == null || userGroups.isEmpty()) {
      log.debug("Failed to authenticate since user has no groups");
      throw new UsernameNotFoundException("Usuário não possui grupos");
    }
    if (user.getPassword() == null) {
      log.debug("Failed to authenticate since user has no password");
      throw new PasswordNotSetException("Usuário não possui senha");
    }
    if (user.getUsername() == null) {
      log.debug("Failed to authenticate since user has no username");
      throw new UsernameNotFoundException("Usuário não possui nome de usuário");
    }
    final var isBlockedByBruteForce = bruteForceDefenseService.isLocked(user);
    if (isBlockedByBruteForce) {
      log.debug("Failed to authenticate since user is blocked by BruteForce");
      throw new CustomerBlockedException();
    }
    final var isEnabled = user.isEnabled();
    if (!isEnabled) {
      log.debug("Customer is not enabled");
      throw new CustomerDisabledException();
    }
  }

  private List<UserGroupModel> getUserGroupModels(final UserModel user) {
    if (user == null) {
      log.debug(
          "convertUserInSpringSecurityUserDetails - Failed to authenticate since user not found");
      throw new UsernameNotFoundException("Usuário não encontrado");
    }
    return arencoAuthLibGroupService.getGroupsByUser(user);
  }
}
