import 'package:arenco_app/domain/entities/entities.dart';
import 'package:json_annotation/json_annotation.dart';

part 'securities_receivable_finance.g.dart';

@JsonSerializable(explicitToJson: true)

/// {
///
///      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
///      "empresa": "string",
///      "cliente": "string",
///      "numeroDocumento": "string",
///      "sequencia": "string",
///      "dateCreated": "2024-10-22T10:03:34.690Z",
///      "lastUpdated": "2024-10-22T10:03:34.690Z",
///      "version": 0
///
///}
class SecuritiesReceivableFinanceData {
  String? id;
  @JsonKey(name: "empresa")
  String? company;
  @JsonKey(name: "cliente")
  String? client;
  @JsonKey(name: "numeroDocumento")
  String? documentNumber;
  @JsonKey(name: "sequencia")
  String? sequence;
  String? dateCreated;
  String? lastUpdated;
  String? version;

  SecuritiesReceivableFinanceData({
    this.client,
    this.company,
    this.dateCreated,
    this.documentNumber,
    this.id,
    this.lastUpdated,
    this.sequence,
    this.version,
  });

  factory SecuritiesReceivableFinanceData.fromJson(Map<String, dynamic> json) =>
      _$SecuritiesReceivableFinanceDataFromJson(json);

  SecuritiesReceivableEntity toEntity() => SecuritiesReceivableEntity(
    id: id,
    client: client,
    company: company,
    dateCreated: dateCreated,
    documentNumber: documentNumber,
    lastUpdated: lastUpdated,
    sequence: sequence,
    version: version,
  );

  Map<String, dynamic> toJson() => _$SecuritiesReceivableFinanceDataToJson(this);
}
