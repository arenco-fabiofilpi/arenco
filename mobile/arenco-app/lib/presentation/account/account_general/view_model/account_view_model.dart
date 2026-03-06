import 'package:injectable/injectable.dart';
import 'package:mobx/mobx.dart';
part 'account_view_model.g.dart';

@LazySingleton()
class AccountGeneralViewModel = _AccountGeneralViewModelBase with _$AccountGeneralViewModel;

abstract class _AccountGeneralViewModelBase with Store {

  @observable
  String? _imageUrl;

  @observable
  String? _name = "Fabio Abrantes";

  @observable
  String? _username = "fabio.abrantes";

  @computed
  String? get imageUrl => _imageUrl;

  @computed
  String? get name => _name;

  @computed
  String? get username => _username;
}
