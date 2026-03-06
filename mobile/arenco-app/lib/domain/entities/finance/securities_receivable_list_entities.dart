// ignore_for_file: must_be_immutable

import 'package:arenco_app/domain/domain.dart';
import 'package:equatable/equatable.dart';

class SecuritiesReceivableListEntity extends Equatable {
  List<SecuritiesReceivableEntity?>? securitiesReceivableList;

  SecuritiesReceivableListEntity({
    this.securitiesReceivableList,
  });

  @override
  List<Object?> get props => [
        securitiesReceivableList,
      ];
}
