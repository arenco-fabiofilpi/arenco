import 'package:arenco_app/presentation/presentation.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:flutter/material.dart';

 mixin RoutesConfig {
  /// Set all App's routes
  ///
  /// example:
  /// ```dart
  /// nameExample : (context) => Container(),
  /// ```
  Map<String, Widget Function(BuildContext context)> config(
    BuildContext context,
  ) {
    return {
      splashScreen: (context) =>  SplashView(),
      loginScreen: (context) =>  LoginView(),
      forgottenPassword: (context) =>  ForgottenPasswordView(),
      homeScreen: (context) =>  HomeView(),
      accountDetail: (context) => AccountDetailView(),
      insertToken: (context) => InsertTokenView(),
      resetPassword: (context) => ResetPasswordView(),
    };
  }
}
