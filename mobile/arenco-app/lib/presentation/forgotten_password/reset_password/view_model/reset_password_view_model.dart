// ignore_for_file: use_setters_to_change_properties

import 'package:arenco_app/domain/entities/entities.dart';
import 'package:arenco_app/infra/infra.dart';
import 'package:arenco_app/presentation/forgotten_password/insert_token/view_model/view_model.dart';
import 'package:arenco_app/presentation/forgotten_password/insert_username/view_model/view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/shared_library.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:injectable/injectable.dart';
import 'package:mobx/mobx.dart';
part 'reset_password_view_model.g.dart';

@lazySingleton
class ResetPasswordViewModel = _ResetPasswordViewModelBase
    with _$ResetPasswordViewModel;

abstract class _ResetPasswordViewModelBase with Store, Navigation {
  @observable
  bool _loadingButton = false;

  @observable
  bool _obscureText = false;

  @observable
  bool _obscureSecondText = false;

  @observable
  String _newPassword = "";

  @observable
  String _newPasswordAgain = "";

  @observable
  String? _validatePasswordIsEqual;

  @observable
  String? _newPasswordErro;

  @observable
  String? _newPasswordAgainErro;

  @computed
  bool get loadingButton => _loadingButton;

  @computed
  String get newPassword => _newPassword;

  @computed
  bool get obscureText => _obscureText;

  @computed
  bool get obscureSecondText => _obscureSecondText;

  @computed
  String? get validatePasswordIsEqualText => _validatePasswordIsEqual;

  @computed
  String? get newPasswordErro => _newPasswordErro;

  @computed
  String? get newPasswordAgainErro => _newPasswordAgainErro;

  @action
  void resetData() {
    _loadingButton = false;
    _newPassword = '';
    _newPasswordAgain = '';
  }

  @action
  void changeResetPassword(String value) {
    _newPassword = value;
  }

  @action
  void changeResetPasswordAgain(String value) {
    _newPasswordAgain = value;
  }

  @action
  void setObscureText() {
    _obscureText = !obscureText;
  }

  @action
  void setObscureSecondText() {
    _obscureSecondText = !_obscureSecondText;
  }

  @action
  void validatePasswordIsEqual() {
    if (_newPassword != _newPasswordAgain) {
      _validatePasswordIsEqual =
          "As senhas digitadas não correspondem. Por favor, verifique e tente novamente.";
      return;
    }
    _validatePasswordIsEqual = null;
  }

  @computed
  bool get isValidate => checkAllValidate(valueToValidate: _newPassword);

  @computed
  bool get isEqual => _newPassword == _newPasswordAgain;

  @computed
  bool get checkCompletedPassword => isValidate && isEqual;

  @action
  Future<void> newPasswordWithToken({required BuildContext context}) async {
    final InsertUsernameViewModel insertUsernameViewModel =
        GetIt.I.get<InsertUsernameViewModel>();
    final InsertTokenViewModel insertTokenViewModel =
        GetIt.I.get<InsertTokenViewModel>();
    _loadingButton = true;
    if (checkCompletedPassword) {
      final response =
          await NewPasswordTokenRepository().changePasswordWithToken(
        newPassowrdTokenParams: NewPasswordTokenParams(
          newPassord: _newPassword,
          token: insertTokenViewModel.pinCode,
          username: insertUsernameViewModel.username,
        ),
      );
      _loadingButton = false;
      if (response.statusData == StatusData.success) {
        if (context.mounted) popUntilPage(context, page: loginScreen);
      } else {
        if (context.mounted) {
          ShowModalComponent(
            title: "Parece que ocorreu um erro inesperado. 😕",
            subtitle: response.messageData?.errorMessage,
            onPressed: () {
              navigateBack(context);
            },
            titleButton: "Voltar",
          ).modalBottomSheet(context);
        }
      }
    }
  }
}
