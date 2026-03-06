import 'package:arenco_app/presentation/forgotten_password/insert_token/view/widgets/widgets.dart';
import 'package:arenco_app/presentation/forgotten_password/insert_token/view_model/view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/shared_library.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:pinput/pinput.dart';
import 'package:timer_button/timer_button.dart';

class InsertTokenView extends StatelessWidget with Navigation {
  InsertTokenView({super.key});

  final insertTokenViewModel = GetIt.I.get<InsertTokenViewModel>();

  final defaultPinTheme = PinTheme(
    width: 56,
    height: 76,
    textStyle: const TextStyle(
      fontSize: 20,
      color: ColorsTheme.textColor,
      fontWeight: FontWeight.w600,
    ),
    decoration: BoxDecoration(
      border: Border.all(
        color: ColorsTheme.primaryColor,
      ),
      borderRadius: BorderRadius.circular(20),
    ),
  );

  @override
  Widget build(BuildContext context) {
    return PopScope(
      onPopInvokedWithResult: (didpop, result) {
        insertTokenViewModel.cleanData();
      },
      child: Scaffold(
        appBar: AppBarComponent(
          name: "Redefinição de senha",
          setIconLeadingBackPage: true,
          onTapLeading: () {
            insertTokenViewModel.cleanData();
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
                      "Verifique seu e-mail!\nEnviamos um código de verificação para você 📧✉️.",
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
                    height: 16,
                  ),
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16.0),
                    child: Pinput(
                      defaultPinTheme: defaultPinTheme,
                      validator: (s) {
                        return s?.length == 6 ? null : 'Código incorreto';
                      },
                      onChanged: (value) {
                        insertTokenViewModel.changePinCode(value: value);
                      },
                      length: 6,
                      onCompleted: (pin) =>
                          insertTokenViewModel.finishPageToRecoveryPassword(
                        context: context,
                      ),
                    ),
                  ),
                  SizedBox(
                    height: context.paddingPercentHeight(heightPadding: 64),
                  ),
                  Column(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      ButtonsNextWidget(
                        showButtons:
                            MediaQuery.of(context).viewInsets.bottom > 0,
                        paddingButtons: true,
                      ),
                    ],
                  ),
                ],
              ),
            ),
            ButtonsNextWidget(
              showButtons: MediaQuery.of(context).viewInsets.bottom <= 0,
              paddingButtons: true,
            ),
          ],
        ),
      ),
    );
  }
}
