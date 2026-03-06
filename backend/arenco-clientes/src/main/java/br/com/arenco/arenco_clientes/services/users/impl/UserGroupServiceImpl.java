package br.com.arenco.arenco_clientes.services.users.impl;

import br.com.arenco.arenco_clientes.services.users.UserGroupService;
import br.com.arenco.arenco_clientes.entities.UserGroupModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.repositories.UserGroupModelRepository;
import br.com.arenco.arenco_clientes.repositories.UsersToGroupsRelationModelRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {
  private final UserGroupModelRepository userGroupRepository;
  private final UsersToGroupsRelationModelRepository usersToGroupsRelationModelRepository;

  @Override
  public List<UserGroupModel> getGroupsByUser(final UserModel user) {
    final var relations = usersToGroupsRelationModelRepository.findByUserId(user.getId());
    final var groupModelList = new ArrayList<UserGroupModel>();
    if (relations != null && !relations.isEmpty()) {
      for (final var relation : relations) {
        final var groupId = relation.getUserGroupId();
        final var groupModelOptional = userGroupRepository.findById(groupId);
        groupModelOptional.ifPresent(groupModelList::add);
      }
    }
    return groupModelList;
  }
}
