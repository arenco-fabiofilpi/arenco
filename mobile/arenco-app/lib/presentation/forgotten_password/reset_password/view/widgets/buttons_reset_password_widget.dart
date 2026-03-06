// ignore_for_file: must_be_immutable

import 'package:arenco_app/presentation/forgotten_password/reset_password/view_model/view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';

class ButtonsResetPasswordWidget extends StatelessWidget {
  ButtonsResetPasswordWidget({
    required this.paddingButtons,
    required this.showButtons,
    super.key,
  });
  bool showButtons;
  bool paddingButtons;
  final resetPasswordViewModel = GetIt.I.get<ResetPasswordViewModel>();

  @override
  Widget build(BuildContext context) {
    return showButtons
        ? Padding(
            padding: EdgeInsets.only(
              bottom: context.paddingTopStatusBar() +
                  context.paddingPercentHeight(
                    heightPadding: 26,
                  ),
              left: !paddingButtons
                  ? 0
                  : context.paddingPercentHorizontal(width: 22),
              right: !paddingButtons
                  ? 0
                  : context.paddingPercentHorizontal(width: 22),
            ),
            child: Align(
              alignment: Alignment.bottomCenter,
              child: ColoredBox(
                color: Colors.white,
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Container(
                      height: context.paddingPercentHeight(heightPadding: 8),
                      color: Colors.white,
                    ),
                    Observer(
                      builder: (context) {
                        return ElevatedButtonComponent(
                          colorButton:
                              resetPasswordViewModel.checkCompletedPassword
                                  ? null
                                  : Colors.grey[300],
                          loading: resetPasswordViewModel.loadingButton,
                          onPressed: () async {
                            await resetPasswordViewModel.newPasswordWithToken(context: context);
                          },
                          textButton: "Redefinir senha",
                        );
                      },
                    ),
                    SizedBox(
                      height: context.paddingPercentHeight(heightPadding: 8),
                    ),
                    Opacity(
                      opacity: 0.0,
                      child: IgnorePointer(
                        child: ElevatedButtonComponent(
                          loading: false,
                          onPressed: () async {},
                          textButton: enter,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          )
        : Container();
  }
}
