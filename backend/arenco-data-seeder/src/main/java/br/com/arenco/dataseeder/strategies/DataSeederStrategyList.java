package br.com.arenco.dataseeder.strategies;

import br.com.arenco.dataseeder.strategies.impl.*;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeederStrategyList {
  private final SeedUsersStrategyImpl seedUsersStrategy;
  private final SeedContactsStrategyImpl seedContactsStrategy;
  private final SeedReceivedStrategyImpl seedReceivedStrategy;
  private final SeedAddressesStrategyImpl seedAddressesStrategy;
  private final SeedAgreementsStrategyImpl seedAgreementsStrategy;
  private final SeedUserGroupsStrategyImpl seedUserGroupsStrategy;
  private final SeedReceivablesStrategyImpl seedReceivablesStrategy;
  private final SeedPermissionsStrategyImpl seedPermissionsStrategy;
  private final SeedContactPreferenceStrategyImpl seedContactPreferenceStrategy;
  private final SeedMessengerSettingsStrategyImpl seedMessengerSettingsStrategy;
  private final SeedUsersAndGroupsRelationStrategyImpl seedUsersAndGroupsRelationStrategy;
  private final SeedGroupHierarchyRelationsStrategyImpl seedGroupHierarchyRelationsStrategy;
  private final SeedGroupPermissionRelationsStrategyImpl seedGroupPermissionRelationsStrategy;

  public List<DataSeederStrategy> dataSeederStrategies() {
    final List<DataSeederStrategy> list = new ArrayList<>();
    list.add(seedUsersStrategy);
    list.add(seedContactsStrategy);
    list.add(seedReceivedStrategy);
    list.add(seedAddressesStrategy);
    list.add(seedAgreementsStrategy);
    list.add(seedUserGroupsStrategy);
    list.add(seedReceivablesStrategy);
    list.add(seedPermissionsStrategy);
    list.add(seedContactPreferenceStrategy);
    list.add(seedMessengerSettingsStrategy);
    list.add(seedUsersAndGroupsRelationStrategy);
    list.add(seedGroupHierarchyRelationsStrategy);
    list.add(seedGroupPermissionRelationsStrategy);
    return list;
  }
}
