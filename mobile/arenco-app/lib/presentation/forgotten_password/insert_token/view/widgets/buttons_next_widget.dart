// ignore_for_file: must_be_immutable

import 'package:arenco_app/presentation/forgotten_password/insert_token/view_model/view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';
import 'package:timer_button/timer_button.dart';

class ButtonsNextWidget extends StatelessWidget {
  ButtonsNextWidget({
    required this.showButtons,
    required this.paddingButtons,
    super.key,
  });
  final insertTokenViewModel = GetIt.I.get<InsertTokenViewModel>();
  bool showButtons;
  bool paddingButtons;

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
                          loading: insertTokenViewModel.loadingNextButton,
                          onPressed: () async {
                            await insertTokenViewModel
                                .finishPageToRecoveryPassword(
                              context: context,
                            );
                          },
                          textButton: "Próximo",
                        );
                      },
                    ),
                    SizedBox(
                      height: context.paddingPercentHeight(heightPadding: 8),
                    ),
                    ColoredBox(
                      color: Colors.transparent,
                      child: TimerButton.builder(
                        onPressed: () async {
                          await insertTokenViewModel.requestTokenAgain(
                            context: context,
                          );
                        },
                        timeOutInSeconds: 60,
                        builder: (context, timeLeft) {
                          return SizedBox(
                            width: double.infinity,
                            child: Observer(
                              builder: (context) {
                                return Padding(
                                  padding:
                                      const EdgeInsets.symmetric(vertical: 12),
                                  child: insertTokenViewModel
                                          .loadingRequestPinAgain
                                      ? Center(
                                          child: SizedBox(
                                            height: 20,
                                            width: 20,
                                            child: CircularProgressIndicator(
                                              valueColor:
                                                  AlwaysStoppedAnimation<Color>(
                                                ColorsTheme.textColorGrey!,
                                              ),
                                              strokeWidth: 2.5,
                                            ),
                                          ),
                                        )
                                      : Padding(
                                          padding: const EdgeInsets.symmetric(
                                            vertical: 1.0,
                                          ),
                                          child: AutoSizeText(
                                            timeLeft == -1
                                                ? "Enviar novamente!"
                                                : "Enviar novamente em ${timeLeft}s",
                                            maxFontSize: 14,
                                            textAlign: TextAlign.center,
                                            style: Theme.of(context)
                                                .textTheme
                                                .headlineLarge
                                                ?.copyWith(
                                                  fontSize: context.isTablet()
                                                      ? 14
                                                      : 16,
                                                  color: timeLeft == -1
                                                      ? ColorsTheme
                                                          .buttonYellowColor
                                                      : Colors.grey[350],
                                                ),
                                          ),
                                        ),
                                );
                              },
                            ),
                          );
                        },
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
