// ignore_for_file: prefer_final_fields, use_setters_to_change_properties
import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/infra.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/shared_library.dart';
import 'package:flutter/material.dart';
import 'package:injectable/injectable.dart';
import 'package:mobx/mobx.dart';
part 'login_view_model.g.dart';

@LazySingleton()
class LoginViewModel = _LoginViewModelBase with _$LoginViewModel;

abstract class _LoginViewModelBase extends LoginRepository
    with Store, Navigation {
  @observable
  bool _loadingSendButton = false;

  @computed
  bool get loadingSendButton => _loadingSendButton;

  @observable
  bool _obscureText = true;

  @computed
  bool get obscureText => _obscureText;

  @action
  void changeObscure() => _obscureText = !_obscureText;

  @observable
  String? _username;

  @observable
  String? _usernameError;

  @observable
  String? _password;

  @observable
  String? _passwordError;

  @computed
  String? get username => _username;

  @computed
  String? get usernameError => _usernameError;

  @computed
  String? get password => _password;

  @computed
  String? get passwordError => _passwordError;

  @action
  void onChangedUsername(String value) {
    _username = value;
    validateUsername();
  }

  @action
  void onChangedPassword(String value) {
    _password = value;
    validatePassword();
  }

  @action
  void validateUsername() {
    if (_username == null || (_username != null && _username == "")) {
      _usernameError = "Campo obrigatório";
      return;
    }
    _usernameError = null;
  }

  @action
  bool validatePassword() {
    if (_password == null || (_password != null && _password == "")) {
      _passwordError = "Campo obrigatório";
      return false;
    }
    _passwordError = null;
    return true;
  }

  @action
  bool _validateForm() {
    _validateSizeUserName();
    validatePassword();
    if (_password != null &&
        _passwordError == null &&
        _username != null &&
        _usernameError == null) {
      return true;
    }
    return false;
  }

  @action
  Future<void> sendForm({required BuildContext context}) async {
    if (_validateForm()) {
      _loadingSendButton = true;
      final responseAPI = await loginRequest(
        loginParams: LoginParams(
          password: _password,
          username: _username,
        ),
      );

      switch (responseAPI.statusData) {
        case StatusData.success:
          {
            if (responseAPI.statusData == StatusData.success &&
                context.mounted) {
              _pushToHome(context: context);
            }
            break;
          }
        case StatusData.systemError:
          {
            if (context.mounted) {
              ShowModalComponent(
                title: "Parece que ocorreu um erro inesperado. 😕",
                subtitle: responseAPI.messageData?.errorMessage,
                onPressed: () {
                  navigateBack(context);
                },
                titleButton: "Voltar",
              ).modalBottomSheet(context);
            }
            break;
          }
        default:
          {
            ShowModalComponent(
              title: "Ops! Não conseguimos realizar o seu login. 😕",
              subtitle: responseAPI.messageData?.errorMessage,
              onPressed: () {
                navigateBack(context);
              },
              titleButton: "Voltar",
            ).modalBottomSheet(context);
            break;
          }
      }
    }
    _loadingSendButton = false;
  }

  @action
  void _pushToHome({required BuildContext context}) {
    pushReplacement(context, page: homeScreen);
  }

  @action
  bool _validateSizeUserName() {
    if (_username?.length == 14 || _username?.length == 18) {
      return true;
    }
    _usernameError = "Campo incorreto";
    return false;
  }
}
