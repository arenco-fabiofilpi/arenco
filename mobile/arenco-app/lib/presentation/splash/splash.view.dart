import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/infra/infra.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/routes/routes.dart';
import 'package:flutter/material.dart';
import 'package:responsive_builder/responsive_builder.dart';

class SplashView extends StatelessWidget with Navigation {
  const SplashView({super.key});

  Future<void> enterUserOnApp({required BuildContext context}) async {
    final loginData = await GetUsernameLocalStorage().getUserData();

    if (loginData.username != '') {
      final response = await LoginRepository().loginRequest(
        loginParams: loginData,
      );

      switch (response.statusData) {
        case StatusData.unauthorized || StatusData.forbidden:
          if (context.mounted) pushReplacement(context, page: loginScreen);
        default:
          if (context.mounted) pushReplacement(context, page: homeScreen);
      }
    } else {
      if (context.mounted) pushReplacement(context, page: loginScreen);
    }
  }

  @override
  Widget build(BuildContext context) {
    enterUserOnApp(context: context);

    return ColoredBox(
      color: Theme.of(context).primaryColor,
      child: ResponsiveBuilder(
        builder: (context, sizingInformation) => Center(
          child: Image.asset(
            logoApp,
            width: sizingInformation.deviceScreenType == DeviceScreenType.mobile
                ? context.screenWidth() / 2
                : 250,
          ),
        ),
      ),
    );
  }
}
