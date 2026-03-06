// ignore_for_file: must_be_immutable

import 'package:equatable/equatable.dart';

class SecuritiesReceivableEntity extends Equatable {
  String? id;
  String? company;
  String? client;
  String? documentNumber;
  String? sequence;
  String? dateCreated;
  String? lastUpdated;
  String? version;

  SecuritiesReceivableEntity({
    this.id,
    this.company,
    this.client,
    this.documentNumber,
    this.sequence,
    this.dateCreated,
    this.lastUpdated,
    this.version,
  });

  @override
  List<Object?> get props => [
        id,
        company,
        client,
        documentNumber,
        sequence,
        dateCreated,
        lastUpdated,
        version,
      ];
}
