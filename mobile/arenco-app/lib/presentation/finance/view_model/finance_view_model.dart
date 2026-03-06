import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/repository/repository.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:injectable/injectable.dart';
import 'package:mobx/mobx.dart';
part 'finance_view_model.g.dart';

@LazySingleton()
class FinanceViewModel = _FinanceViewModelBase with _$FinanceViewModel;

abstract class _FinanceViewModelBase extends FinanceRepository with Store{
  
  @observable
  String? _json = "";

  @observable
  bool? _loading = false;

  @observable
  String? _error = "";

  @computed 
  String? get jsonView => _json;

  @computed
  bool? get loading => _loading;

  @computed
  String? get erroMessage => _error; 

  @observable
  String? _token = "";

  @computed
  String? get token => _token;

  @observable
  String? _baseUrl = "";

  @computed
  String? get baseUrl => _baseUrl;

  @action
  Future<void> getSecuritiesReceivableFinance()async{
    _loading = true;
    try {
      final response = await getSecuritiesReceivableList();
      if (response.statusData == StatusData.success) {
        _json = response.body.toString();
        _error = "";
      } else {
        final token = await GetRefreshToken().getTokenLocalStorage();
        _token = token.token;
        _baseUrl = response.messageData?.errorMessage ?? "";
        _error = response.statusData.toString();
      }
    } catch (e) {
      _error = e.toString();
    } finally {
      _loading = false;
    }
  }

}
