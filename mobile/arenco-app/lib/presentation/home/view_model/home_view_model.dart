// ignore_for_file: use_setters_to_change_properties

import 'package:injectable/injectable.dart';
import 'package:mobx/mobx.dart';
part 'home_view_model.g.dart';

@LazySingleton()
class HomeViewModel = _HomeViewModelBase with _$HomeViewModel;

abstract class _HomeViewModelBase with Store {
  
  @observable
  int _currentIndexBar = 0;

  @computed
  int get currentIndexBar => _currentIndexBar;

  @action
  void setCurrentIndexBar(int current) => _currentIndexBar = current;

}
