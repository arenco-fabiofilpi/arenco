// ignore_for_file: prefer_final_locals

import 'package:arenco_app/data/data.dart';
import 'package:arenco_app/domain/entities/entities.dart';
import 'package:json_annotation/json_annotation.dart';

part 'securities_receivable_finance_list.g.dart';

@JsonSerializable(explicitToJson: true)

/// {
///     "tituloAReceberModelList": [
///     {
///       "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
///       "empresa": "string",
///       "cliente": "string",
///       "numeroDocumento": "string",
///       "sequencia": "string",
///       "dateCreated": "2024-10-22T10:03:34.690Z",
///       "lastUpdated": "2024-10-22T10:03:34.690Z",
///       "version": 0
///      }
///   ]
///}
class SecuritiesReceivableFinanceListData {
  @JsonKey(name: "tituloAReceberModelList")
  List<SecuritiesReceivableFinanceData?>? securitiesReceivableFinanceList;

  SecuritiesReceivableFinanceListData({
    this.securitiesReceivableFinanceList,
  });

  factory SecuritiesReceivableFinanceListData.fromJson(
          Map<String, dynamic> json) =>
      _$SecuritiesReceivableFinanceListDataFromJson(json);

  SecuritiesReceivableListEntity toEntity() {
    List<SecuritiesReceivableEntity?>? listSecurities = [];

    securitiesReceivableFinanceList
        ?.forEach((SecuritiesReceivableFinanceData? securities) {
      if (securities != null) {
        listSecurities.add(
          SecuritiesReceivableEntity(
            client: securities.client,
            company: securities.company,
            dateCreated: securities.dateCreated,
            documentNumber: securities.documentNumber,
            id: securities.id,
            lastUpdated: securities.lastUpdated,
            sequence: securities.sequence,
            version: securities.version,
          ),
        );
      }
    });

    return SecuritiesReceivableListEntity(
      securitiesReceivableList: listSecurities,
    );
  }

  Map<String, dynamic> toJson() =>
      _$SecuritiesReceivableFinanceListDataToJson(this);
}
