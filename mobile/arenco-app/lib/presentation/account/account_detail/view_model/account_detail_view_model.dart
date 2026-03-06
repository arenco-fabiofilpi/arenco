import 'package:injectable/injectable.dart';
import 'package:mobx/mobx.dart';
part 'account_detail_view_model.g.dart';

@lazySingleton
class AccountDetailViewModel = _AccountDetailViewModelBase with _$AccountDetailViewModel;

abstract class _AccountDetailViewModelBase with Store {

  @observable
  String? _id = "fabio.abrantes";

  @observable
  String? _name = "Fabio abrantes";

  @observable
  String? _email = "fabio.abrantes@teste.com";

  @observable
  String? _phoneNumber = "+55 11 9 9999-9999";

  @computed
  String? get id => _id;

  @computed
  String? get name => _name;

  @computed
  String? get email => _email;

  @computed
  String? get phoneNumber => _phoneNumber;
  
}
