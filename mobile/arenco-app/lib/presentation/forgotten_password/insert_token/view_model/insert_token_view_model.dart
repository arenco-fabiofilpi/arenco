// ignore_for_file: use_setters_to_change_properties

import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/infra.dart';
import 'package:arenco_app/presentation/forgotten_password/insert_username/view_model/view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/shared_library.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:injectable/injectable.dart';
import 'package:mobx/mobx.dart';
part 'insert_token_view_model.g.dart';

@lazySingleton
class InsertTokenViewModel = _InsertTokenViewModelBase
    with _$InsertTokenViewModel;

abstract class _InsertTokenViewModelBase with Store, Navigation {
  @observable
  String _pinCode = "";

  @observable
  bool _loadingNextButton = false;

  @observable
  bool _loadingRequestPinAgain = false;

  @computed
  bool get loadingNextButton => _loadingNextButton;

  @computed
  bool get loadingRequestPinAgain => _loadingRequestPinAgain;

  @computed
  String get pinCode => _pinCode;

  @action
  bool _checkButtonIsPressed() {
    return _loadingNextButton || _loadingRequestPinAgain;
  }

  @action
  void changePinCode({required String value}) {
    _pinCode = value;
  }

  @action
  void cleanData() {
    _pinCode = "";
    _loadingNextButton = false;
    _loadingRequestPinAgain = false;
  }

  @action
  Future<ResponseReturnDataEntity> _validateToken() async {
    final response = await CheckTokenRepository().checkToken(
      checkTokenParams: convertCheckTokenParams(),
    );
    return response;
  }

  @action
  CheckTokenParams convertCheckTokenParams() {
    final InsertUsernameViewModel insertUsernameViewModel =
        GetIt.I.get<InsertUsernameViewModel>();
    return CheckTokenParams(
      username: insertUsernameViewModel.username,
      token: _pinCode,
    );
  }

  @action
  Future<void> finishPageToRecoveryPassword({
    required BuildContext context,
  }) async {
    if (_checkButtonIsPressed()) {
      return;
    }

    _loadingNextButton = true;

    final validateTokenResponse = await _validateToken();

    if (validateTokenResponse.statusData == StatusData.success) {
      if (context.mounted) navigateTo(context, page: resetPassword);
    } else {
      if (context.mounted) {
        ShowModalComponent(
          title: "Parece que ocorreu um erro inesperado. 😕",
          subtitle: validateTokenResponse.messageData?.errorMessage,
          onPressed: () {
            navigateBack(context);
          },
          titleButton: "Voltar",
        ).modalBottomSheet(context);
      }
    }

    _loadingNextButton = false;
  }

  @action
  Future<void> requestTokenAgain({required BuildContext context}) async {
    final insertUsernameViewModel = GetIt.I.get<InsertUsernameViewModel>();

    _loadingRequestPinAgain = true;
    await insertUsernameViewModel.sendRequestTokenAgain(context: context);

    _loadingRequestPinAgain = false;
  }
}
