// ignore_for_file: must_be_immutable

import 'package:arenco_app/presentation/forgotten_password/insert_username/view_model/insert_username_view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';

class ButtonsWidget extends StatelessWidget {
  ButtonsWidget({
    required this.paddingButtons,
    required this.showButtons,
    super.key,
  });
  final insertUsernameViewModel = GetIt.I.get<InsertUsernameViewModel>();
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
                          loading: insertUsernameViewModel.loading,
                          onPressed: () async {
                            insertUsernameViewModel
                                .requestTokenForgottenPassword(
                              context: context,
                            );
                          },
                          textButton: insertUsernameViewModel.nameButtonRequest,
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
