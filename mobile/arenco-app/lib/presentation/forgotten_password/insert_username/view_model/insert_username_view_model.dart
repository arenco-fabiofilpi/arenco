// ignore_for_file: avoid_bool_literals_in_conditional_expressions, use_setters_to_change_properties, unnecessary_null_comparison

import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/infra.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/shared_library.dart';
import 'package:flutter/material.dart';
import 'package:injectable/injectable.dart';
import 'package:mobx/mobx.dart';
part 'insert_username_view_model.g.dart';

enum CheckBoxSelect {
  sms,
  email,
}

enum StepRequest { sendRequestContacts, sendRequestToken }

@lazySingleton
class InsertUsernameViewModel = _InsertUsernameViewModelBase
    with _$InsertUsernameViewModel;

abstract class _InsertUsernameViewModelBase with Store, Navigation {
  @observable
  ContactsPasswordListParams? _contactsPasswordListParams;

  @observable
  ContactsPasswordParams? _contactsPasswordSelected;

  @observable
  StepRequest _stepRequest = StepRequest.sendRequestContacts;

  @observable
  String _nameButtonRequest = "Consultar";

  @observable
  String _username = "";

  @observable
  String? _usernameError;

  @observable
  CheckBoxSelect? _checkBoxSelect;

  @observable
  String? _checkBoxError;

  @observable
  bool _loading = false;

  @computed
  ContactsPasswordParams? get contactsPasswordSelected =>
      _contactsPasswordSelected;

  @computed
  ContactsPasswordListParams? get contactsPasswordListParams =>
      _contactsPasswordListParams;

  @computed
  StepRequest get stepRequest => _stepRequest;

  @computed
  String get nameButtonRequest => _nameButtonRequest;

  @computed
  String? get checkBoxError => _checkBoxError;

  @computed
  String? get usernameError => _usernameError;

  @computed
  bool get loading => _loading;

  @computed
  String get username => _username;

  @computed
  bool get checkSMSSelected =>
      _checkBoxSelect != null && _checkBoxSelect == CheckBoxSelect.sms
          ? true
          : false;

  @computed
  bool get checkEMAILSelected =>
      _checkBoxSelect != null && _checkBoxSelect == CheckBoxSelect.email
          ? true
          : false;

  @computed
  bool get loadingRequestContacts =>
      _loading == true && _stepRequest == StepRequest.sendRequestContacts;

  @action
  void changeStepRequest() {
    _stepRequest = _stepRequest == StepRequest.sendRequestContacts
        ? StepRequest.sendRequestToken
        : StepRequest.sendRequestContacts;
    _nameButtonRequest = _stepRequest == StepRequest.sendRequestContacts
        ? "Consultar"
        : "Enviar solicitação";
  }

  @action
  void changeUsername({required String username}) {
    _username = username;
    _validateUsername();
  }

  @action
  void selectedBox({required ContactsPasswordParams? contactsPasswordParams}) {
    _contactsPasswordSelected = contactsPasswordParams;
  }

  @action
  Future<void> requestTokenForgottenPassword({
    required BuildContext context,
    bool pushPage = true,
  }) async {
    try {
      _loading = true;
      if (_stepRequest == StepRequest.sendRequestContacts &&
          _validateSizeUserName() &&
          _validateUsername()) {
        final request = await PasswordRepository().getConsultContact(
          username: _username,
        );
        if (request.isSuccessful ?? false) {
          _contactsPasswordListParams =
              request.body as ContactsPasswordListParams? ??
                  const ContactsPasswordListParams();
          changeStepRequest();
        } else {
          if (context.mounted) {
            ShowModalComponent(
              title: "Parece que ocorreu um erro inesperado. 😕",
              subtitle: request.messageData?.errorMessage,
              onPressed: () {
                navigateBack(context);
              },
              titleButton: "Voltar",
            ).modalBottomSheet(context);
          }
        }
      } else {
        if (_validateSizeUserName() &&
            _validateUsername() &&
            _validateBoxSelectedContact()) {
          final request = await RequestTokenRepository().requestToken(
            requestTokenParams: RequestTokenParams(
              contactUid: _contactsPasswordSelected?.uuid ?? "",
              username: _username,
            ),
          );

          if (request.isSuccessful ?? false) {
            cleanToPageToken();
            if (context.mounted && pushPage) {
              navigateTo(context, page: insertToken);
            }
          } else {
            if (context.mounted) {
              ShowModalComponent(
                title: "Parece que ocorreu um erro inesperado. 😕",
                subtitle: request.messageData?.errorMessage,
                onPressed: () {
                  navigateBack(context);
                },
                titleButton: "Voltar",
              ).modalBottomSheet(context);
            }
          }
        }
      }
    } catch (e) {
      if (context.mounted) {
        ShowModalComponent(
          title: "Parece que ocorreu um erro inesperado. 😕",
          subtitle: "Tenta novamente mais tarde!",
          onPressed: () {
            navigateBack(context);
          },
          titleButton: "Voltar",
        ).modalBottomSheet(context);
      }
    } finally {
      _loading = false;
    }
  }

  @action
  bool _validateBoxSelectedContact() {
    if (_contactsPasswordSelected == null) {
      _checkBoxError = "Campo obrigatório";
      return false;
    }
    _checkBoxError = null;
    return true;
  }

  @action
  bool _validateSizeUserName() {
    if (_username.length == 14 || _username.length == 18) {
      return true;
    }
    _usernameError = "Campo incorreto";
    return false;
  }

  @action
  bool _validateUsername() {
    if (_username == null || (_username != null && _username == "")) {
      _usernameError = "Campo obrigatório";
      return false;
    }
    _usernameError = null;
    return true;
  }

  @action
  void cleanData() {
    _checkBoxSelect = null;
    _username = "";
    _checkBoxError = null;
    _usernameError = null;
    _stepRequest = StepRequest.sendRequestContacts;
    _nameButtonRequest = "Consultar";
    _contactsPasswordSelected = null;
  }

  @action
  void cleanToPageToken() {
    _checkBoxSelect = null;
    _checkBoxError = null;
    _usernameError = null;
    _stepRequest = StepRequest.sendRequestContacts;
    _nameButtonRequest = "Consultar";
  }

  @action
  void initData({required String usernameByLogin}) {
    _username = usernameByLogin;
    if (!_validateSizeUserName()) _username = "";
    _checkBoxError = null;
    _usernameError = null;
  }

  @action
  Future<void> sendRequestTokenAgain({required BuildContext context}) async {
    _stepRequest = StepRequest.sendRequestToken;
    await requestTokenForgottenPassword(context: context, pushPage: false);
  }
}
