package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.ArencoAuthLibGroupService;
import br.com.arenco.arenco_clientes.entities.GroupsToSuperGroupsRelationModel;
import br.com.arenco.arenco_clientes.entities.RoleModel;
import br.com.arenco.arenco_clientes.entities.UserGroupModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.repositories.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoAuthLibGroupServiceImpl implements ArencoAuthLibGroupService {
  private final GroupsToSuperGroupsRelationModelRepository
      groupsToSuperGroupsRelationModelRepository;
  private final GroupsToRolesRelationModelRepository groupsToRolesRelationModelRepository;
  private final UsersToGroupsRelationModelRepository usersToGroupsRelationModelRepository;
  private final RoleModelRepository roleModelRepository;
  private final UserGroupModelRepository userGroupModelRepository;

  @Override
  public List<UserGroupModel> getGroupsByUser(final UserModel user) {
    log.debug("getGroupsByUser - UserId {}", user.getId());
    final var relations =
        usersToGroupsRelationModelRepository.findByUserId(user.getId());
    log.debug("getGroupsByUser - Relations {}", relations);
    if (relations.isEmpty()) {
      return List.of();
    }
    final var resultList = new ArrayList<UserGroupModel>();
    for (final var relation : relations) {
      log.debug("getGroupsByUser - Relation {}", relation);
      final var groupId = relation.getUserGroupId();
      final var groupOpt = userGroupModelRepository.findById(groupId);
      if (groupOpt.isEmpty()) {
        log.error("Group {} not found", groupId);
        continue;
      }
      final var group = groupOpt.get();
      log.debug("getGroupsByUser - Group {}", group);
      resultList.add(group);
    }
    return resultList;
  }

  @Override
  public List<RoleModel> getRolesByGroupList(final List<UserGroupModel> groupModelList) {
    log.debug("getRolesByGroup - Group {}", groupModelList);
    final var allGroups = getAllGroups(groupModelList);
    final var resultSet = new HashSet<RoleModel>();
    log.debug("getRolesByGroup - allGroups {}", allGroups);
    for (final var group : allGroups) {
      log.debug("getRolesByGroup - group {}", group);
      final var rolesRelationModels =
          groupsToRolesRelationModelRepository.findByUserGroupId(group.getId());
      for (final var role : rolesRelationModels) {
        log.debug("getRolesByGroup - role {}", role);
        roleModelRepository.findById(role.getRoleId()).ifPresent(resultSet::add);
      }
    }
    return new ArrayList<>(resultSet);
  }

  @Override
  public List<GrantedAuthority> getGrantedAuthorities(
      final List<UserGroupModel> userGroupModelList) {
    final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    final var roles = this.getRolesByGroupList(userGroupModelList);
    for (final var role : roles) {
      grantedAuthorities.add(new SimpleGrantedAuthority(role.getCode()));
    }
    return new ArrayList<>(grantedAuthorities);
  }

  private List<UserGroupModel> getAllGroups(final List<UserGroupModel> groups) {
    log.debug("getAllGroups - Starting with groups: {}", groups);
    final var resultList = new HashSet<UserGroupModel>(); // Para evitar duplicatas
    final var visited = new HashSet<String>(); // Para evitar loops em relações cíclicas

    // Metodo recursivo para processar cada grupo
    processGroups(groups, resultList, visited);

    return new ArrayList<>(resultList);
  }

  private void processGroups(
      final List<UserGroupModel> groups,
      final Set<UserGroupModel> resultList,
      final Set<String> visited) {
    for (final var group : groups) {
      if (group == null || visited.contains(group.getId())) {
        continue; // Já processado ou nulo
      }
      visited.add(group.getId());
      resultList.add(group);
      log.debug("Processing group: {}", group);

      // Buscar e processar supergrupos desse grupo
      final List<GroupsToSuperGroupsRelationModel> superGroupsRelationModels =
          groupsToSuperGroupsRelationModelRepository.findByGroupId(group.getId());
      final var superGroups = getSuperGroups(superGroupsRelationModels);
      processGroups(superGroups, resultList, visited);

      // Buscar e processar subgrupos desse grupo
      final List<GroupsToSuperGroupsRelationModel> subGroupsRelationModels =
          groupsToSuperGroupsRelationModelRepository.findBySuperGroupId(group.getId());
      final var subGroups = getUserGroups(subGroupsRelationModels);
      processGroups(subGroups, resultList, visited);
    }
  }

  private List<UserGroupModel> getUserGroups(
      final List<GroupsToSuperGroupsRelationModel> groupsRelationModels) {
    final var resultList = new ArrayList<UserGroupModel>();
    for (final var group : groupsRelationModels) {
      userGroupModelRepository
          .findById(group.getGroupId())
          .ifPresent(resultList::add);
    }
    return resultList;
  }

  private List<UserGroupModel> getSuperGroups(
      final List<GroupsToSuperGroupsRelationModel> groupsRelationModels) {
    final var resultList = new ArrayList<UserGroupModel>();
    for (final var group : groupsRelationModels) {
      userGroupModelRepository
          .findById(group.getSuperGroupId())
          .ifPresent(resultList::add);
    }
    return resultList;
  }
}
