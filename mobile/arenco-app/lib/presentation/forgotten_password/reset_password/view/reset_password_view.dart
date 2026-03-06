import 'package:arenco_app/presentation/forgotten_password/reset_password/view/widgets/widget.dart';
import 'package:arenco_app/presentation/forgotten_password/reset_password/view_model/view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/shared_library.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';
import 'package:password_strength_checker/password_strength_checker.dart';

class ResetPasswordView extends StatelessWidget with Navigation {
  ResetPasswordView({super.key});

  final resetPasswordViewModel = GetIt.I.get<ResetPasswordViewModel>();
  final listRegex = getListRegex();

  @override
  Widget build(BuildContext context) {
    final passNotifier = ValueNotifier<PasswordStrength?>(null);
    return PopScope(
      onPopInvokedWithResult: (value, result) async {
        resetPasswordViewModel.resetData();
      },
      child: Scaffold(
        appBar: AppBarComponent(
          name: "Redefinição de senha",
          setIconLeadingBackPage: true,
          onTapLeading: () async {
            resetPasswordViewModel.resetData();
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
                      "Agora, insira a nova senha que\ndeseja utilizar 🔐😊",
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
                    height: 32,
                  ),
                  Padding(
                    padding: EdgeInsets.symmetric(
                      horizontal: context.paddingPercentHorizontal(width: 22),
                    ),
                    child: Observer(
                      builder: (context) {
                        return FormFieldResetPassword(
                          label: password,
                          onChanged: (value) async {
                            resetPasswordViewModel.changeResetPassword(value);
                            passNotifier.value =
                                PasswordStrength.calculate(text: value);
                          },
                          errorText: resetPasswordViewModel.newPasswordErro,
                          obscureText: resetPasswordViewModel.obscureText,
                          prefixIcon: Icons.lock,
                          suffixIcon: IconButton(
                            highlightColor: const Color.fromARGB(0, 73, 71, 71),
                            onPressed: () async {
                              resetPasswordViewModel.setObscureText();
                            },
                            icon: Padding(
                              padding: const EdgeInsets.only(right: 8, left: 8),
                              child: Icon(
                                resetPasswordViewModel.obscureText
                                    ? Icons.remove_red_eye_outlined
                                    : Icons.visibility_off_outlined,
                                color: Colors.grey[600],
                              ),
                            ),
                          ),
                        );
                      },
                    ),
                  ),
                  const SizedBox(
                    height: 16,
                  ),
                  Padding(
                    padding: EdgeInsets.symmetric(
                      horizontal: context.paddingPercentHorizontal(width: 22),
                    ),
                    child: Observer(
                      builder: (context) {
                        return FormFieldResetPassword(
                          label: "Confirmar senha",
                          onChanged:
                              resetPasswordViewModel.changeResetPasswordAgain,
                          errorText:
                              resetPasswordViewModel.newPasswordAgainErro,
                          obscureText: resetPasswordViewModel.obscureSecondText,
                          prefixIcon: Icons.lock,
                          suffixIcon: IconButton(
                            highlightColor: const Color.fromARGB(0, 73, 71, 71),
                            onPressed: () async {
                              resetPasswordViewModel.setObscureSecondText();
                            },
                            icon: Padding(
                              padding: const EdgeInsets.only(right: 8, left: 8),
                              child: Icon(
                                resetPasswordViewModel.obscureSecondText
                                    ? Icons.remove_red_eye_outlined
                                    : Icons.visibility_off_outlined,
                                color: Colors.grey[600],
                              ),
                            ),
                          ),
                        );
                      },
                    ),
                  ),
                  const SizedBox(
                    height: 32,
                  ),
                  Padding(
                    padding: EdgeInsets.symmetric(
                      horizontal: context.paddingPercentHorizontal(width: 22),
                    ),
                    child: Observer(
                      builder: (_) => PasswordStrengthComponent(
                        password: resetPasswordViewModel.newPassword,
                        checkPasswordList: {
                          '8 caracteres no mínimo': listRegex[0],
                          "Uma letra maiúscula": listRegex[1],
                          "Uma letra minúscula": listRegex[2],
                          "Um número": listRegex[3],
                          "Um caractere especial": listRegex[4],
                        },
                      ),
                    ),
                  ),
                  SizedBox(
                    height: context.paddingPercentHeight(heightPadding: 64),
                  ),
                  Column(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      ButtonsResetPasswordWidget(
                        showButtons:
                            MediaQuery.of(context).viewInsets.bottom > 0,
                        paddingButtons: true,
                      ),
                    ],
                  ),
                ],
              ),
            ),
            ButtonsResetPasswordWidget(
              showButtons: MediaQuery.of(context).viewInsets.bottom <= 0,
              paddingButtons: true,
            ),
          ],
        ),
      ),
    );
  }
}
