import 'package:animate_do/animate_do.dart';
import 'package:arenco_app/presentation/forgotten_password/insert_username/view/widgets/widget.dart';
import 'package:arenco_app/presentation/forgotten_password/insert_username/view_model/view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/routes/routes.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';

class ForgottenPasswordView extends StatelessWidget
    with ColorsTheme, Navigation {
  ForgottenPasswordView({super.key});

  final insertUsernameViewModel = GetIt.I.get<InsertUsernameViewModel>();

  @override
  Widget build(BuildContext context) {
    return PopScope(
      onPopInvokedWithResult: (value, result) async {
        insertUsernameViewModel.cleanData();
      },
      child: Scaffold(
        appBar: AppBarComponent(
          name: "Redefinição de senha",
          setIconLeadingBackPage: true,
          onTapLeading: () async {
            insertUsernameViewModel.cleanData();
            navigateBack(context);
          },
        ),
        backgroundColor: Colors.white,
        body: Stack(
          children: [
            Padding(
              padding: EdgeInsets.only(
                bottom: MediaQuery.of(context).viewInsets.bottom <= 0
                    ? context.paddingTopStatusBar() +
                        context.paddingPercentHeight(
                          heightPadding: 70,
                        )
                    : 0,
              ),
              child: ListView(
                children: [
                  const SizedBox(
                    height: 64,
                  ),
                  Padding(
                    padding: EdgeInsets.symmetric(
                      horizontal: context.paddingPercentWidth(widthPadding: 24),
                    ),
                    child: AutoSizeText(
                      "Quase lá! Siga os próximos passos\npara redefinir sua senha 🔒😊.",
                      minFontSize: 14,
                      maxFontSize: 16,
                      style: Theme.of(context)
                          .textTheme
                          .headlineLarge
                          ?.copyWith(color: ColorsTheme.textColorBlack),
                      textAlign: TextAlign.center,
                    ),
                  ),
                  const SizedBox(
                    height: 64,
                  ),
                  Padding(
                    padding: EdgeInsets.symmetric(
                      horizontal: context.paddingPercentHorizontal(width: 22),
                    ),
                    child: AutoSizeText(
                      "1. Informe o CPF/CNPJ cadastrado na sua conta 😊",
                      maxFontSize: 14,
                      style: Theme.of(context)
                          .textTheme
                          .headlineSmall
                          ?.copyWith(color: ColorsTheme.textColorBlack),
                    ),
                  ),
                  Padding(
                    padding: EdgeInsets.symmetric(
                      horizontal: context.paddingPercentHorizontal(width: 22),
                      vertical: context.paddingPercentHeight(heightPadding: 12),
                    ),
                    child: Observer(
                      builder: (context) {
                        return FormFieldComponent(
                          label: "",
                          hintText: insertUsernameViewModel.username,
                          onChange: (value) {
                            insertUsernameViewModel.changeUsername(
                              username: value,
                            );
                          },
                          inputFormatterList: [CpfCnpjFormatter()],
                          textInputAction: TextInputAction.go,
                          errorText: insertUsernameViewModel.usernameError,
                          inputType: const TextInputType.numberWithOptions(
                            signed: true,
                            decimal: true,
                          ),
                        );
                      },
                    ),
                  ),
                  Observer(
                    builder: (_) {
                      return insertUsernameViewModel.loadingRequestContacts
                          ? const LoadingContactsWidget()
                          : insertUsernameViewModel.stepRequest ==
                                  StepRequest.sendRequestToken
                              ? FadeInRight(
                                  duration: const Duration(
                                    seconds: 3,
                                  ),
                                  child: ChoseContactWidget(),
                                )
                              : Container();
                    },
                  ),
                ],
              ),
            ),
            ButtonsWidget(
              showButtons: MediaQuery.of(context).viewInsets.bottom <= 0,
              paddingButtons: true,
            ),
          ],
        ),
      ),
    );
  }
}
